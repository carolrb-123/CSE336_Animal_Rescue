package com.rescue;
import com.rescue.model.*;
import com.rescue.factory.*;
import com.rescue.repo.*;
import javax.swing.SwingUtilities;
import com.rescue.view.*;
import com.rescue.controller.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            DonationRepository donationRepository =
                    new DonationRepository();

            DonorRepository donorRepository =
                    new DonorRepository();

            DonationView view = new DonationView();

            new DonationController(
                    view,
                    donationRepository,
                    donorRepository
            );

            view.setVisible(true);
        });
    }
}

