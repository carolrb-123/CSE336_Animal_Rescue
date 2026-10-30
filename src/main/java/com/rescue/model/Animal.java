package com.rescue.model;

public class Animal implements Entity {

    private final int id;
    private final String name;
    private final String species;
    private final int ageYears;
    private final int ageMonths;
    private final String healthStatus;
    private boolean availableForAdoption;
    private final String description;

    public Animal(int id, String name, String species,
                  int ageYears, int ageMonths, String healthStatus,
                  boolean availableForAdoption, String description) {

        this.id = id;
        this.name = name;
        this.species = species;
        this.ageYears = ageYears;
        this.ageMonths = ageMonths;
        this.healthStatus = healthStatus;
        this.availableForAdoption = availableForAdoption;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getAgeYears() {
        return ageYears;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public boolean isAvailableForAdoption() {
        return availableForAdoption;
    }

    public String getDescription() {
        return description;
    }

    public void setAvailableForAdoption(boolean availableForAdoption) {
        this.availableForAdoption = availableForAdoption;
    }
}