package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Recipient;

/**
 * STRATEGY pattern: ConcreteStrategy for a post on the rescue's social page.
 * A post is public, so it may only go to Contact.PUBLIC_AUDIENCE, never to a named person.
 * That stops a private message (a donor's thank-you, say) from ending up on the page.
 */
public class SocialPostStrategy extends ChannelStrategy {

    public static final int MAX_LENGTH = 280;
    private static final String HASHTAGS = "\n\n#AnimalRescue #AdoptDontShop";

    public SocialPostStrategy(MessageGateway gateway, String organisationName) {
        super(gateway, organisationName);
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
    public String addressOf(Recipient recipient) {
        return organisationName + " social page";
    }

    @Override
    public String format(Recipient recipient, String message) {
        String head = message.trim();
        int room = MAX_LENGTH - HASHTAGS.length();
        if (head.length() > room) {
            head = head.substring(0, room - 3) + "...";
        }
        return head + HASHTAGS;
    }

    @Override
    public String whyUnreachable(Recipient recipient) {
        return "Social posts go to the public page only, not to a person.";
    }
}
