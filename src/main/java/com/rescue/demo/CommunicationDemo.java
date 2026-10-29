package com.rescue.demo;

import com.rescue.factory.DonationFactory;
import com.rescue.model.Contact;
import com.rescue.model.Donation;
import com.rescue.model.Donor;
import com.rescue.repo.SentMessageRepository;
import com.rescue.service.CommunicationModule;
import com.rescue.service.Subject;
import com.rescue.service.observer.AnimalStatusChanged;
import com.rescue.service.observer.EventUpdated;
import com.rescue.service.strategy.ConsoleGateway;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

/**
 * Runs Part C on its own until A wires it into Main.
 * Uses B's real Donor, Donation and DonationFactory; the animal and event subjects stand in for E and D.
 * Run with --console for a text-only walkthrough.
 */
public class CommunicationDemo {

    public static void main(String[] args) {
        CommunicationModule comms = new CommunicationModule(new SentMessageRepository(), new ConsoleGateway(),
                "Animal Rescue", "EGP", new Contact("Mona Fathy (coordinator)", null, "+20 100 123 4567"));

        Subject<Donation> donations = new Subject<>();
        Subject<AnimalStatusChanged> animals = new Subject<>();
        Subject<EventUpdated> events = new Subject<>();
        comms.connectDonations(donations);
        comms.connectAnimals(animals);
        comms.connectEvents(events);

        Donor sara = new Donor(1, "Sara Adel", "sara.adel@example.com", "01001112223");
        Donation donation = DonationFactory.createDonation("Online", sara, 1500, "Thank you for supporting us");
        donation.setId(1);
        donations.publish(donation);

        Contact nour = new Contact("Nour Hany", "nour.hany@example.com", null);
        Contact youssef = new Contact("Youssef Samir", null, "+201234567890");
        animals.publish(new AnimalStatusChanged(7, "Bondok", "IN_TREATMENT", "AVAILABLE", List.of(nour)));
        events.publish(new EventUpdated(3, "Adoption Day at Al-Azhar Park", "Moved to Sat 7 Nov, 10:00",
                List.of(youssef)));

        if (GraphicsEnvironment.isHeadless() || Arrays.asList(args).contains("--console")) {
            System.out.println("===== Message log =====");
            comms.notifier().history().forEach(System.out::println);
            System.out.println("===== Activity feed =====");
            comms.feed().entries().forEach(System.out::println);
            return;
        }
        SwingUtilities.invokeLater(() -> comms.createController().getView().setVisible(true));
    }
}
