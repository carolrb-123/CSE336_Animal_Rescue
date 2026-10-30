package com.rescue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.rescue.controller.AdopterController;
import com.rescue.controller.AnimalController;
import com.rescue.repo.AdopterRepository;
import com.rescue.repo.AnimalRepository;
import com.rescue.view.AdopterView;
import com.rescue.view.AnimalView;

public class Main {

    @SuppressWarnings("unused")
    private static AnimalController animalController;

    @SuppressWarnings("unused")
    private static AdopterController adopterController;

        private static final AnimalRepository animalRepository =
            new AnimalRepository();

        private static final AdopterRepository adopterRepository =
            new AdopterRepository();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            showManagementMenu();
        });
    }

    private static void showManagementMenu() {

        String[] options = {
                "Animal Management",
                "Adopter Management"
        };

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose the management section:",
                "Animal Rescue",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {

            AnimalView animalView =
                    new AnimalView();

                animalController = new AnimalController(
                    animalView,
                    animalRepository
            );

            animalView.getBackButton().addActionListener(e -> {
                animalView.dispose();
                showManagementMenu();
            });

            animalView.setVisible(true);

        } else if (choice == 1) {

            AdopterView adopterView =
                    new AdopterView();

                adopterController = new AdopterController(
                    adopterView,
                    adopterRepository
            );

            adopterView.getBackButton().addActionListener(e -> {
                adopterView.dispose();
                showManagementMenu();
            });

            adopterView.setVisible(true);
        }
    }
}