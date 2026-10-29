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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

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

                AnimalRepository animalRepository =
                        new AnimalRepository();

                AnimalView animalView =
                        new AnimalView();

                new AnimalController(
                        animalView,
                        animalRepository
                );

                animalView.setVisible(true);

            } else if (choice == 1) {

                AdopterRepository adopterRepository =
                        new AdopterRepository();

                AdopterView adopterView =
                        new AdopterView();

                new AdopterController(
                        adopterView,
                        adopterRepository
                );

                adopterView.setVisible(true);
            }
        });
    }
}