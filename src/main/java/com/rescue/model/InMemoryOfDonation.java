package com.rescue.model;

public class InMemoryOfDonation extends DonationDecorator{
    private final String personName;

    public InMemoryOfDonation(Donation donation, String personName){
        super(donation);
        this.personName=personName;
    }

    @Override
    public double getAmount(){
        return donation.getAmount();
    }

    @Override
    public String getReceiptText(){
        return donation.getReceiptText() + "\nThis donation is in memory of: " + personName;
    }
}
