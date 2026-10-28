package com.rescue.service;
import java.util.ArrayList;
import java.util.List;
public class Subject<E> {
    private final List<Observer<E>> observers = new ArrayList<>();
    public void attach(Observer<E> o) { observers.add(o); }
    public void detach(Observer<E> o) { observers.remove(o); }
    public void publish(E event) { observers.forEach(o -> o.update(event)); }
}
