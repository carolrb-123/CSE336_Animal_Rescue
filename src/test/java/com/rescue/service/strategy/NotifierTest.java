package com.rescue.service.strategy;

import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Donor;
import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;
import com.rescue.repo.SentMessageRepository;
import com.rescue.service.RecordingGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotifierTest {

    private RecordingGateway gateway;
    private Notifier notifier;

    private final Donor donor = new Donor(1, "Sara", "sara@example.com", "+201000000001");
    private final Contact volunteer = new Contact("Youssef", "y@example.com", "+201000000002");

    @BeforeEach
    void setUp() {
        gateway = new RecordingGateway();
        notifier = new Notifier(new SentMessageRepository())
                .register(new EmailStrategy(gateway, "Animal Rescue"))
                .register(new SmsStrategy(gateway, "Animal Rescue"))
                .register(new SocialPostStrategy(gateway, "Animal Rescue"));
    }

    @Test
    void picksStrategyFromRecipientTypeAtRuntime() {
        assertEquals(ChannelType.EMAIL, notifier.send(donor, RecipientType.DONOR, "hi").getChannel());
        assertEquals(ChannelType.SMS, notifier.send(volunteer, RecipientType.VOLUNTEER, "hi").getChannel());
        assertEquals(ChannelType.SOCIAL_POST, notifier.postPublicly("hi").getChannel());
    }

    @Test
    void changingTheDefaultChangesTheStrategy() {
        notifier.setDefaultChannel(RecipientType.DONOR, ChannelType.SMS);
        assertEquals(ChannelType.SMS, notifier.send(donor, RecipientType.DONOR, "hi").getChannel());
    }

    @Test
    void fallsBackWhenPreferredChannelCannotReach() {
        Donor noEmail = new Donor(2, "Karim", "", "01112223334");
        SentMessage result = notifier.send(noEmail, RecipientType.DONOR, "hi");
        assertTrue(result.isSent());
        assertEquals(ChannelType.SMS, result.getChannel());
    }

    @Test
    void failsCleanlyWhenNothingCanReach() {
        Donor unreachable = new Donor(3, "Ghost", "not-an-email", "12");
        assertFalse(notifier.send(unreachable, RecipientType.DONOR, "hi").isSent());
        assertTrue(gateway.sent.isEmpty());
    }

    @Test
    void socialPostNeverGoesToAPerson() {
        SentMessage result = notifier.send(donor, RecipientType.DONOR, "hi", ChannelType.SOCIAL_POST);
        assertFalse(result.isSent());
        assertTrue(result.getFailureReason().contains("public page"));
    }

    @Test
    void gatewayErrorIsLoggedAsFailure() {
        gateway.failNext = true;
        SentMessage result = notifier.send(donor, RecipientType.DONOR, "hi");
        assertFalse(result.isSent());
        assertTrue(result.getFailureReason().contains("gateway down"));
    }

    @Test
    void sharedInterfaceSendStillWorks() {
        new SmsStrategy(gateway, "Animal Rescue").send(donor, "plain NotificationStrategy call");
        assertEquals(1, gateway.count(ChannelType.SMS));
    }

    @Test
    void everyAttemptIsLoggedWithAnId() {
        notifier.broadcast(List.of(donor, volunteer), RecipientType.DONOR, "hi");
        notifier.send(donor, RecipientType.DONOR, "hi", ChannelType.SOCIAL_POST);
        assertEquals(3, notifier.history().size());
        assertTrue(notifier.history().stream().allMatch(m -> m.getId() > 0));
    }
}
