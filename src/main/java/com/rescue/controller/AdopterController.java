package com.rescue.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import com.rescue.model.Adopter;
import com.rescue.repo.AdopterRepository;
import com.rescue.repo.RepositoryEvent;
import com.rescue.service.EmailNotificationStrategy;
import com.rescue.service.NotificationStrategy;
import com.rescue.service.Observer;
import com.rescue.view.AdopterView;

public class AdopterController {

    private final AdopterView view;
    private final AdopterRepository repository;
        private final NotificationStrategy notificationStrategy;
        private final Observer<RepositoryEvent<Adopter>> repositoryObserver = event -> {
                if (SwingUtilities.isEventDispatchThread()) {
                        refreshTable();
                } else {
                        SwingUtilities.invokeLater(this::refreshTable);
                }
        };

    public AdopterController(
            AdopterView view,
            AdopterRepository repository) {
                this(view, repository, new EmailNotificationStrategy());
        }

        public AdopterController(
                        AdopterView view,
                        AdopterRepository repository,
                        NotificationStrategy notificationStrategy) {

        this.view = view;
        this.repository = repository;
                this.notificationStrategy = notificationStrategy;
                repository.addObserver(repositoryObserver);
                view.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent event) {
                                repository.removeObserver(repositoryObserver);
                        }
                });

        this.view.getBackBtn().addActionListener(e -> this.view.dispose());
        view.getAddButton().addActionListener(
                e -> addAdopter()
        );

        view.getUpdateButton().addActionListener(
                e -> updateAdopter()
        );

        view.getDeleteButton().addActionListener(
                e -> deleteAdopter()
        );

        view.getClearButton().addActionListener(
                e -> clearFields()
        );

        view.getAdopterTable()
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedAdopter();
                    }
                });

        refreshTable();
    }

    private void addAdopter() {

        String name = view.getNameField().getText().trim();
        String email = view.getEmailField().getText().trim();
        String phone = view.getPhoneField().getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            view.showMessage("Please fill in all fields.");
            return;
        }

        if (!email.contains("@")) {
            view.showMessage("Please enter a valid email.");
            return;
        }

        Adopter adopter = new Adopter(
                repository.nextId(),
                name,
                email,
                phone
        );

        repository.save(adopter);
        notificationStrategy.send(adopter, "Your adopter profile was added successfully.");
        clearFields();
        view.showMessage("Adopter added successfully!");
    }

    private void updateAdopter() {

        try {

            int id = Integer.parseInt(
                    view.getIdField().getText().trim()
            );

            if (repository.findById(id).isEmpty()) {

                view.showMessage(
                        "Adopter not found."
                );

                return;
            }

            String name =
                    view.getNameField().getText().trim();

            String email =
                    view.getEmailField().getText().trim();

            String phone =
                    view.getPhoneField().getText().trim();

            if (id <= 0) {

                view.showMessage(
                        "Adopter ID must be positive."
                );

                return;
            }

            if (name.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()) {

                view.showMessage(
                        "Please fill in all fields."
                );

                return;
            }

            if (!email.contains("@")) {

                view.showMessage(
                        "Please enter a valid email."
                );

                return;
            }

            Adopter updatedAdopter = new Adopter(
                    id,
                    name,
                    email,
                    phone
            );

            repository.save(updatedAdopter);

            notificationStrategy.send(updatedAdopter, "Your adopter profile was updated successfully.");
            clearFields();

            view.showMessage(
                    "Adopter updated successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Please enter a valid ID."
            );
        }
    }

    private void deleteAdopter() {

        try {

            int id = Integer.parseInt(
                    view.getIdField().getText().trim()
            );

            if (repository.findById(id).isEmpty()) {

                view.showMessage(
                        "Adopter not found."
                );

                return;
            }

            repository.delete(id);

            clearFields();

            view.showMessage(
                    "Adopter deleted successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Please select an adopter or enter a valid ID."
            );
        }
    }

    private void loadSelectedAdopter() {

        int selectedRow =
                view.getAdopterTable().getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow =
                view.getAdopterTable()
                        .convertRowIndexToModel(selectedRow);

        int id = Integer.parseInt(
                view.getTableModel()
                        .getValueAt(modelRow, 0)
                        .toString()
        );

        repository.findById(id).ifPresent(adopter -> {

            view.getIdField().setText(
                    String.valueOf(adopter.getId())
            );

            view.getNameField().setText(
                    adopter.getName()
            );

            view.getEmailField().setText(
                    adopter.getEmail()
            );

            view.getPhoneField().setText(
                    adopter.getPhone()
            );
        });
    }

    private void refreshTable() {

        view.getTableModel().setRowCount(0);

        List<Adopter> adopters =
                repository.findAll();

        for (Adopter adopter : adopters) {

            view.getTableModel().addRow(
                    new Object[]{
                            adopter.getId(),
                            adopter.getName(),
                            adopter.getEmail(),
                            adopter.getPhone()
                    }
            );
        }
    }

    private void clearFields() {

        view.getIdField().setText("Assigned automatically");
        view.getNameField().setText("");
        view.getEmailField().setText("");
        view.getPhoneField().setText("");

        view.getAdopterTable().clearSelection();
    }
}
