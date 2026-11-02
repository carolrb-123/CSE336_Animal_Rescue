package com.rescue.controller;

import com.rescue.service.CommunicationModule;
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
        view.getVolunteerBtn().addActionListener(e -> openVolunteerModule());
        view.getEventBtn().addActionListener(e -> openEventModule());
        view.getUserMgmtBtn().addActionListener(e -> openUserModule());
        view.getCommsBtn().addActionListener(e -> openCommunicationModule());

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

    private void openVolunteerModule() {
        VolunteerView volunteerView = new VolunteerView();
        new VolunteerController(volunteerView, new VolunteerRepository(), new EventRegistrationRepository());
        volunteerView.setVisible(true);
    }

    private void openEventModule() {
        EventView eventView = new EventView();
        new EventController(
                eventView,
                new RescueEventRepository(),
                new VolunteerRepository(),
                new EventRegistrationRepository());
        eventView.setVisible(true);
    }

    private void openUserModule() {
        UserManagementView userView = new UserManagementView();
        new UserManagementController(userView, new UserRepository());
        userView.setVisible(true);
    }

    private void openCommunicationModule() {
        CommunicationModule module = new CommunicationModule();
        CommunicationController commsController = module.createController();
        commsController.getView().setVisible(true);
    }
}
