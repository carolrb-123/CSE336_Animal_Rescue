package com.rescue.service.strategy;

import com.rescue.model.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for each strategy on its own.
class StrategyFormatTest {

    private final Contact sara = new Contact("Sara", "sara@example.com", "+20 100 000 0001");

    @Test
    void emailHasAGreetingAndASignature() {
        String text = new EmailStrategy("Animal Rescue").format(sara, "Body");
        assertTrue(text.startsWith("Dear Sara,"));
        assertTrue(text.endsWith("Animal Rescue"));
    }

    @Test
    void emailNeedsAnAtSign() {
        Contact bad = new Contact("Sara", "sara.example.com", null);
        assertFalse(new EmailStrategy("X").canReach(bad));
    }

    @Test
    void smsIsCutTo160Characters() {
        String text = new SmsStrategy("Animal Rescue").format(sara, "x".repeat(500));
        assertEquals(SmsStrategy.MAX_LENGTH, text.length());
        assertTrue(text.endsWith("..."));
    }

    @Test
    void smsAcceptsAPhoneWithSpaces() {
        assertTrue(new SmsStrategy("X").canReach(sara));
    }

    @Test
    void smsRejectsAPhoneWithLetters() {
        assertFalse(new SmsStrategy("X").canReach(new Contact("A", null, "0100abc4567")));
    }

    @Test
    void socialPostIsPublicOnlyAndFits280() {
        SocialPostStrategy social = new SocialPostStrategy("Animal Rescue");
        assertFalse(social.canReach(sara));
        String post = social.format(Contact.PUBLIC_AUDIENCE, "y".repeat(600));
        assertTrue(post.length() <= SocialPostStrategy.MAX_LENGTH);
        assertTrue(post.contains("#AnimalRescue"));
    }
}
