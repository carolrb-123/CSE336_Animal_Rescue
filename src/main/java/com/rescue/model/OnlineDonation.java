package com.rescue.model;

public class OnlineDonation extends BaseDonation{

    int id;
    double amount;
    String receiptText;

    public OnlineDonation(Donor donor, double amount, String receiptText){

        super(donor, amount, receiptText);
    }



}
