package com.rescue.controller;

import java.util.List;

import com.rescue.model.Adopter;
import com.rescue.repo.AdopterRepository;
import com.rescue.view.AdopterView;

public class AdopterController {

    private final AdopterView view;
    private final AdopterRepository repository;

    public AdopterController(
            AdopterView view,
            AdopterRepository repository) {

        this.view = view;
        this.repository = repository;

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

        try {

            int id = Integer.parseInt(
                    view.getIdField().getText().trim()
            );

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

            if (repository.findById(id).isPresent()) {

                view.showMessage(
                        "Adopter ID already exists."
                );

                return;
            }

            Adopter adopter = new Adopter(
                    id,
                    name,
                    email,
                    phone
            );

            repository.save(adopter);

            refreshTable();
            clearFields();

            view.showMessage(
                    "Adopter added successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Please enter a valid ID."
            );
        }
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

            refreshTable();
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

            refreshTable();
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

        view.getIdField().setText("");
        view.getNameField().setText("");
        view.getEmailField().setText("");
        view.getPhoneField().setText("");

        view.getAdopterTable().clearSelection();
    }
}
