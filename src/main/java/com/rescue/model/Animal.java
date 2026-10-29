package com.rescue.model;

public class Animal implements Entity {

    private int id;
    private String name;
    private String species;
    private int age;
    private String healthStatus;
    private boolean availableForAdoption;

    public Animal(int id, String name, String species, int age,
                 String healthStatus, boolean availableForAdoption) {

        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.healthStatus = healthStatus;
        this.availableForAdoption = availableForAdoption;
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

    public int getAge() {
        return age;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public boolean isAvailableForAdoption() {
        return availableForAdoption;
    }

    public void setAvailableForAdoption(boolean availableForAdoption) {
        this.availableForAdoption = availableForAdoption;
    }
}