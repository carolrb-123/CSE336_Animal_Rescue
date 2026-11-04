package com.rescue.service.observer;

import com.rescue.factory.DonationFactory;
import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Donation;
import com.rescue.model.Donor;
import com.rescue.model.Recipient;
import com.rescue.service.CommunicationModule;
import com.rescue.service.MemoryMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests for the OBSERVER pattern: does publishing an event make the observers react?
class ObserverTest {

    private MemoryMessageRepository log;
    private CommunicationModule comms;

    private final Donor donor = new Donor(1, "Sara", "sara@example.com", "01000000001");
    private final Contact coordinator = new Contact("Mona", null, "+201000000009");

    @BeforeEach
    void setUp() {
        log = new MemoryMessageRepository();
        comms = new CommunicationModule(log, "Animal Rescue", coordinator);
    }

    @Test
    void aDonationThanksTheDonorAlertsTheCoordinatorAndUpdatesTheDashboard() {
        Donation donation = DonationFactory.createDonation("Online", donor, 500, "Thanks");
        donation.setId(4);
        comms.getDonationSubject().publish(donation);

        assertEquals(1, log.countSent(ChannelType.EMAIL));   // thank-you to Sara
        assertEquals(1, log.countSent(ChannelType.SMS));     // alert to the coordinator
        assertTrue(log.findAll().get(0).getText().contains("EGP 500.00"));
        assertEquals(1, comms.getFeed().getEntries().size()); // dashboard line
    }

    @Test
    void anAvailableAnimalTellsFollowersAndPostsPublicly() {
        List<Recipient> followers = new ArrayList<>();
        followers.add(new Contact("Nour", "nour@example.com", null));
        comms.getAnimalSubject().publish(new AnimalStatusChanged(1, "Bondok", "IN_TREATMENT", "AVAILABLE", followers));

        assertEquals(1, log.countSent(ChannelType.EMAIL));
        assertEquals(1, log.countSent(ChannelType.SOCIAL_POST));
        assertEquals(1, comms.getFeed().getEntries().size());
    }

    @Test
    void otherAnimalChangesDoNotPostPublicly() {
        comms.getAnimalSubject().publish(new AnimalStatusChanged(1, "Bondok", "INTAKE", "IN_TREATMENT", null));
        assertEquals(0, log.countSent(ChannelType.SOCIAL_POST));
    }

    @Test
    void anEventUpdateReachesEveryParticipant() {
        List<Recipient> participants = new ArrayList<>();
        participants.add(new Contact("Youssef", null, "+201000000002"));
        participants.add(new Contact("Nour", "nour@example.com", null));
        comms.getEventSubject().publish(new EventUpdated(1, "Adoption Day", "Moved to Saturday", participants));

        assertEquals(1, log.countSent(ChannelType.SMS));     // Youssef only has a phone
        assertEquals(1, log.countSent(ChannelType.EMAIL));   // Nour only has an email
    }

    @Test
    void aDetachedObserverStopsReceiving() {
        com.rescue.service.Subject<Donation> donations = new com.rescue.service.Subject<>();
        ThankYouMailerObserver mailer = new ThankYouMailerObserver(comms.getNotifier());
        donations.attach(mailer);
        donations.detach(mailer);
        donations.publish(DonationFactory.createDonation("Online", donor, 100, "x"));
        assertTrue(log.findAll().isEmpty());
    }
}
