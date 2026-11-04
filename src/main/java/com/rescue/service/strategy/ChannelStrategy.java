package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Recipient;
import com.rescue.service.NotificationStrategy;

// STRATEGY pattern - the parent of all the sending strategies.
// Every strategy (Email, SMS, Social post) must answer the same questions,
// so the Notifier can treat them all the same way.
public abstract class ChannelStrategy implements NotificationStrategy {

    protected String organisationName;

    public ChannelStrategy(String organisationName) {
        this.organisationName = organisationName;
    }

    // Which channel is this? (EMAIL, SMS or SOCIAL_POST)
    public abstract ChannelType getChannel();

    // Can this channel reach this person? (valid email? valid phone? public page?)
    public abstract boolean canReach(Recipient recipient);

    // Where does the message go? (the email address, the phone number or the page)
    public abstract String getAddress(Recipient recipient);

    // Turn the message into the right shape for this channel.
    public abstract String format(Recipient recipient, String message);

    // Send the message. In this phase "sending" means printing it in the console.
    @Override
    public void send(Recipient recipient, String message) {
        System.out.println("----- " + getChannel() + " to " + getAddress(recipient) + " -----");
        System.out.println(format(recipient, message));
        System.out.println();
    }
}
