package com.rescue.controller;

import com.rescue.model.EventRegistration;
import com.rescue.model.RescueEvent;
import com.rescue.repo.EventRegistrationRepository;
import com.rescue.repo.RescueEventRepository;
import com.rescue.repo.VolunteerRepository;
import com.rescue.view.EventView;

import javax.swing.JTable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class EventController {

    private final EventView view;
    private final RescueEventRepository eventRepository;
    private final VolunteerRepository volunteerRepository;
    private final EventRegistrationRepository registrationRepository;

    public EventController(EventView view, RescueEventRepository eventRepository,
                           VolunteerRepository volunteerRepository,
                           EventRegistrationRepository registrationRepository) {
        this.view = view;
        this.eventRepository = eventRepository;
        this.volunteerRepository = volunteerRepository;
        this.registrationRepository = registrationRepository;

        view.getBackButton().addActionListener(e -> view.dispose());
        view.getAddButton().addActionListener(e -> addEvent());
        view.getUpdateButton().addActionListener(e -> updateEvent());
        view.getDeleteButton().addActionListener(e -> deleteEvent());
        view.getClearButton().addActionListener(e -> view.clearEventFields());
        view.getEventTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedEvent();
        });

        view.getAddRegistrationButton().addActionListener(e -> saveRegistration(false));
        view.getUpdateRegistrationButton().addActionListener(e -> saveRegistration(true));
        view.getDeleteRegistrationButton().addActionListener(e -> deleteRegistration());
        view.getClearRegistrationButton().addActionListener(e -> view.clearRegistrationFields());
        view.getRegistrationTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedRegistration();
        });
        refreshTables();
    }

    private void addEvent() {
        try {
            eventRepository.save(readEvent(0));
            refreshTables();
            view.clearEventFields();
            view.showMessage("Event added successfully!");
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void updateEvent() {
        try {
            Optional<Integer> selectedId = selectedId(view.getEventTable());
            if (selectedId.isEmpty()) {
                view.showMessage("Select an event from the table to update.");
                return;
            }
            int id = selectedId.get();
            if (eventRepository.findById(id).isEmpty()) {
                view.showMessage("The selected event no longer exists.");
                return;
            }
            eventRepository.save(readEvent(id));
            refreshTables();
            view.clearEventFields();
            view.showMessage("Event updated successfully!");
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private RescueEvent readEvent(int id) {
        String name = view.getNameField().getText().trim();
        String type = (String) view.getTypeBox().getSelectedItem();
        String dateText = view.getDateField().getText().trim();
        String location = view.getLocationField().getText().trim();
        List<String> skills = view.getSkillsList().getSelectedValuesList();
        String description = view.getDescriptionField().getText().trim();
        String hoursText = view.getHoursField().getText().trim();
        if (name.isEmpty() || type.isEmpty() || dateText.isEmpty()
                || location.isEmpty() || description.isEmpty() || hoursText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please fill in the event name, type, date, location, description, and hours.");
        }
        try {
            double hours = Double.parseDouble(hoursText);
            if (!Double.isFinite(hours) || hours <= 0) {
                throw new IllegalArgumentException("Event hours must be greater than zero.");
            }
            return new RescueEvent(id, name, type, LocalDate.parse(dateText),
                    location, skills, description, hours);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a numeric value for event hours.");
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Enter the event date in YYYY-MM-DD format.");
        }
    }

    private void deleteEvent() {
        Optional<Integer> id = selectedId(view.getEventTable());
        if (id.isEmpty() || eventRepository.findById(id.get()).isEmpty()) {
            view.showMessage("Select an event from the table to delete.");
            return;
        }
        if (registrationRepository.findAll().stream()
                .anyMatch(registration -> registration.getEventId() == id.get())) {
            view.showMessage("Remove this event's volunteer registrations before deleting the event.");
            return;
        }
        eventRepository.delete(id.get());
        refreshTables();
        view.clearEventFields();
        view.showMessage("Event deleted successfully!");
    }

    private void saveRegistration(boolean update) {
        try {
            String idText = view.getRegistrationIdField().getText().trim();
            if (update && idText.isEmpty()) {
                view.showMessage("Select an existing registration to update.");
                return;
            }
            if (!update && !idText.isEmpty()) {
                view.showMessage("Leave Registration ID blank when adding a new registration.");
                return;
            }
            int id = idText.isEmpty() ? 0 : parseId(idText);
            if (update && registrationRepository.findById(id).isEmpty()) {
                view.showMessage("Select an existing registration or enter a valid ID.");
                return;
            }

            RescueEvent event = (RescueEvent) view.getEventBox().getSelectedItem();
            com.rescue.model.Volunteer volunteer =
                    (com.rescue.model.Volunteer) view.getVolunteerBox().getSelectedItem();
            String status = (String) view.getStatusBox().getSelectedItem();
            if (event == null || volunteer == null) {
                view.showMessage("Create at least one event and one volunteer before registering.");
                return;
            }
            boolean duplicate = registrationRepository.findAll().stream()
                    .anyMatch(registration -> registration.getId() != id
                            && registration.getEventId() == event.getId()
                            && registration.getVolunteerId() == volunteer.getId());
            if (duplicate) {
                view.showMessage("This volunteer is already registered for the selected event.");
                return;
            }
            LocalDateTime registeredAt = registrationRepository.findById(id)
                    .map(EventRegistration::getRegisteredAt)
                    .orElseGet(LocalDateTime::now);
            registrationRepository.save(new EventRegistration(
                    id, event.getId(), volunteer.getId(), status, registeredAt));
            refreshTables();
            view.clearRegistrationFields();
            view.showMessage(update ? "Registration updated successfully!" : "Volunteer registered successfully!");
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void deleteRegistration() {
        Optional<Integer> id = selectedOrEnteredId(
                view.getRegistrationTable(), view.getRegistrationIdField().getText());
        if (id.isEmpty() || registrationRepository.findById(id.get()).isEmpty()) {
            view.showMessage("Select an existing registration to delete.");
            return;
        }
        registrationRepository.delete(id.get());
        refreshTables();
        view.clearRegistrationFields();
        view.showMessage("Registration deleted successfully!");
    }

    private void loadSelectedEvent() {
        int row = selectedModelRow(view.getEventTable());
        if (row < 0) return;
        int id = Integer.parseInt(view.getTableModel().getValueAt(row, 0).toString());
        eventRepository.findById(id).ifPresent(event -> {
            view.getNameField().setText(event.getName());
            view.getTypeBox().setSelectedItem(event.getType());
            view.getDateField().setText(event.getDate().toString());
            view.getLocationField().setText(event.getLocation());
            view.getSkillsList().clearSelection();
            event.getRequiredSkills().forEach(skill -> {
                int index = com.rescue.model.VolunteerOptions.SKILLS.indexOf(skill);
                if (index >= 0) {
                    view.getSkillsList().addSelectionInterval(index, index);
                }
            });
            view.getDescriptionField().setText(event.getDescription());
            view.getHoursField().setText(String.valueOf(event.getHours()));
        });
    }

    private void loadSelectedRegistration() {
        int row = selectedModelRow(view.getRegistrationTable());
        if (row < 0) return;
        int id = Integer.parseInt(view.getRegistrationTableModel().getValueAt(row, 0).toString());
        registrationRepository.findById(id).ifPresent(registration -> {
            view.getRegistrationIdField().setText(String.valueOf(registration.getId()));
            selectEvent(registration.getEventId());
            for (int i = 0; i < view.getVolunteerBox().getItemCount(); i++) {
                if (view.getVolunteerBox().getItemAt(i).getId() == registration.getVolunteerId()) {
                    view.getVolunteerBox().setSelectedIndex(i);
                    break;
                }
            }
            view.getStatusBox().setSelectedItem(registration.getStatus());
        });
    }

    private void selectEvent(int eventId) {
        for (int i = 0; i < view.getEventBox().getItemCount(); i++) {
            if (view.getEventBox().getItemAt(i).getId() == eventId) {
                view.getEventBox().setSelectedIndex(i);
                break;
            }
        }
    }

    private void refreshTables() {
        view.getTableModel().setRowCount(0);
        eventRepository.findAll().forEach(event ->
                view.getTableModel().addRow(new Object[]{
                        event.getId(),
                        event.getName(),
                        event.getType(),
                        event.getDate(),
                        event.getLocation(),
                        String.join(", ", event.getRequiredSkills()),
                        event.getDescription(),
                        event.getHours()
                }));

        view.setRegistrationOptions(eventRepository.findAll(), volunteerRepository.findAll());
        view.getRegistrationTableModel().setRowCount(0);
        registrationRepository.findAll().forEach(registration -> {
            String eventName = eventRepository.findById(registration.getEventId())
                    .map(RescueEvent::getName).orElse("Missing event");
            String volunteerName = volunteerRepository.findById(registration.getVolunteerId())
                    .map(com.rescue.model.Volunteer::getName).orElse("Missing volunteer");
            view.getRegistrationTableModel().addRow(new Object[]{
                    registration.getId(), eventName, volunteerName,
                    registration.getStatus(), registration.getRegisteredAt()
            });
        });
    }

    private static Optional<Integer> selectedId(JTable table) {
        int row = selectedModelRow(table);
        if (row < 0) return Optional.empty();
        return Optional.of(Integer.parseInt(table.getModel().getValueAt(row, 0).toString()));
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
            throw new IllegalArgumentException("Enter a valid ID.");
        }
    }

}
