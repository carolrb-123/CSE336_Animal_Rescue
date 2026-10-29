package com.rescue.service.observer;

import com.rescue.model.ActivityFeed;
import com.rescue.service.Observer;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * OBSERVER pattern: ConcreteObserver. Writes each event to the dashboard's activity feed.
 * Generic, so the same class watches donations, animals and events.
 */
public class DashboardObserver<E> implements Observer<E> {

    private final ActivityFeed feed;
    private final Function<E, String> summary;

    public DashboardObserver(ActivityFeed feed, Function<E, String> summary) {
        this.feed = feed;
        this.summary = summary;
    }

    @Override
    public void update(E event) {
        feed.add(LocalDateTime.now(), summary.apply(event));
    }
}
