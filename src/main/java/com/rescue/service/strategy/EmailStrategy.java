package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Recipient;

import java.util.regex.Pattern;

/** STRATEGY pattern: ConcreteStrategy for email. Greeting, message and signature. */
public class EmailStrategy extends ChannelStrategy {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public EmailStrategy(MessageGateway gateway, String organisationName) {
        super(gateway, organisationName);
    }

    @Override
    public ChannelType getChannel() {
        return ChannelType.EMAIL;
    }

    @Override
    public boolean canReach(Recipient recipient) {
        return recipient.getEmail() != null && EMAIL.matcher(recipient.getEmail().trim()).matches();
    }

    @Override
    public String addressOf(Recipient recipient) {
        return recipient.getEmail().trim();
    }

    @Override
    public String format(Recipient recipient, String message) {
        return "Dear " + recipient.getName() + ",\n\n"
                + message.trim() + "\n\n"
                + "With thanks,\n" + organisationName;
    }

    @Override
    public String whyUnreachable(Recipient recipient) {
        return recipient.getName() + " has no valid email address.";
    }
}
