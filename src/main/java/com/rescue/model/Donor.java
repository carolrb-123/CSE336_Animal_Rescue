package com.rescue.model;

public class Donor implements Entity, Recipient{

    private int id;
    private String name;
    private String email;
    private String phone;

    public Donor(int id, String name, String email, String phone){
        this.id=id;
        this.name=name;
        this.email=email;
        this.phone=phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public String getEmail(){
        return email;
    }

    @Override
    public String getPhone(){
        return phone;
    }
}

