package com.rescue.model;

public interface Donation extends Entity{

    void setId(int id);
    double getAmount();
    String getReceiptText();
    Donor getDonor();
}
