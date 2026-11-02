package com.rescue.view;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JFrame {
    private JButton animalBtn = new JButton("Animal Management (Admin/Volunteer)");
    private JButton adopterBtn = new JButton("Adopter Management (Admin/Volunteer)");
    private JButton donationBtn = new JButton("Donation Management (Donor/Admin)");
    private JButton volunteerBtn = new JButton("Volunteer Management (Admin/Volunteer)");
    private JButton eventBtn = new JButton("Event Management (Admin/Volunteer)");

    private JButton commsBtn = new JButton("Communication Center");

    private JButton userMgmtBtn = new JButton("User Management");

    public ControlPanelView() {
        setTitle("Animal Rescue - Role Control Panel");
        setSize(450, 470); // Slightly taller to fit the new buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(7, 1, 15, 15));

        add(animalBtn);
        add(adopterBtn);
        add(donationBtn);
        add(volunteerBtn);
        add(eventBtn);

        // 3. Add the button to the frame
        add(commsBtn);

        add(userMgmtBtn);

        setLocationRelativeTo(null);
    }

    public JButton getAnimalBtn() { return animalBtn; }
    public JButton getAdopterBtn() { return adopterBtn; }
    public JButton getDonationBtn() { return donationBtn; }
    public JButton getVolunteerBtn() { return volunteerBtn; }
    public JButton getEventBtn() { return eventBtn; }


    public JButton getCommsBtn() { return commsBtn; }

    public JButton getUserMgmtBtn() { return userMgmtBtn; }
}