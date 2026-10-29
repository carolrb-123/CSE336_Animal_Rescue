package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;
import com.rescue.repo.Repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * STRATEGY pattern: the Context. Every part of the system sends messages through here.
 * <p>
 * At runtime it picks the strategy from the recipient type: donors get email, volunteers
 * get SMS, the public gets a social post. If that channel can't reach the person (a donor
 * with no email, say), it falls back to any other channel that can. Every attempt, sent or
 * failed, is saved in the message log.
 * <p>
 * Phase 2 hook: this class is the Facade target, the single entry point for all communication.
 */
public class Notifier {

    private final Map<ChannelType, ChannelStrategy> strategies = new EnumMap<>(ChannelType.class);
    private final Map<RecipientType, ChannelType> defaultChannels = new EnumMap<>(RecipientType.class);
    private final Repository<SentMessage> messageLog;

    public Notifier(Repository<SentMessage> messageLog) {
        this.messageLog = Objects.requireNonNull(messageLog, "messageLog");
        defaultChannels.put(RecipientType.DONOR, ChannelType.EMAIL);
        defaultChannels.put(RecipientType.VOLUNTEER, ChannelType.SMS);
        defaultChannels.put(RecipientType.ADOPTER, ChannelType.EMAIL);
        defaultChannels.put(RecipientType.COORDINATOR, ChannelType.SMS);
        defaultChannels.put(RecipientType.PUBLIC, ChannelType.SOCIAL_POST);
    }

    /** Adds (or replaces) the strategy for its channel. Returns this so calls can be chained. */
    public Notifier register(ChannelStrategy strategy) {
        strategies.put(strategy.getChannel(), strategy);
        return this;
    }

    public void setDefaultChannel(RecipientType type, ChannelType channel) {
        defaultChannels.put(type, channel);
    }

    public ChannelType getDefaultChannel(RecipientType type) {
        return defaultChannels.get(type);
    }

    public Set<ChannelType> availableChannels() {
        return strategies.keySet();
    }

    /** Sends on the channel chosen for this recipient type, falling back if it can't reach them. */
    public SentMessage send(Recipient recipient, RecipientType type, String message) {
        ChannelStrategy strategy = chooseStrategy(recipient, type);
        if (strategy == null) {
            return record(SentMessage.failed(recipient, type, null,
                    "No channel can reach " + recipient.getName() + " (no valid email or phone)."));
        }
        return deliver(strategy, recipient, type, message);
    }

    /** Sends on one specific channel, with no fallback. Used when the user picks the channel by hand. */
    public SentMessage send(Recipient recipient, RecipientType type, String message, ChannelType channel) {
        ChannelStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            return record(SentMessage.failed(recipient, type, channel, channel.label() + " is not set up."));
        }
        if (!strategy.canReach(recipient)) {
            return record(SentMessage.failed(recipient, type, channel, strategy.whyUnreachable(recipient)));
        }
        return deliver(strategy, recipient, type, message);
    }

    /** Sends one message to many people of the same type; each still gets their best channel. */
    public List<SentMessage> broadcast(Collection<? extends Recipient> recipients, RecipientType type, String message) {
        return recipients.stream().map(r -> send(r, type, message)).toList();
    }

    /** Posts on the rescue's public social page. */
    public SentMessage postPublicly(String message) {
        return send(Contact.PUBLIC_AUDIENCE, RecipientType.PUBLIC, message);
    }

    /** The message log, newest first. */
    public List<SentMessage> history() {
        return messageLog.findAll().stream()
                .sorted(Comparator.comparing(SentMessage::getSentAt).thenComparing(SentMessage::getId).reversed())
                .toList();
    }

    /** The runtime choice of strategy. Package-visible so tests can check it. */
    ChannelStrategy chooseStrategy(Recipient recipient, RecipientType type) {
        ChannelStrategy preferred = strategies.get(defaultChannels.get(type));
        if (preferred != null && preferred.canReach(recipient)) {
            return preferred;
        }
        for (ChannelStrategy fallback : strategies.values()) {
            if (fallback.canReach(recipient)) {
                return fallback;
            }
        }
        return null;
    }

    private SentMessage deliver(ChannelStrategy strategy, Recipient recipient, RecipientType type, String message) {
        try {
            String text = strategy.deliver(recipient, message);
            return record(SentMessage.sent(recipient, type, strategy.getChannel(), strategy.addressOf(recipient), text));
        } catch (RuntimeException e) {
            return record(SentMessage.failed(recipient, type, strategy.getChannel(), "Sending failed: " + e.getMessage()));
        }
    }

    private SentMessage record(SentMessage sentMessage) {
        messageLog.save(sentMessage);
        return sentMessage;
    }
}
