package com.rescue.model;

public class RecurringDonation extends DonationDecorator{
    public RecurringDonation(Donation donation){
        super(donation);
    }

    @Override
    public double getAmount(){
        return donation.getAmount();
    }

    @Override
    public String getReceiptText(){
        return donation.getReceiptText() + "\nThis is a recurring donation";
    }
}
