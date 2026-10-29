package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Recipient;

import java.util.regex.Pattern;

/** STRATEGY pattern: ConcreteStrategy for SMS. One line, cut to 160 characters. */
public class SmsStrategy extends ChannelStrategy {

    public static final int MAX_LENGTH = 160;
    private static final Pattern PHONE = Pattern.compile("^\\+?\\d{7,15}$");

    public SmsStrategy(MessageGateway gateway, String organisationName) {
        super(gateway, organisationName);
    }

    @Override
    public ChannelType getChannel() {
        return ChannelType.SMS;
    }

    @Override
    public boolean canReach(Recipient recipient) {
        return recipient.getPhone() != null && PHONE.matcher(normalise(recipient.getPhone())).matches();
    }

    @Override
    public String addressOf(Recipient recipient) {
        return normalise(recipient.getPhone());
    }

    @Override
    public String format(Recipient recipient, String message) {
        String text = (organisationName + ": " + message).replaceAll("\\s+", " ").trim();
        return text.length() <= MAX_LENGTH ? text : text.substring(0, MAX_LENGTH - 3) + "...";
    }

    @Override
    public String whyUnreachable(Recipient recipient) {
        return recipient.getName() + " has no valid phone number.";
    }

    private static String normalise(String phone) {
        return phone.replaceAll("[\\s\\-()]", "");
    }
}
