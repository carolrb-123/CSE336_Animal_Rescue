package com.rescue.service.strategy;

import com.rescue.model.Contact;
import com.rescue.service.RecordingGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyFormatTest {

    private final RecordingGateway gateway = new RecordingGateway();
    private final Contact sara = new Contact("Sara", "sara@example.com", "+20 100 000 0001");

    @Test
    void emailHasGreetingAndSignature() {
        String text = new EmailStrategy(gateway, "Animal Rescue").format(sara, "Body");
        assertTrue(text.startsWith("Dear Sara,"));
        assertTrue(text.endsWith("Animal Rescue"));
    }

    @Test
    void emailRejectsInvalidAddress() {
        assertFalse(new EmailStrategy(gateway, "X").canReach(new Contact("Sara", "sara.example.com", null)));
    }

    @Test
    void smsIsCutTo160Characters() {
        String text = new SmsStrategy(gateway, "Animal Rescue").format(sara, "x".repeat(500));
        assertEquals(SmsStrategy.MAX_LENGTH, text.length());
        assertTrue(text.endsWith("..."));
    }

    @Test
    void smsAcceptsPhoneWithSpaces() {
        assertTrue(new SmsStrategy(gateway, "X").canReach(sara));
    }

    @Test
    void socialPostIsPublicOnlyAndFitsLimit() {
        SocialPostStrategy social = new SocialPostStrategy(gateway, "Animal Rescue");
        assertFalse(social.canReach(sara));
        String post = social.format(Contact.PUBLIC_AUDIENCE, "y".repeat(600));
        assertTrue(post.length() <= SocialPostStrategy.MAX_LENGTH);
        assertTrue(post.contains("#AnimalRescue"));
    }
}
