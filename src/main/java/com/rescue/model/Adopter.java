package com.rescue.model;

public class Adopter implements Entity, Recipient {

    private final int id;
    private final String name;
    private final String email;
    private final String phone;

    public Adopter(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhone() {
        return phone;
    }
}
