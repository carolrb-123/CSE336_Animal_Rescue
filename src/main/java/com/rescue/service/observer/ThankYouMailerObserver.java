package com.rescue.service.observer;

import com.rescue.model.Donation;
import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

import java.util.Locale;

/**
 * OBSERVER pattern: ConcreteObserver on Subject&lt;Donation&gt;.
 * Thanks every donor as soon as their donation is saved. Works with decorated donations
 * too, since it only uses the Donation interface.
 */
public class ThankYouMailerObserver implements Observer<Donation> {

    private final Notifier notifier;
    private final String currency;

    public ThankYouMailerObserver(Notifier notifier, String currency) {
        this.notifier = notifier;
        this.currency = currency;
    }

    @Override
    public void update(Donation donation) {
        String message = String.format(Locale.US,
                "Thank you for your donation of %s %,.2f. It goes straight to food, vet care and shelter "
                        + "for animals waiting for a home. Your donation number is %d.",
                currency, donation.getAmount(), donation.getId());
        notifier.send(donation.getDonor(), RecipientType.DONOR, message);
    }
}
