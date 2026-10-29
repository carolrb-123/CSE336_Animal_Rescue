package com.rescue.view;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JFrame {
    private JButton animalBtn = new JButton("Animal Management (Admin/Volunteer)");
    private JButton adopterBtn = new JButton("Adopter Management (Admin/Volunteer)");
    private JButton donationBtn = new JButton("Donation Management (Donor/Admin)");

    public ControlPanelView() {
        setTitle("Animal Rescue - Role Control Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setLayout(new GridLayout(3, 1, 15, 15));

        add(animalBtn);
        add(adopterBtn);
        add(donationBtn);

        setLocationRelativeTo(null);
    }


    public JButton getAnimalBtn() { return animalBtn; }
    public JButton getAdopterBtn() { return adopterBtn; }
    public JButton getDonationBtn() { return donationBtn; }
}