package com.rescue.service.observer;

import com.rescue.model.ActivityFeed;
import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

// OBSERVER pattern - observer on animals: when an animal's status changes,
// tell the people following it, post publicly if it's ready or adopted,
// and write a line on the dashboard.
public class AnimalStatusObserver implements Observer<AnimalStatusChanged> {

    private final Notifier notifier;
    private final ActivityFeed feed;

    public AnimalStatusObserver(Notifier notifier, ActivityFeed feed) {
        this.notifier = notifier;
        this.feed = feed;
    }

    @Override
    public void update(AnimalStatusChanged change) {
        String name = change.getAnimalName();

        // 1) Tell the people who follow this animal.
        if (!change.getInterested().isEmpty()) {
            String message = "Update on " + name + ": status changed from "
                    + change.getOldStatus() + " to " + change.getNewStatus() + ".";
            notifier.broadcast(change.getInterested(), RecipientType.ADOPTER, message);
        }

        // 2) Post on the public page for good news.
        if (change.getNewStatus().equalsIgnoreCase("AVAILABLE")) {
            notifier.postPublicly("Meet " + name + "! Ready for a forever home. "
                    + "Come and say hello at our next adoption day.");
        } else if (change.getNewStatus().equalsIgnoreCase("ADOPTED")) {
            notifier.postPublicly("Happy tails! " + name + " has been adopted. "
                    + "Thank you to everyone who helped.");
        }

        // 3) Write it on the dashboard.
        feed.add(change.getSummary());
    }
}
