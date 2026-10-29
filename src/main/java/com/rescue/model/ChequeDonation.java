package com.rescue.model;

public class ChequeDonation extends BaseDonation{

    int id;
    double amount;
    String receiptText;

    public ChequeDonation(Donor donor, double amount, String receiptText){

        super(donor, amount, receiptText);
    }


}
