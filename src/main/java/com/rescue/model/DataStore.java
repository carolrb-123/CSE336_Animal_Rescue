package com.rescue.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataStore {
    private static DataStore instance = new DataStore();


    private Map<Integer, Animal> animals = new LinkedHashMap<>();
    private Map<Integer, Adopter> adopters = new LinkedHashMap<>();
    private Map<Integer, Donation> donations = new LinkedHashMap<>();
    private Map<Integer, Donor> donors = new LinkedHashMap<>();
    private Map<Integer, SentMessage> sentMessages = new LinkedHashMap<>();
    private Map<Integer, Volunteer> volunteers = new LinkedHashMap<>();
    private Map<Integer, RescueEvent> events = new LinkedHashMap<>();
    private Map<Integer, EventRegistration> eventRegistrations = new LinkedHashMap<>();
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int nextVolunteerId = 1;
    private int nextEventId = 1;

    private DataStore() {
        users.put(1, new User(1, "admin", "admin123", "Admin"));
        users.put(2, new User(2, "volunteer1", "pass123", "Volunteer"));
        users.put(3, new User(3, "donor_sarah", "pass123", "Donor"));
    }

    public static DataStore getInstance() {
        return instance;
    }

    public Map<Integer, Animal> getAnimals() { return animals; }
    public Map<Integer, Adopter> getAdopters() { return adopters; }
    public Map<Integer, Donation> getDonations() { return donations; }
    public Map<Integer, Donor> getDonors() { return donors; }
    public Map<Integer, SentMessage> getSentMessages() { return sentMessages; }
    public Map<Integer, Volunteer> getVolunteers() { return volunteers; }
    public Map<Integer, RescueEvent> getEvents() { return events; }
    public Map<Integer, EventRegistration> getEventRegistrations() { return eventRegistrations; }
    public Map<Integer, User> getUsers() { return users; }

    public synchronized int allocateVolunteerId() {
        nextVolunteerId = Math.max(nextVolunteerId, nextIdAfter(volunteers));
        return nextVolunteerId++;
    }

    public synchronized void reserveVolunteerId(int id) {
        nextVolunteerId = Math.max(nextVolunteerId, id + 1);
    }

    public synchronized int allocateEventId() {
        nextEventId = Math.max(nextEventId, nextIdAfter(events));
        return nextEventId++;
    }

    public synchronized void reserveEventId(int id) {
        nextEventId = Math.max(nextEventId, id + 1);
    }

    private static int nextIdAfter(Map<Integer, ?> items) {
        return items.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }
}