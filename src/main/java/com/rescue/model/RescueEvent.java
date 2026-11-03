package com.rescue.model;

import java.time.LocalDate;
import java.util.List;

public class RescueEvent implements Entity {

    private int id;
    private final String name;
    private final String type;
    private final LocalDate date;
    private final String location;
    private final List<String> requiredSkills;
    private final String description;
    private final double hours;

    public RescueEvent(int id, String name, String type, LocalDate date, String location,
                       List<String> requiredSkills, String description, double hours) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.location = location;
        this.requiredSkills = List.copyOf(requiredSkills);
        this.description = description;
        this.hours = hours;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public String getDescription() {
        return description;
    }

    public double getHours() {
        return hours;
    }

    @Override
    public String toString() {
        return id + " - " + name + " (" + date + ")";
    }
}
