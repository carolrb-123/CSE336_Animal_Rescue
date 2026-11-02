package com.rescue.model;

import java.util.List;

public class Volunteer implements Entity {

    private int id;
    private final String name;
    private final String email;
    private final String phone;
    private final List<String> skills;
    private final String availability;
    private final String role;
    private final double hours;
    private final List<String> certificates;

    public Volunteer(int id, String name, String email, String phone, List<String> skills,
                     String availability, String role, double hours, List<String> certificates) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.skills = List.copyOf(skills);
        this.availability = availability;
        this.role = role;
        this.hours = hours;
        this.certificates = List.copyOf(certificates);
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getAvailability() {
        return availability;
    }

    public String getRole() {
        return role;
    }

    public double getHours() {
        return hours;
    }

    public List<String> getCertificates() {
        return certificates;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
