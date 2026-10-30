package com.rescue.view;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JFrame {
    private JButton animalBtn = new JButton("Animal Management (Admin/Volunteer)");
    private JButton adopterBtn = new JButton("Adopter Management (Admin/Volunteer)");
    private JButton donationBtn = new JButton("Donation Management (Donor/Admin)");
    private JButton volunteerBtn = new JButton("Volunteer Management (Admin/Volunteer)");
    private JButton eventBtn = new JButton("Event Management (Admin/Volunteer)");

    public ControlPanelView() {
        setTitle("Animal Rescue - Role Control Panel");
        setSize(450, 410);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setLayout(new GridLayout(5, 1, 15, 15));

        add(animalBtn);
        add(adopterBtn);
        add(donationBtn);
        add(volunteerBtn);
        add(eventBtn);

        setLocationRelativeTo(null);
    }


    public JButton getAnimalBtn() { return animalBtn; }
    public JButton getAdopterBtn() { return adopterBtn; }
    public JButton getDonationBtn() { return donationBtn; }
    public JButton getVolunteerBtn() { return volunteerBtn; }
    public JButton getEventBtn() { return eventBtn; }
}