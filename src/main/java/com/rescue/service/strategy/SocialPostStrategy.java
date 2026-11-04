package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Recipient;

// STRATEGY pattern - concrete strategy: a post on the rescue's social page.
// A post is public, so it can ONLY go to Contact.PUBLIC_AUDIENCE, never to a person.
public class SocialPostStrategy extends ChannelStrategy {

    public static final int MAX_LENGTH = 280;
    private static final String HASHTAGS = "\n\n#AnimalRescue #AdoptDontShop";

    public SocialPostStrategy(String organisationName) {
        super(organisationName);
    }

    @Override
    public ChannelType getChannel() {
        return ChannelType.SOCIAL_POST;
    }

    @Override
    public boolean canReach(Recipient recipient) {
        return recipient == Contact.PUBLIC_AUDIENCE;
    }

    @Override
    public String getAddress(Recipient recipient) {
        return organisationName + " social page";
    }

    @Override
    public String format(Recipient recipient, String message) {
        // Leave room for the hashtags so the whole post stays within 280 characters.
        int room = MAX_LENGTH - HASHTAGS.length();
        if (message.length() > room) {
            message = message.substring(0, room - 3) + "...";
        }
        return message + HASHTAGS;
    }
}
