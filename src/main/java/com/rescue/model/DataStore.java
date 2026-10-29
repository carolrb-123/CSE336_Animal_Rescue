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

    private DataStore() {}

    public static DataStore getInstance() {
        return instance;
    }

    public Map<Integer, Animal> getAnimals() { return animals; }
    public Map<Integer, Adopter> getAdopters() { return adopters; }
    public Map<Integer, Donation> getDonations() { return donations; }
    public Map<Integer, Donor> getDonors() { return donors; }
    public Map<Integer, SentMessage> getSentMessages() { return sentMessages; }
}