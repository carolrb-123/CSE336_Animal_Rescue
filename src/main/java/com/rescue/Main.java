package com.rescue;

import com.rescue.model.*;
import com.rescue.factory.*;
import com.rescue.repo.*;
import com.rescue.view.*;
import com.rescue.controller.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            String[] options = {
                    "Animal Management (Maryam)",
                    "Adopter Management (Maryam)",
                    "Donation Management (Members B & C)"
            };

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Choose the management section:",
                    "Animal Rescue - Temporary Integration Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                AnimalRepository animalRepository = new AnimalRepository();
                AnimalView animalView = new AnimalView();
                new AnimalController(animalView, animalRepository);
                animalView.setVisible(true);

            } else if (choice == 1) {
                AdopterRepository adopterRepository = new AdopterRepository();
                AdopterView adopterView = new AdopterView();
                new AdopterController(adopterView, adopterRepository);
                adopterView.setVisible(true);

            } else if (choice == 2) {
                DonationRepository donationRepository = new DonationRepository();
                DonorRepository donorRepository = new DonorRepository();
                DonationView view = new DonationView();
                new DonationController(view, donationRepository, donorRepository);
                view.setVisible(true);
            }
        });
    }
}