
package com.rescue.controller;

import com.rescue.model.Donor;
import com.rescue.model.Donation;
import com.rescue.factory.DonationFactory;
import com.rescue.repo.DonationRepository;
import com.rescue.repo.DonorRepository;
import com.rescue.view.DonationView;

public class DonationController {

    private final DonationView view;
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;

    public DonationController(
            DonationView view,
            DonationRepository donationRepository,
            DonorRepository donorRepository) {

        this.view = view;
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;

        view.addSaveListener(e -> saveDonation());
    }

    private void saveDonation() {
        try {
            String name = view.getDonorName();
            String email = view.getDonorEmail();
            String phone = view.getDonorPhone();
            String type = view.getDonationType();
            String receipt = view.getReceiptText();

            if (view.isInMemoryOfSelected()
                    && view.getMemoryName().isEmpty()) {
                view.showMessage(
                        "Please enter the person's name for an in-memory donation.");
                return;
            }

            if (view.isRecurringSelected()) {
                receipt += " | Recurring donation";
            }

            if (view.isInMemoryOfSelected()) {
                receipt += " | In memory of: " + view.getMemoryName();
            }

            if (name.isEmpty() || email.isEmpty()
                    || phone.isEmpty() || receipt.isEmpty()
                    || view.getAmountText().isEmpty()) {
                view.showMessage("Please fill in all fields.");
                return;
            }

            if (!email.contains("@")) {
                view.showMessage("Please enter a valid email.");
                return;
            }

            double amount = Double.parseDouble(view.getAmountText());

            if (!Double.isFinite(amount) || amount <= 0) {
                view.showMessage("Amount must be a positive number.");
                return;
            }

            Donor donor = new Donor(0, name, email, phone);
            donorRepository.save(donor);

            Donation donation =
                    DonationFactory.createDonation(type, donor, amount, receipt);

            // Save the donation; its ID is assigned automatically.
            donationRepository.save(donation);

            view.displayDonation(
                    "Donor: " + donation.getDonor().getName()
                            + " | Donor ID: " + donation.getDonor().getId()
                            + " | Donation ID: " + donation.getId()
                            + " | Type: " + type
                            + " | Amount: " + donation.getAmount()
                            + " EGP"
                            + " | " + donation.getReceiptText()
            );

            view.showMessage("Donation saved successfully!");
            view.clearFields();

        } catch (NumberFormatException ex) {
            view.showMessage("Please enter a valid numeric amount.");
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }
}
