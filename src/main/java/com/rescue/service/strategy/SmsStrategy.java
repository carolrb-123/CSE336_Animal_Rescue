package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Recipient;

// STRATEGY pattern - concrete strategy: send by SMS (one line, max 160 characters).
public class SmsStrategy extends ChannelStrategy {

    public static final int MAX_LENGTH = 160;

    public SmsStrategy(String organisationName) {
        super(organisationName);
    }

    @Override
    public ChannelType getChannel() {
        return ChannelType.SMS;
    }

    @Override
    public boolean canReach(Recipient recipient) {
        String phone = recipient.getPhone();
        if (phone == null) {
            return false;
        }
        phone = cleanPhone(phone);
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (phone.length() < 7 || phone.length() > 15) {
            return false;
        }
        // Every character must be a digit.
        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getAddress(Recipient recipient) {
        return cleanPhone(recipient.getPhone());
    }

    @Override
    public String format(Recipient recipient, String message) {
        String text = organisationName + ": " + message.replace("\n", " ");
        if (text.length() > MAX_LENGTH) {
            text = text.substring(0, MAX_LENGTH - 3) + "...";
        }
        return text;
    }

    // Remove spaces and dashes, so "+20 100-123-4567" becomes "+201001234567".
    private String cleanPhone(String phone) {
        return phone.replace(" ", "").replace("-", "");
    }
}
