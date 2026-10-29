package com.rescue.controller;

import com.rescue.view.*;
import com.rescue.repo.*;

public class ControlPanelController {
    private ControlPanelView view;

    public ControlPanelController(ControlPanelView view) {
        this.view = view;
        initController();
    }

    private void initController() {

        view.getAnimalBtn().addActionListener(e -> openAnimalModule());
        view.getAdopterBtn().addActionListener(e -> openAdopterModule());
        view.getDonationBtn().addActionListener(e -> openDonationModule());
    }

    private void openAnimalModule() {
        AnimalView animalView = new AnimalView();
        new AnimalController(animalView, new AnimalRepository());
        animalView.setVisible(true);
    }

    private void openAdopterModule() {
        AdopterView adopterView = new AdopterView();
        new AdopterController(adopterView, new AdopterRepository());
        adopterView.setVisible(true);
    }

    private void openDonationModule() {
        DonationView donationView = new DonationView();
        new DonationController(donationView, new DonationRepository(), new DonorRepository());
        donationView.setVisible(true);
    }
}