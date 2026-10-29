package com.rescue.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** The dashboard's "what just happened" list, newest first. Filled by DashboardObserver. */
public class ActivityFeed {

    private static final int MAX_ENTRIES = 200;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("dd MMM HH:mm");

    private final Deque<String> entries = new ArrayDeque<>();
    private final List<Runnable> changeListeners = new CopyOnWriteArrayList<>();

    public void add(LocalDateTime at, String text) {
        synchronized (entries) {
            entries.addFirst(TIME.format(at) + "   " + text);
            if (entries.size() > MAX_ENTRIES) {
                entries.removeLast();
            }
        }
        changeListeners.forEach(Runnable::run);
    }

    public List<String> entries() {
        synchronized (entries) {
            return List.copyOf(entries);
        }
    }

    /** Lets a controller refresh its view when the feed changes. */
    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }
}
