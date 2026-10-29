
package com.rescue.model;

public abstract class BaseDonation implements Donation {

    private int id;
    private final Donor donor;
    private final double amount;
    private final String receiptText;

    public BaseDonation(
            Donor donor, double amount, String receiptText) {

        if (donor == null) {
            throw new IllegalArgumentException(
                    "A donation must have a donor.");
        }

        this.donor = donor;
        this.amount = amount;
        this.receiptText = receiptText;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Donor getDonor() {
        return donor;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getReceiptText() {
        return receiptText;
    }
}
