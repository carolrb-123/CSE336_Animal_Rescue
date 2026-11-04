package com.rescue.service.observer;

import com.rescue.model.Donation;
import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

// OBSERVER pattern - observer 2 on donations: tell the volunteer coordinator.
public class CoordinatorAlertObserver implements Observer<Donation> {

    private final Notifier notifier;
    private final Recipient coordinator;

    public CoordinatorAlertObserver(Notifier notifier, Recipient coordinator) {
        this.notifier = notifier;
        this.coordinator = coordinator;
    }

    @Override
    public void update(Donation donation) {
        String amount = String.format("%.2f", donation.getAmount());
        String message = "New donation of EGP " + amount + " from " + donation.getDonor().getName() + ".";
        notifier.send(coordinator, RecipientType.COORDINATOR, message);
    }
}
