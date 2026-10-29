package com.rescue.model;

public abstract class DonationDecorator implements Donation{

    protected final Donation donation;

    public DonationDecorator(Donation donation){
        this.donation=donation;
    }

    @Override
    public void setId(int id){
        donation.setId(id);
    }

    @Override
    public int getId() {
        return donation.getId();
    }

    @Override
    public Donor getDonor() {
        return donation.getDonor();
    }

    @Override
    public double getAmount(){
        return donation.getAmount();
    }

    @Override
    public String getReceiptText(){
        return donation.getReceiptText();
    }

}
