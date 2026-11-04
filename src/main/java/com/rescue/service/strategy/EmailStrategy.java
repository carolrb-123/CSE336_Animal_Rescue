package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Recipient;

// STRATEGY pattern - concrete strategy: send by email.
public class EmailStrategy extends ChannelStrategy {

    public EmailStrategy(String organisationName) {
        super(organisationName);
    }

    @Override
    public ChannelType getChannel() {
        return ChannelType.EMAIL;
    }

    @Override
    public boolean canReach(Recipient recipient) {
        String email = recipient.getEmail();
        return email != null && email.contains("@") && email.contains(".");
    }

    @Override
    public String getAddress(Recipient recipient) {
        return recipient.getEmail();
    }

    @Override
    public String format(Recipient recipient, String message) {
        return "Dear " + recipient.getName() + ",\n\n"
                + message + "\n\n"
                + "With thanks,\n" + organisationName;
    }
}
