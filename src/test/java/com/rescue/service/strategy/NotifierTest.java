package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Donor;
import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;
import com.rescue.service.MemoryMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests for the STRATEGY pattern: does the Notifier choose the right way to send?
class NotifierTest {

    private MemoryMessageRepository log;
    private Notifier notifier;

    private final Donor donor = new Donor(1, "Sara", "sara@example.com", "+201000000001");
    private final Contact volunteer = new Contact("Youssef", "y@example.com", "+201000000002");

    @BeforeEach
    void setUp() {
        log = new MemoryMessageRepository();
        notifier = new Notifier(log);
        notifier.addStrategy(new EmailStrategy("Animal Rescue"));
        notifier.addStrategy(new SmsStrategy("Animal Rescue"));
        notifier.addStrategy(new SocialPostStrategy("Animal Rescue"));
    }

    @Test
    void choosesTheChannelFromTheRecipientType() {
        assertEquals(ChannelType.EMAIL, notifier.send(donor, RecipientType.DONOR, "hi").getChannel());
        assertEquals(ChannelType.SMS, notifier.send(volunteer, RecipientType.VOLUNTEER, "hi").getChannel());
        assertEquals(ChannelType.SOCIAL_POST, notifier.postPublicly("hi").getChannel());
    }

    @Test
    void changingTheDefaultChangesTheChannel() {
        notifier.setDefaultChannel(RecipientType.DONOR, ChannelType.SMS);
        assertEquals(ChannelType.SMS, notifier.send(donor, RecipientType.DONOR, "hi").getChannel());
    }

    @Test
    void usesSmsWhenTheDonorHasNoEmail() {
        Donor noEmail = new Donor(2, "Karim", "", "01112223334");
        SentMessage result = notifier.send(noEmail, RecipientType.DONOR, "hi");
        assertTrue(result.isSent());
        assertEquals(ChannelType.SMS, result.getChannel());
    }

    @Test
    void failsWhenNothingCanReachThePerson() {
        Donor unreachable = new Donor(3, "Ghost", "not-an-email", "12");
        SentMessage result = notifier.send(unreachable, RecipientType.DONOR, "hi");
        assertFalse(result.isSent());
    }

    @Test
    void aSocialPostNeverGoesToAPerson() {
        SentMessage result = notifier.send(donor, RecipientType.DONOR, "hi", ChannelType.SOCIAL_POST);
        assertFalse(result.isSent());
        assertTrue(result.getFailureReason().contains("public page"));
    }

    @Test
    void broadcastGivesEachPersonTheirOwnChannel() {
        List<Recipient> people = new ArrayList<>();
        people.add(new Contact("Only email", "a@example.com", null));
        people.add(new Contact("Only phone", null, "01112223334"));
        notifier.broadcast(people, RecipientType.DONOR, "hi");
        assertEquals(1, log.countSent(ChannelType.EMAIL));
        assertEquals(1, log.countSent(ChannelType.SMS));
    }

    @Test
    void everyAttemptIsSavedNewestFirst() {
        notifier.send(donor, RecipientType.DONOR, "first");
        notifier.send(donor, RecipientType.DONOR, "second", ChannelType.SOCIAL_POST);
        List<SentMessage> history = notifier.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.get(0).isSent());   // the newest one (the failed social post)
    }
}
