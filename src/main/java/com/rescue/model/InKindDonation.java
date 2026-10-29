package com.rescue.model;

public class InKindDonation extends BaseDonation{

    int id;
    double amount;
    String receiptText;

    public InKindDonation(Donor donor, double amount, String receiptText){

        super(donor, amount, receiptText);
    }

}
