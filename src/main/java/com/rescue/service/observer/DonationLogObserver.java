package com.rescue.service.observer;

import com.rescue.model.ActivityFeed;
import com.rescue.model.Donation;
import com.rescue.service.Observer;

// OBSERVER pattern - observer 3 on donations: write a line on the dashboard.
public class DonationLogObserver implements Observer<Donation> {

    private final ActivityFeed feed;

    public DonationLogObserver(ActivityFeed feed) {
        this.feed = feed;
    }

    @Override
    public void update(Donation donation) {
        String amount = String.format("%.2f", donation.getAmount());
        feed.add("Donation #" + donation.getId() + " of EGP " + amount
                + " from " + donation.getDonor().getName());
    }
}
