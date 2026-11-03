package com.rescue.view;

import com.rescue.service.strategy.VolunteerMatchingStrategy;
import com.rescue.model.VolunteerOptions;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.DefaultListSelectionModel;
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

public class VolunteerView extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JButton backButton = new JButton("Back to Menu");
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JList<String> skillsList = new JList<>(
            VolunteerOptions.SKILLS.toArray(String[]::new));
    private final JList<String> availabilityList = new JList<>(
            VolunteerOptions.AVAILABILITIES.toArray(String[]::new));
    private final JTextField certificatesField = new JTextField(20);
    private final JButton addButton = new RoundedButton("Add");
    private final JButton updateButton = new RoundedButton("Update");
    private final JButton deleteButton = new RoundedButton("Delete");
    private final JButton clearButton = new RoundedButton("Clear");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Phone", "Skills", "Availability", "Completed Hours", "Certificates"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable volunteerTable = new JTable(tableModel);

    private final JComboBox<VolunteerMatchingStrategy> strategyBox = new JComboBox<>();
    private final JComboBox<String> matchCriterionBox = new JComboBox<>();
    private final JButton matchButton = new RoundedButton("Match");
    private final DefaultTableModel matchTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Skills", "Availability", "Completed Hours"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable matchTable = new JTable(matchTableModel);

    public VolunteerView() {
        setTitle("Animal Rescue | Volunteers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(950, 700));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(LIGHT_GREEN);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_GREEN);
        header.add(header("Volunteer Management"), BorderLayout.CENTER);
        backButton.setFont(FIELD);
        header.add(backButton, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Volunteers", scrollable(buildVolunteerPanel()));
        tabs.addTab("Volunteer Matching", scrollable(buildMatchingPanel()));

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setSize(1100, 760);
        setLocationRelativeTo(null);
    }

    private JPanel buildVolunteerPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(new EmptyBorder(16, 4, 4, 4));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(new EmptyBorder(22, 22, 22, 22));
        for (JTextField field : new JTextField[]{nameField, emailField, phoneField, certificatesField}) {
            styleField(field);
        }
        styleMultiSelectList(skillsList, 6);
        styleMultiSelectList(availabilityList, 3);

        int row = 0;
        addField(formCard, "Volunteer Name", nameField, row++);
        addField(formCard, "Email Address", emailField, row++);
        addField(formCard, "Phone Number", phoneField, row++);
        addField(formCard, "Skills (select one or more)", new JScrollPane(skillsList), row++);
        addField(formCard, "Availability (select one or more)",
                new JScrollPane(availabilityList), row++);
        addField(formCard, "Certificates (comma-separated)", certificatesField, row++);

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
        panel.add(buildTablePanel("Volunteers", volunteerTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMatchingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(new EmptyBorder(16, 4, 4, 4));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(new EmptyBorder(22, 22, 22, 22));
        styleCombo(strategyBox);
        styleCombo(matchCriterionBox);
        addField(formCard, "Matching Strategy", strategyBox, 0);
        addField(formCard, "Match Option", matchCriterionBox, 1);
        styleButton(matchButton);
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 2;
        buttonConstraints.gridwidth = 2;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(4, 0, 0, 0);
        formCard.add(matchButton, buttonConstraints);

        panel.add(formCard, BorderLayout.NORTH);
        panel.add(buildTablePanel("Matching Volunteers", matchTable), BorderLayout.CENTER);
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
        table.setRowHeight(34);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(LABEL);
        table.getTableHeader().setBackground(WHITE);
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setPreferredSize(new Dimension(0, 32));
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths(table);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(SECTION_BORDER));
        scrollPane.setPreferredSize(new Dimension(0, 260));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private static JScrollPane scrollable(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private static void styleMultiSelectList(JList<String> list, int visibleRows) {
        list.setVisibleRowCount(visibleRows);
        list.setSelectionModel(new ToggleSelectionModel());
        list.setFont(FIELD);
        list.setForeground(TEXT);
        list.setBackground(WHITE);
        list.setBorder(BorderFactory.createLineBorder(FIELD_BORDER));
    }

    private static final class ToggleSelectionModel extends DefaultListSelectionModel {

        private static final long serialVersionUID = 1L;

        @Override
        public void setSelectionInterval(int index0, int index1) {
            if (index0 == index1 && isSelectedIndex(index0)) {
                removeSelectionInterval(index0, index1);
            } else {
                addSelectionInterval(index0, index1);
            }
        }
    }

    private static void setColumnWidths(JTable table) {
        int[] widths = table.getColumnCount() == 8
                ? new int[]{45, 145, 210, 125, 280, 280, 120, 190}
                : new int[]{45, 145, 280, 280, 120};
        for (int column = 0; column < widths.length; column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(widths[column]);
        }
    }

    private static void styleButton(JButton button) {
        button.setFont(BUTTON);
        button.setPreferredSize(new Dimension(150, 42));
    }

    private void styleField(JTextField field) {
        RescueTheme.styleField(field);
        field.setPreferredSize(new Dimension(250, 38));
    }

    private static void addField(JPanel panel, String label, javax.swing.JComponent field, int row) {
        RescueTheme.addField(panel, label, field, row);
    }

    public void setMatchingStrategies(List<VolunteerMatchingStrategy> strategies) {
        strategyBox.removeAllItems();
        strategies.forEach(strategyBox::addItem);
        strategyBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value == null ? "" : value.getName());
            label.setFont(FIELD);
            return label;
        });
    }

    public void setMatchOptions(List<String> options) {
        matchCriterionBox.removeAllItems();
        options.forEach(matchCriterionBox::addItem);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearVolunteerFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        skillsList.clearSelection();
        availabilityList.clearSelection();
        certificatesField.setText("");
        volunteerTable.clearSelection();
    }

    public void clearMatchResults() {
        matchTableModel.setRowCount(0);
    }

    public JButton getBackButton() { return backButton; }
    public JTextField getNameField() { return nameField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getPhoneField() { return phoneField; }
    public JList<String> getSkillsList() { return skillsList; }
    public JList<String> getAvailabilityList() { return availabilityList; }
    public JTextField getCertificatesField() { return certificatesField; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getClearButton() { return clearButton; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getVolunteerTable() { return volunteerTable; }
    public JComboBox<VolunteerMatchingStrategy> getStrategyBox() { return strategyBox; }
    public JComboBox<String> getMatchCriterionBox() { return matchCriterionBox; }
    public JButton getMatchButton() { return matchButton; }
    public DefaultTableModel getMatchTableModel() { return matchTableModel; }
}
