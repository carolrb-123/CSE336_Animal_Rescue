package com.rescue.controller;

import java.util.List;

import com.rescue.model.Animal;
import com.rescue.repo.AnimalRepository;
import com.rescue.view.AnimalView;

public class AnimalController {

    private final AnimalView view;
    private final AnimalRepository repository;

    public AnimalController(
            AnimalView view,
            AnimalRepository repository) {

        this.view = view;
        this.repository = repository;

        view.getAddButton().addActionListener(
                e -> addAnimal()
        );

        view.getUpdateButton().addActionListener(
                e -> updateAnimal()
        );

        view.getDeleteButton().addActionListener(
                e -> deleteAnimal()
        );

        view.getClearButton().addActionListener(
                e -> clearFields()
        );

        view.getAnimalTable()
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedAnimal();
                    }
                });

        refreshTable();
    }

    private void addAnimal() {

        String name = view.getNameField().getText().trim();
        String species = (String) view.getSpeciesComboBox().getSelectedItem();
        String healthStatus = (String) view.getHealthComboBox().getSelectedItem();

        if (name.isEmpty()) {
            view.showMessage("Please enter an animal name.");
            return;
        }

        if (view.getSpeciesComboBox().getSelectedIndex() == 0
                || view.getHealthComboBox().getSelectedIndex() == 0) {
            view.showMessage("Please select a species and health status.");
            return;
        }

        Animal animal = new Animal(
                repository.nextId(),
                name,
                species,
                (Integer) view.getAgeYearsSpinner().getValue(),
                (Integer) view.getAgeMonthsSpinner().getValue(),
                healthStatus,
                view.getAvailableCheckBox().isSelected(),
                view.getDescriptionArea().getText().trim()
        );

        repository.save(animal);
        refreshTable();
        clearFields();
        view.showMessage("Animal added successfully!");
    }

    private void updateAnimal() {

        try {

            int id = Integer.parseInt(
                    view.getIdField().getText().trim()
            );

            if (repository.findById(id).isEmpty()) {

                view.showMessage(
                        "Animal not found."
                );

                return;
            }

            String name =
                    view.getNameField().getText().trim();

            String species = (String) view.getSpeciesComboBox().getSelectedItem();
            String healthStatus = (String) view.getHealthComboBox().getSelectedItem();

            boolean available =
                    view.getAvailableCheckBox().isSelected();

            if (name.isEmpty()
                    || view.getSpeciesComboBox().getSelectedIndex() == 0
                    || view.getHealthComboBox().getSelectedIndex() == 0) {
                view.showMessage(
                        "Enter a name and select a species and health status."
                );
                return;
            }

            Animal updatedAnimal = new Animal(
                    id,
                    name,
                    species,
                    (Integer) view.getAgeYearsSpinner().getValue(),
                    (Integer) view.getAgeMonthsSpinner().getValue(),
                    healthStatus,
                    available,
                    view.getDescriptionArea().getText().trim()
            );

            repository.save(updatedAnimal);

            refreshTable();
            clearFields();

            view.showMessage(
                    "Animal updated successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Select an animal from the table before updating it."
            );
        }
    }

    private void deleteAnimal() {

        try {

            int id = Integer.parseInt(
                    view.getIdField().getText().trim()
            );

            if (repository.findById(id).isEmpty()) {

                view.showMessage(
                        "Animal not found."
                );

                return;
            }

            repository.delete(id);

            refreshTable();
            clearFields();

            view.showMessage(
                    "Animal deleted successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Please select an animal or enter a valid ID."
            );
        }
    }

    private void loadSelectedAnimal() {

        int selectedRow =
                view.getAnimalTable().getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow =
                view.getAnimalTable()
                        .convertRowIndexToModel(selectedRow);

        int id = Integer.parseInt(
                view.getTableModel()
                        .getValueAt(modelRow, 0)
                        .toString()
        );

        repository.findById(id).ifPresent(animal -> {

            view.getIdField().setText(
                    String.valueOf(animal.getId())
            );

            view.getNameField().setText(
                    animal.getName()
            );

            view.getSpeciesComboBox().setSelectedItem(animal.getSpecies());
            view.getAgeYearsSpinner().setValue(animal.getAgeYears());
            view.getAgeMonthsSpinner().setValue(animal.getAgeMonths());
            view.getHealthComboBox().setSelectedItem(animal.getHealthStatus());
            view.getDescriptionArea().setText(animal.getDescription());

            view.getAvailableCheckBox().setSelected(
                    animal.isAvailableForAdoption()
            );
        });
    }

    private void refreshTable() {

        view.getTableModel().setRowCount(0);

        List<Animal> animals =
                repository.findAll();

        for (Animal animal : animals) {

            view.getTableModel().addRow(
                    new Object[]{
                            animal.getId(),
                            animal.getName(),
                            animal.getSpecies(),
                            animal.getAgeYears() + "y "
                                    + animal.getAgeMonths() + "m",
                            animal.getHealthStatus(),
                            animal.isAvailableForAdoption(),
                            animal.getDescription()
                    }
            );
        }
    }

    private void clearFields() {

        view.getIdField().setText("Assigned automatically");
        view.getNameField().setText("");
        view.getSpeciesComboBox().setSelectedIndex(0);
        view.getAgeYearsSpinner().setValue(0);
        view.getAgeMonthsSpinner().setValue(0);
        view.getHealthComboBox().setSelectedIndex(0);
        view.getDescriptionArea().setText("");

        view.getAvailableCheckBox().setSelected(false);

        view.getAnimalTable().clearSelection();
    }
}
