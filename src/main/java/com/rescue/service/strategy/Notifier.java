package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;
import com.rescue.repo.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// STRATEGY pattern - the context.
// Everyone who wants to send a message calls the Notifier.
// It chooses the strategy at runtime from the recipient type:
// donors get email, volunteers get SMS, the public gets a social post.
// If that channel can't reach the person, it tries the other channels.
public class Notifier {

    private final List<ChannelStrategy> strategies = new ArrayList<>();
    private final HashMap<RecipientType, ChannelType> defaultChannels = new HashMap<>();
    private final Repository<SentMessage> messageLog;

    public Notifier(Repository<SentMessage> messageLog) {
        this.messageLog = messageLog;
        defaultChannels.put(RecipientType.DONOR, ChannelType.EMAIL);
        defaultChannels.put(RecipientType.VOLUNTEER, ChannelType.SMS);
        defaultChannels.put(RecipientType.ADOPTER, ChannelType.EMAIL);
        defaultChannels.put(RecipientType.COORDINATOR, ChannelType.SMS);
        defaultChannels.put(RecipientType.PUBLIC, ChannelType.SOCIAL_POST);
    }

    public void addStrategy(ChannelStrategy strategy) {
        strategies.add(strategy);
    }

    public ChannelType getDefaultChannel(RecipientType type) {
        return defaultChannels.get(type);
    }

    public void setDefaultChannel(RecipientType type, ChannelType channel) {
        defaultChannels.put(type, channel);
    }

    // Send using the default channel for this recipient type (with fallback).
    public SentMessage send(Recipient recipient, RecipientType type, String message) {
        // 1) Try the default channel for this type.
        ChannelStrategy preferred = findStrategy(defaultChannels.get(type));
        if (preferred != null && preferred.canReach(recipient)) {
            return deliver(preferred, recipient, type, message);
        }

        // 2) Fallback: try every other channel, in order.
        for (ChannelStrategy strategy : strategies) {
            if (strategy.canReach(recipient)) {
                return deliver(strategy, recipient, type, message);
            }
        }

        // 3) No channel can reach this person.
        return saveFailure(recipient, type, null,
                "No channel can reach " + recipient.getName() + " (no valid email or phone).");
    }

    // Send on one channel chosen by the user (no fallback).
    public SentMessage send(Recipient recipient, RecipientType type, String message, ChannelType channel) {
        ChannelStrategy strategy = findStrategy(channel);
        if (strategy == null) {
            return saveFailure(recipient, type, channel, channel + " is not set up.");
        }
        if (!strategy.canReach(recipient)) {
            if (channel == ChannelType.SOCIAL_POST) {
                return saveFailure(recipient, type, channel,
                        "Social posts go to the public page only, not to a person.");
            }
            return saveFailure(recipient, type, channel,
                    recipient.getName() + " can't be reached by " + channel + ".");
        }
        return deliver(strategy, recipient, type, message);
    }

    // Send the same message to many people. Each one still gets their best channel.
    public List<SentMessage> broadcast(List<Recipient> recipients, RecipientType type, String message) {
        List<SentMessage> results = new ArrayList<>();
        for (Recipient recipient : recipients) {
            results.add(send(recipient, type, message));
        }
        return results;
    }

    // Post on the rescue's public social page.
    public SentMessage postPublicly(String message) {
        return send(Contact.PUBLIC_AUDIENCE, RecipientType.PUBLIC, message);
    }

    // All messages, newest first.
    public List<SentMessage> getHistory() {
        List<SentMessage> all = messageLog.findAll();
        List<SentMessage> newestFirst = new ArrayList<>();
        for (int i = all.size() - 1; i >= 0; i--) {
            newestFirst.add(all.get(i));
        }
        return newestFirst;
    }

    private ChannelStrategy findStrategy(ChannelType channel) {
        for (ChannelStrategy strategy : strategies) {
            if (strategy.getChannel() == channel) {
                return strategy;
            }
        }
        return null;
    }

    private SentMessage deliver(ChannelStrategy strategy, Recipient recipient, RecipientType type, String message) {
        try {
            String text = strategy.format(recipient, message);
            strategy.send(recipient, message);
            SentMessage result = new SentMessage(recipient.getName(), type, strategy.getChannel(),
                    strategy.getAddress(recipient), text, true, null);
            messageLog.save(result);
            return result;
        } catch (Exception e) {
            // If sending fails, log it instead of crashing the program.
            return saveFailure(recipient, type, strategy.getChannel(), "Sending failed: " + e.getMessage());
        }
    }

    private SentMessage saveFailure(Recipient recipient, RecipientType type, ChannelType channel, String reason) {
        SentMessage result = new SentMessage(recipient.getName(), type, channel, null, null, false, reason);
        messageLog.save(result);
        return result;
    }
}
