package com.rescue.service.observer;

import com.rescue.model.Donation;
import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

// OBSERVER pattern - observer 1 on donations: thank the donor.
public class ThankYouMailerObserver implements Observer<Donation> {

    private final Notifier notifier;

    public ThankYouMailerObserver(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void update(Donation donation) {
        String amount = String.format("%.2f", donation.getAmount());
        String message = "Thank you for your donation of EGP " + amount + ". "
                + "It goes straight to food, vet care and shelter for animals waiting for a home. "
                + "Your donation number is " + donation.getId() + ".";
        notifier.send(donation.getDonor(), RecipientType.DONOR, message);
    }
}
