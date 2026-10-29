package com.rescue.service.observer;

import com.rescue.factory.DonationFactory;
import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Donation;
import com.rescue.model.Donor;
import com.rescue.model.RecurringDonation;
import com.rescue.repo.SentMessageRepository;
import com.rescue.service.CommunicationModule;
import com.rescue.service.RecordingGateway;
import com.rescue.service.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    private final Contact coordinator = new Contact("Mona", null, "+201000000009");
    private final Donor donor = new Donor(1, "Sara", "sara@example.com", "01000000001");
    private final Contact adopter = new Contact("Nour", "nour@example.com", null);
    private final Contact volunteer = new Contact("Youssef", null, "+201000000002");

    private RecordingGateway gateway;
    private CommunicationModule comms;

    @BeforeEach
    void setUp() {
        gateway = new RecordingGateway();
        comms = new CommunicationModule(new SentMessageRepository(), gateway, "Animal Rescue", "EGP", coordinator);
    }

    @Test
    void donationThanksDonorAlertsCoordinatorAndUpdatesDashboard() {
        Subject<Donation> donations = new Subject<>();
        comms.connectDonations(donations);
        Donation d = DonationFactory.createDonation("Online", donor, 500, "Thanks");
        d.setId(4);
        donations.publish(d);

        assertEquals(1, gateway.count(ChannelType.EMAIL));
        assertEquals(1, gateway.count(ChannelType.SMS));
        assertTrue(gateway.sent.stream().anyMatch(s -> s.text().contains("EGP 500.00")));
        assertEquals(1, comms.feed().entries().size());
    }

    @Test
    void worksWithDecoratedDonations() {
        Subject<Donation> donations = new Subject<>();
        comms.connectDonations(donations);
        donations.publish(new RecurringDonation(DonationFactory.createDonation("Cheque", donor, 200, "r")));
        assertEquals(1, gateway.count(ChannelType.EMAIL));
    }

    @Test
    void animalAvailableTellsFollowersAndPostsPublicly() {
        Subject<AnimalStatusChanged> animals = new Subject<>();
        comms.connectAnimals(animals);
        animals.publish(new AnimalStatusChanged(1, "Bondok", "IN_TREATMENT", "AVAILABLE", List.of(adopter)));
        assertEquals(1, gateway.count(ChannelType.EMAIL));
        assertEquals(1, gateway.count(ChannelType.SOCIAL_POST));
    }

    @Test
    void otherStatusChangesDoNotPostPublicly() {
        Subject<AnimalStatusChanged> animals = new Subject<>();
        comms.connectAnimals(animals);
        animals.publish(new AnimalStatusChanged(1, "Bondok", "INTAKE", "IN_TREATMENT", List.of()));
        assertEquals(0, gateway.count(ChannelType.SOCIAL_POST));
    }

    @Test
    void eventUpdateReachesEveryParticipant() {
        Subject<EventUpdated> events = new Subject<>();
        comms.connectEvents(events);
        events.publish(new EventUpdated(1, "Adoption Day", "Moved to Saturday", List.of(volunteer, adopter)));
        assertEquals(1, gateway.count(ChannelType.EMAIL));   // adopter has only email
        assertEquals(2, gateway.count(ChannelType.SMS));     // volunteer + coordinator
    }

    @Test
    void detachedObserverStopsReceiving() {
        Subject<Donation> donations = new Subject<>();
        ThankYouMailerObserver mailer = new ThankYouMailerObserver(comms.notifier(), "EGP");
        donations.attach(mailer);
        donations.detach(mailer);
        donations.publish(DonationFactory.createDonation("Online", donor, 100, "x"));
        assertTrue(gateway.sent.isEmpty());
    }
}
