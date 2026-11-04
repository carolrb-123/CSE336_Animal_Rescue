package com.rescue.service.observer;

import com.rescue.model.ActivityFeed;
import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

// OBSERVER pattern - observer on events: when an event changes,
// tell everyone registered and write a line on the dashboard.
public class EventUpdateObserver implements Observer<EventUpdated> {

    private final Notifier notifier;
    private final ActivityFeed feed;

    public EventUpdateObserver(Notifier notifier, ActivityFeed feed) {
        this.notifier = notifier;
        this.feed = feed;
    }

    @Override
    public void update(EventUpdated event) {
        if (!event.getParticipants().isEmpty()) {
            String message = event.getTitle() + ": " + event.getChange()
                    + ". Reply if you can no longer come.";
            notifier.broadcast(event.getParticipants(), RecipientType.VOLUNTEER, message);
        }
        feed.add(event.getSummary());
    }
}
