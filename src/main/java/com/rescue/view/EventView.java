package com.rescue.view;

import com.rescue.model.RescueEvent;
import com.rescue.model.Volunteer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import static com.rescue.view.RescueTheme.*;

public class EventView extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JButton backButton = new JButton("Back to Menu");
    private final JTextField idField = new JTextField(20);
    private final JTextField nameField = new JTextField(20);
    private final JTextField typeField = new JTextField(20);
    private final JTextField dateField = new JTextField(20);
    private final JTextField locationField = new JTextField(20);
    private final JTextField skillsField = new JTextField(20);
    private final JTextField descriptionField = new JTextField(20);
    private final JButton addButton = new RoundedButton("Add Event");
    private final JButton updateButton = new RoundedButton("Update Event");
    private final JButton deleteButton = new RoundedButton("Delete Event");
    private final JButton clearButton = new RoundedButton("Clear");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Event", "Type", "Date", "Location", "Required Skills", "Description"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable eventTable = new JTable(tableModel);

    private final JTextField registrationIdField = new JTextField(20);
    private final JComboBox<RescueEvent> eventBox = new JComboBox<>();
    private final JComboBox<Volunteer> volunteerBox = new JComboBox<>();
    private final JComboBox<String> statusBox =
            new JComboBox<>(new String[]{"Registered", "Confirmed", "Completed", "Cancelled"});
    private final JButton addRegistrationButton = new RoundedButton("Add Registration");
    private final JButton updateRegistrationButton = new RoundedButton("Update Registration");
    private final JButton deleteRegistrationButton = new RoundedButton("Delete Registration");
    private final JButton clearRegistrationButton = new RoundedButton("Clear");
    private final DefaultTableModel registrationTableModel = new DefaultTableModel(
            new String[]{"ID", "Event", "Volunteer", "Status", "Registered At"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable registrationTable = new JTable(registrationTableModel);

    public EventView() {
        setTitle("Animal Rescue | Events");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(950, 760));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(LIGHT_GREEN);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_GREEN);
        header.add(header("Event Management"), BorderLayout.CENTER);
        backButton.setFont(FIELD);
        header.add(backButton, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Events", buildEventPanel());
        tabs.addTab("Volunteer Registrations", buildRegistrationPanel());

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel buildEventPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(new EmptyBorder(16, 4, 4, 4));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(new EmptyBorder(22, 22, 22, 22));
        for (JTextField field : new JTextField[]{idField, nameField, typeField, dateField,
                locationField, skillsField, descriptionField}) {
            styleField(field);
        }

        int row = 0;
        addField(formCard, "Event ID", idField, row++);
        addField(formCard, "Event Name", nameField, row++);
        addField(formCard, "Event Type", typeField, row++);
        addField(formCard, "Date (YYYY-MM-DD)", dateField, row++);
        addField(formCard, "Location", locationField, row++);
        addField(formCard, "Required Skills (comma-separated)", skillsField, row++);
        addField(formCard, "Description", descriptionField, row++);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setBackground(WHITE);
        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(clearButton);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = row;
        buttonConstraints.gridwidth = 2;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(18, 0, 0, 0);
        formCard.add(buttons, buttonConstraints);

        panel.add(formCard, BorderLayout.NORTH);
        panel.add(buildTablePanel("Events", eventTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRegistrationPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(new EmptyBorder(16, 4, 4, 4));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(new EmptyBorder(22, 22, 22, 22));
        styleField(registrationIdField);
        styleCombo(eventBox);
        styleCombo(volunteerBox);
        styleCombo(statusBox);

        addField(formCard, "Registration ID", registrationIdField, 0);
        addField(formCard, "Event", eventBox, 1);
        addField(formCard, "Volunteer", volunteerBox, 2);
        addField(formCard, "Status", statusBox, 3);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setBackground(WHITE);
        for (JButton button : new JButton[]{addRegistrationButton, updateRegistrationButton,
                deleteRegistrationButton, clearRegistrationButton}) {
            styleButton(button);
            buttons.add(button);
        }
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 4;
        buttonConstraints.gridwidth = 2;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(18, 0, 0, 0);
        formCard.add(buttons, buttonConstraints);

        panel.add(formCard, BorderLayout.NORTH);
        panel.add(buildTablePanel("Volunteer Registrations", registrationTable), BorderLayout.CENTER);
        return panel;
    }

    private static JPanel buildTablePanel(String title, JTable table) {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(LIGHT_GREEN);
        tablePanel.add(sectionTitle(title), BorderLayout.NORTH);

        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setGridColor(SECTION_BORDER);
        table.setSelectionBackground(LIGHT_GREEN);
        table.setSelectionForeground(DARK_GREEN);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(LABEL);
        table.getTableHeader().setBackground(WHITE);
        table.getTableHeader().setForeground(TEXT);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(SECTION_BORDER));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private static void styleButton(JButton button) {
        button.setFont(BUTTON);
        button.setPreferredSize(new Dimension(180, 42));
    }

    private void styleField(JTextField field) {
        RescueTheme.styleField(field);
        field.setPreferredSize(new Dimension(250, 38));
    }

    private static void addField(JPanel panel, String label, javax.swing.JComponent field, int row) {
        RescueTheme.addField(panel, label, field, row);
    }

    public void setRegistrationOptions(List<RescueEvent> events, List<Volunteer> volunteers) {
        eventBox.removeAllItems();
        events.forEach(eventBox::addItem);
        volunteerBox.removeAllItems();
        volunteers.forEach(volunteerBox::addItem);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearEventFields() {
        idField.setText("");
        nameField.setText("");
        typeField.setText("");
        dateField.setText("");
        locationField.setText("");
        skillsField.setText("");
        descriptionField.setText("");
        eventTable.clearSelection();
    }

    public void clearRegistrationFields() {
        registrationIdField.setText("");
        if (eventBox.getItemCount() > 0) eventBox.setSelectedIndex(0);
        if (volunteerBox.getItemCount() > 0) volunteerBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        registrationTable.clearSelection();
    }

    public JButton getBackButton() { return backButton; }
    public JTextField getIdField() { return idField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getTypeField() { return typeField; }
    public JTextField getDateField() { return dateField; }
    public JTextField getLocationField() { return locationField; }
    public JTextField getSkillsField() { return skillsField; }
    public JTextField getDescriptionField() { return descriptionField; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getClearButton() { return clearButton; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getEventTable() { return eventTable; }
    public JTextField getRegistrationIdField() { return registrationIdField; }
    public JComboBox<RescueEvent> getEventBox() { return eventBox; }
    public JComboBox<Volunteer> getVolunteerBox() { return volunteerBox; }
    public JComboBox<String> getStatusBox() { return statusBox; }
    public JButton getAddRegistrationButton() { return addRegistrationButton; }
    public JButton getUpdateRegistrationButton() { return updateRegistrationButton; }
    public JButton getDeleteRegistrationButton() { return deleteRegistrationButton; }
    public JButton getClearRegistrationButton() { return clearRegistrationButton; }
    public DefaultTableModel getRegistrationTableModel() { return registrationTableModel; }
    public JTable getRegistrationTable() { return registrationTable; }
}
