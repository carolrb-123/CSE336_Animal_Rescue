package com.rescue.factory;
import com.rescue.model.*;

public class DonationFactory {

    public static Donation createDonation(String type, Donor donor, double amount, String receiptText){
        if (type.equals("Online")){
            return new OnlineDonation(donor, amount, receiptText);
        } else if (type.equals("Cheque")){
            return new ChequeDonation(donor, amount, receiptText);
        } else if (type.equals("In-kind")){
            return new InKindDonation(donor, amount, receiptText);
        }
        else {
            throw new IllegalArgumentException(
                    "Unknown donation type: " + type);
        }
    }
}
