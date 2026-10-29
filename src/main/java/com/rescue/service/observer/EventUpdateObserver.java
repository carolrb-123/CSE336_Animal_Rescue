package com.rescue.service.observer;

import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

/** OBSERVER pattern: ConcreteObserver on Subject&lt;EventUpdated&gt;. Tells everyone registered. */
public class EventUpdateObserver implements Observer<EventUpdated> {

    private final Notifier notifier;

    public EventUpdateObserver(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void update(EventUpdated event) {
        if (event.participants().isEmpty()) {
            return;
        }
        notifier.broadcast(event.participants(), RecipientType.VOLUNTEER,
                event.title() + ": " + event.change() + ". Reply if you can no longer come.");
    }
}
