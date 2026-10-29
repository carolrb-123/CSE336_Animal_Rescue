package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Recipient;
import com.rescue.service.NotificationStrategy;

/**
 * STRATEGY pattern: base for the concrete strategies. It implements the shared
 * NotificationStrategy interface unchanged, and adds what the Notifier needs to choose
 * between channels at runtime (can this channel reach this person?).
 * <p>
 * Phase 2 hook: deliver() is already a Template Method (check, format, transmit; the
 * subclasses fill in the steps). Label it as such in Phase 2.
 */
public abstract class ChannelStrategy implements NotificationStrategy {

    protected final MessageGateway gateway;
    protected final String organisationName;

    protected ChannelStrategy(MessageGateway gateway, String organisationName) {
        this.gateway = gateway;
        this.organisationName = organisationName;
    }

    public abstract ChannelType getChannel();

    /** True if this channel can reach the recipient (valid email, valid phone, public page...). */
    public abstract boolean canReach(Recipient recipient);

    /** Where the message goes on this channel: an email address, a phone number or a page name. */
    public abstract String addressOf(Recipient recipient);

    /** Formats the message the way this channel needs it. */
    public abstract String format(Recipient recipient, String message);

    /** Shown to the user when canReach() is false. */
    public abstract String whyUnreachable(Recipient recipient);

    /** The shared interface's method. */
    @Override
    public final void send(Recipient recipient, String message) {
        deliver(recipient, message);
    }

    /**
     * Checks, formats and transmits the message.
     *
     * @return exactly the text that was sent
     * @throws IllegalArgumentException if this channel cannot reach the recipient
     */
    public final String deliver(Recipient recipient, String message) {
        if (!canReach(recipient)) {
            throw new IllegalArgumentException(whyUnreachable(recipient));
        }
        String text = format(recipient, message);
        gateway.transmit(getChannel(), addressOf(recipient), text);
        return text;
    }
}
