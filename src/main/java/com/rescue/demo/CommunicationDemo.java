package com.rescue.demo;

import com.rescue.factory.DonationFactory;
import com.rescue.model.Contact;
import com.rescue.model.Donation;
import com.rescue.model.Donor;
import com.rescue.model.Recipient;
import com.rescue.service.CommunicationModule;
import com.rescue.service.observer.AnimalStatusChanged;
import com.rescue.service.observer.EventUpdated;

import java.util.ArrayList;
import java.util.List;

// Runs the Communication part on its own with sample data, then opens the screen.
public class CommunicationDemo {

    public static void main(String[] args) {
        CommunicationModule comms = CommunicationModule.getInstance();

        // 1) A donation is saved -> 3 observers react.
        Donor sara = new Donor(1, "Sara Adel", "sara.adel@example.com", "01001112223");
        Donation donation = DonationFactory.createDonation("Online", sara, 1500, "Thank you for supporting us");
        donation.setId(1);
        comms.getDonationSubject().publish(donation);

        // 2) An animal becomes available -> its follower is told + public post.
        List<Recipient> followers = new ArrayList<>();
        followers.add(new Contact("Nour Hany", "nour.hany@example.com", null));
        comms.getAnimalSubject().publish(
                new AnimalStatusChanged(7, "Bondok", "IN_TREATMENT", "AVAILABLE", followers));

        // 3) An event is moved -> its volunteers are told.
        List<Recipient> volunteers = new ArrayList<>();
        volunteers.add(new Contact("Youssef Samir", null, "+201234567890"));
        comms.getEventSubject().publish(
                new EventUpdated(3, "Adoption Day", "Moved to Sat 7 Nov, 10:00", volunteers));

        comms.createController().getView().setVisible(true);
    }
}
