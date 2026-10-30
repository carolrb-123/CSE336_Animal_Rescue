package com.rescue.controller;

import com.rescue.model.Volunteer;
import com.rescue.repo.EventRegistrationRepository;
import com.rescue.repo.VolunteerRepository;
import com.rescue.service.strategy.AvailabilityMatchingStrategy;
import com.rescue.service.strategy.SkillMatchingStrategy;
import com.rescue.service.strategy.VolunteerMatchingStrategy;
import com.rescue.view.VolunteerView;

import javax.swing.JTable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VolunteerController {

    private final VolunteerView view;
    private final VolunteerRepository repository;
    private final EventRegistrationRepository registrationRepository;

    public VolunteerController(VolunteerView view, VolunteerRepository repository,
                               EventRegistrationRepository registrationRepository) {
        this.view = view;
        this.repository = repository;
        this.registrationRepository = registrationRepository;

        view.getBackButton().addActionListener(e -> view.dispose());
        view.getAddButton().addActionListener(e -> addVolunteer());
        view.getUpdateButton().addActionListener(e -> updateVolunteer());
        view.getDeleteButton().addActionListener(e -> deleteVolunteer());
        view.getClearButton().addActionListener(e -> view.clearVolunteerFields());
        view.getVolunteerTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedVolunteer();
        });

        List<VolunteerMatchingStrategy> strategies = List.of(
                new SkillMatchingStrategy(), new AvailabilityMatchingStrategy());
        view.setMatchingStrategies(strategies);
        view.getMatchButton().addActionListener(e -> findVolunteers());
        refreshTable();
    }

    private void addVolunteer() {
        try {
            if (!view.getIdField().getText().isBlank()) {
                view.showMessage("Leave Volunteer ID blank when adding a new volunteer.");
                return;
            }
            Volunteer volunteer = readVolunteer(0);
            repository.save(volunteer);
            refreshTable();
            view.clearVolunteerFields();
            view.showMessage("Volunteer added successfully!");
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void updateVolunteer() {
        try {
            int id = parseId(view.getIdField().getText());
            if (repository.findById(id).isEmpty()) {
                view.showMessage("Select an existing volunteer or enter a valid ID.");
                return;
            }
            repository.save(readVolunteer(id));
            refreshTable();
            view.clearVolunteerFields();
            view.showMessage("Volunteer updated successfully!");
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private Volunteer readVolunteer(int id) {
        String name = view.getNameField().getText().trim();
        String email = view.getEmailField().getText().trim();
        String phone = view.getPhoneField().getText().trim();
        List<String> skills = parseList(view.getSkillsField().getText());
        String availability = view.getAvailabilityField().getText().trim();
        String role = view.getRoleField().getText().trim();
        String hoursText = view.getHoursField().getText().trim();
        List<String> certificates = parseList(view.getCertificatesField().getText());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || skills.isEmpty()
                || availability.isEmpty() || role.isEmpty() || hoursText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please fill in the volunteer's name, contact, skills, availability, role, and hours.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        try {
            double hours = Double.parseDouble(hoursText);
            if (!Double.isFinite(hours) || hours < 0) {
                throw new IllegalArgumentException("Completed hours must be a non-negative number.");
            }
            return new Volunteer(id, name, email, phone, skills, availability, role, hours, certificates);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a numeric value for completed hours.");
        }
    }

    private void deleteVolunteer() {
        Optional<Integer> id = selectedOrEnteredId(view.getVolunteerTable(), view.getIdField().getText());
        if (id.isEmpty() || repository.findById(id.get()).isEmpty()) {
            view.showMessage("Select an existing volunteer to delete.");
            return;
        }
        if (registrationRepository.findAll().stream()
                .anyMatch(registration -> registration.getVolunteerId() == id.get())) {
            view.showMessage("Remove this volunteer's event registrations before deleting the volunteer.");
            return;
        }
        repository.delete(id.get());
        refreshTable();
        view.clearVolunteerFields();
        view.clearMatchResults();
        view.showMessage("Volunteer deleted successfully!");
    }

    private void findVolunteers() {
        VolunteerMatchingStrategy strategy =
                (VolunteerMatchingStrategy) view.getStrategyBox().getSelectedItem();
        String criterion = view.getMatchCriterionField().getText().trim();
        if (strategy == null || criterion.isEmpty()) {
            view.showMessage("Choose a matching strategy and enter a skill or availability.");
            return;
        }
        view.getMatchTableModel().setRowCount(0);
        strategy.match(repository.findAll(), criterion).forEach(volunteer ->
                view.getMatchTableModel().addRow(new Object[]{
                        volunteer.getId(),
                        volunteer.getName(),
                        String.join(", ", volunteer.getSkills()),
                        volunteer.getAvailability(),
                        volunteer.getRole(),
                        volunteer.getHours()
                }));
    }

    private void loadSelectedVolunteer() {
        int row = selectedModelRow(view.getVolunteerTable());
        if (row < 0) return;
        int id = Integer.parseInt(view.getTableModel().getValueAt(row, 0).toString());
        repository.findById(id).ifPresent(volunteer -> {
            view.getIdField().setText(String.valueOf(volunteer.getId()));
            view.getNameField().setText(volunteer.getName());
            view.getEmailField().setText(volunteer.getEmail());
            view.getPhoneField().setText(volunteer.getPhone());
            view.getSkillsField().setText(String.join(", ", volunteer.getSkills()));
            view.getAvailabilityField().setText(volunteer.getAvailability());
            view.getRoleField().setText(volunteer.getRole());
            view.getHoursField().setText(String.valueOf(volunteer.getHours()));
            view.getCertificatesField().setText(String.join(", ", volunteer.getCertificates()));
        });
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        repository.findAll().forEach(volunteer ->
                view.getTableModel().addRow(new Object[]{
                        volunteer.getId(),
                        volunteer.getName(),
                        volunteer.getEmail(),
                        volunteer.getPhone(),
                        String.join(", ", volunteer.getSkills()),
                        volunteer.getAvailability(),
                        volunteer.getRole(),
                        volunteer.getHours(),
                        String.join(", ", volunteer.getCertificates())
                }));
    }

    private static Optional<Integer> selectedOrEnteredId(JTable table, String text) {
        if (!text.isBlank()) {
            try {
                return Optional.of(parseId(text));
            } catch (IllegalArgumentException ex) {
                return Optional.empty();
            }
        }
        int row = selectedModelRow(table);
        if (row < 0) return Optional.empty();
        return Optional.of(Integer.parseInt(table.getModel().getValueAt(row, 0).toString()));
    }

    private static int selectedModelRow(JTable table) {
        int selected = table.getSelectedRow();
        return selected < 0 ? -1 : table.convertRowIndexToModel(selected);
    }

    private static int parseId(String text) {
        try {
            int id = Integer.parseInt(text.trim());
            if (id <= 0) throw new IllegalArgumentException("ID must be positive.");
            return id;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a valid volunteer ID.");
        }
    }

    private static List<String> parseList(String text) {
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}
