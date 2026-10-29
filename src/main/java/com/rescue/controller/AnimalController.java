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

        try {

            int id = Integer.parseInt(
                    view.getIdField().getText().trim()
            );

            String name =
                    view.getNameField().getText().trim();

            String species =
                    view.getSpeciesField().getText().trim();

            int age = Integer.parseInt(
                    view.getAgeField().getText().trim()
            );

            String healthStatus =
                    view.getHealthField().getText().trim();

            boolean available =
                    view.getAvailableCheckBox().isSelected();

            if (id <= 0 || age < 0) {

                view.showMessage(
                        "ID must be positive and age cannot be negative."
                );

                return;
            }

            if (name.isEmpty()
                    || species.isEmpty()
                    || healthStatus.isEmpty()) {

                view.showMessage(
                        "Please fill in all fields."
                );

                return;
            }

            if (repository.findById(id).isPresent()) {

                view.showMessage(
                        "Animal ID already exists."
                );

                return;
            }

            Animal animal = new Animal(
                    id,
                    name,
                    species,
                    age,
                    healthStatus,
                    available
            );

            repository.save(animal);

            refreshTable();
            clearFields();

            view.showMessage(
                    "Animal added successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Please enter a valid ID and age."
            );
        }
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

            String species =
                    view.getSpeciesField().getText().trim();

            int age = Integer.parseInt(
                    view.getAgeField().getText().trim()
            );

            String healthStatus =
                    view.getHealthField().getText().trim();

            boolean available =
                    view.getAvailableCheckBox().isSelected();

            if (id <= 0 || age < 0) {

                view.showMessage(
                        "ID must be positive and age cannot be negative."
                );

                return;
            }

            if (name.isEmpty()
                    || species.isEmpty()
                    || healthStatus.isEmpty()) {

                view.showMessage(
                        "Please fill in all fields."
                );

                return;
            }

            Animal updatedAnimal = new Animal(
                    id,
                    name,
                    species,
                    age,
                    healthStatus,
                    available
            );

            repository.save(updatedAnimal);

            refreshTable();
            clearFields();

            view.showMessage(
                    "Animal updated successfully!"
            );

        } catch (NumberFormatException ex) {

            view.showMessage(
                    "Please enter a valid ID and age."
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

            view.getSpeciesField().setText(
                    animal.getSpecies()
            );

            view.getAgeField().setText(
                    String.valueOf(animal.getAge())
            );

            view.getHealthField().setText(
                    animal.getHealthStatus()
            );

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
                            animal.getAge(),
                            animal.getHealthStatus(),
                            animal.isAvailableForAdoption()
                    }
            );
        }
    }

    private void clearFields() {

        view.getIdField().setText("");
        view.getNameField().setText("");
        view.getSpeciesField().setText("");
        view.getAgeField().setText("");
        view.getHealthField().setText("");

        view.getAvailableCheckBox().setSelected(false);

        view.getAnimalTable().clearSelection();
    }
}
