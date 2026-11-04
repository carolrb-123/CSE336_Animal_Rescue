package com.rescue.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Subject<E> {
    private final List<Observer<E>> observers = new ArrayList<>();

    public void attach(Observer<E> observer) {
        Objects.requireNonNull(observer, "observer");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(Observer<E> observer) {
        observers.remove(observer);
    }

    public void publish(E event) {
        List.copyOf(observers).forEach(observer -> observer.update(event));
    }
}
