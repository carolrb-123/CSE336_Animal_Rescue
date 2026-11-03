package com.rescue.controller;

import com.rescue.model.Volunteer;
import com.rescue.model.VolunteerOptions;
import com.rescue.repo.EventRegistrationRepository;
import com.rescue.repo.RescueEventRepository;
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
    private final RescueEventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;

    public VolunteerController(VolunteerView view, VolunteerRepository repository,
                               RescueEventRepository eventRepository,
                               EventRegistrationRepository registrationRepository) {
        this.view = view;
        this.repository = repository;
        this.eventRepository = eventRepository;
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
        view.setMatchOptions(VolunteerOptions.SKILLS);
        view.getStrategyBox().addActionListener(e -> updateMatchOptions());
        view.getMatchButton().addActionListener(e -> findVolunteers());
        refreshTable();
    }

    private void addVolunteer() {
        try {
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
            Optional<Integer> selectedId = selectedId(view.getVolunteerTable());
            if (selectedId.isEmpty()) {
                view.showMessage("Select a volunteer from the table to update.");
                return;
            }
            int id = selectedId.get();
            if (repository.findById(id).isEmpty()) {
                view.showMessage("The selected volunteer no longer exists.");
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
        List<String> skills = view.getSkillsList().getSelectedValuesList();
        List<String> availability = view.getAvailabilityList().getSelectedValuesList();
        List<String> certificates = parseList(view.getCertificatesField().getText());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || skills.isEmpty()
                || availability.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please fill in the volunteer's name, contact, skills, and availability.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        return new Volunteer(id, name, email, phone, skills, availability, certificates);
    }

    private void deleteVolunteer() {
        Optional<Integer> id = selectedId(view.getVolunteerTable());
        if (id.isEmpty() || repository.findById(id.get()).isEmpty()) {
            view.showMessage("Select a volunteer from the table to delete.");
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
        String criterion = (String) view.getMatchCriterionBox().getSelectedItem();
        if (strategy == null || criterion == null) {
            view.showMessage("Choose a matching strategy and match option.");
            return;
        }
        view.getMatchTableModel().setRowCount(0);
        strategy.match(repository.findAll(), criterion).forEach(volunteer ->
                view.getMatchTableModel().addRow(new Object[]{
                        volunteer.getId(),
                        volunteer.getName(),
                        String.join(", ", volunteer.getSkills()),
                        String.join(", ", volunteer.getAvailability()),
                        completedHours(volunteer.getId())
                }));
    }

    private void loadSelectedVolunteer() {
        int row = selectedModelRow(view.getVolunteerTable());
        if (row < 0) return;
        int id = Integer.parseInt(view.getTableModel().getValueAt(row, 0).toString());
        repository.findById(id).ifPresent(volunteer -> {
            view.getNameField().setText(volunteer.getName());
            view.getEmailField().setText(volunteer.getEmail());
            view.getPhoneField().setText(volunteer.getPhone());
            view.getSkillsList().clearSelection();
            volunteer.getSkills().forEach(skill -> {
                int index = VolunteerOptions.SKILLS.indexOf(skill);
                if (index >= 0) {
                    view.getSkillsList().addSelectionInterval(index, index);
                }
            });
            view.getAvailabilityList().clearSelection();
            volunteer.getAvailability().forEach(availability -> {
                int index = VolunteerOptions.AVAILABILITIES.indexOf(availability);
                if (index >= 0) {
                    view.getAvailabilityList().addSelectionInterval(index, index);
                }
            });
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
                        String.join(", ", volunteer.getAvailability()),
                        completedHours(volunteer.getId()),
                        String.join(", ", volunteer.getCertificates())
                }));
    }

    private double completedHours(int volunteerId) {
        return registrationRepository.findAll().stream()
                .filter(registration -> registration.getVolunteerId() == volunteerId)
                .filter(registration -> "Completed".equals(registration.getStatus()))
                .mapToDouble(registration -> eventRepository.findById(registration.getEventId())
                        .map(event -> event.getHours())
                        .orElse(0.0))
                .sum();
    }

    private static Optional<Integer> selectedId(JTable table) {
        int row = selectedModelRow(table);
        if (row < 0) return Optional.empty();
        return Optional.of(Integer.parseInt(table.getModel().getValueAt(row, 0).toString()));
    }

    private static int selectedModelRow(JTable table) {
        int selected = table.getSelectedRow();
        return selected < 0 ? -1 : table.convertRowIndexToModel(selected);
    }

    private static List<String> parseList(String text) {
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private void updateMatchOptions() {
        VolunteerMatchingStrategy strategy =
                (VolunteerMatchingStrategy) view.getStrategyBox().getSelectedItem();
        view.setMatchOptions(strategy instanceof SkillMatchingStrategy
                ? VolunteerOptions.SKILLS
                : VolunteerOptions.AVAILABILITIES);
    }
}
