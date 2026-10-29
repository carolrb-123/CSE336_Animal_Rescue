package com.rescue.service.observer;

import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.service.Observer;
import com.rescue.service.strategy.Notifier;

import java.util.function.Function;

/**
 * OBSERVER pattern: ConcreteObserver. Sends the volunteer coordinator a short alert.
 * Generic, so the same class watches donations, animals and events.
 */
public class CoordinatorObserver<E> implements Observer<E> {

    private final Notifier notifier;
    private final Recipient coordinator;
    private final Function<E, String> summary;

    public CoordinatorObserver(Notifier notifier, Recipient coordinator, Function<E, String> summary) {
        this.notifier = notifier;
        this.coordinator = coordinator;
        this.summary = summary;
    }

    @Override
    public void update(E event) {
        notifier.send(coordinator, RecipientType.COORDINATOR, "Alert - " + summary.apply(event));
    }
}
