package com.rescue.service.observer;

import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

/**
 * OBSERVER pattern: ConcreteObserver on Subject&lt;AnimalStatusChanged&gt;.
 * Tells the people following the animal, and posts publicly when it becomes available or is adopted.
 */
public class AnimalStatusObserver implements Observer<AnimalStatusChanged> {

    private final Notifier notifier;

    public AnimalStatusObserver(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void update(AnimalStatusChanged change) {
        String name = change.animalName();
        if (!change.interested().isEmpty()) {
            notifier.broadcast(change.interested(), RecipientType.ADOPTER,
                    "Update on " + name + ": status changed from "
                            + AnimalStatusChanged.readable(change.oldStatus()) + " to "
                            + AnimalStatusChanged.readable(change.newStatus()) + ".");
        }
        if (change.isNow("AVAILABLE")) {
            notifier.postPublicly("Meet " + name + "! Ready for a forever home. "
                    + "Come and say hello at our next adoption day.");
        } else if (change.isNow("ADOPTED")) {
            notifier.postPublicly("Happy tails! " + name + " has been adopted. "
                    + "Thank you to everyone who fostered, donated and shared.");
        }
    }
}
