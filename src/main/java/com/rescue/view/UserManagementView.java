package com.rescue.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static com.rescue.view.RescueTheme.*;

public class UserManagementView extends JFrame {
    private final JButton backBtn = new JButton("Back to Menu");
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField = new JTextField(12);
    private JPasswordField passwordField = new JPasswordField(12);
    private JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Admin", "Volunteer", "Donor"});

    private JButton addBtn = new RoundedButton("Create User");
    private JButton updateBtn = new RoundedButton("Update Selected");
    private JButton deleteBtn = new RoundedButton("Delete Selected");
    private JButton clearBtn = new RoundedButton("Clear");

    public UserManagementView() {
        setTitle("Animal Rescue | Users");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(LIGHT_GREEN);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header with Back Button on the Right
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_GREEN);
        header.add(header("User Management"), BorderLayout.CENTER);
        backBtn.setFont(FIELD);
        header.add(backBtn, BorderLayout.EAST);

        // Form Card
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        styleField(usernameField);
        styleField(passwordField);
        styleCombo(roleCombo);

        addField(formCard, "Username", usernameField, 0);
        addField(formCard, "Password", passwordField, 1);
        addField(formCard, "Role", roleCombo, 2);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(WHITE);
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);

        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0; bc.gridy = 3; bc.gridwidth = 2;
        bc.anchor = GridBagConstraints.WEST; bc.insets = new Insets(18, 0, 0, 0);
        formCard.add(btnPanel, bc);

        // Styled Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Role"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userTable.setForeground(TEXT);
        userTable.setBackground(WHITE);
        userTable.setGridColor(SECTION_BORDER);
        userTable.setSelectionBackground(LIGHT_GREEN);
        userTable.setSelectionForeground(DARK_GREEN);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(LABEL);
        userTable.getTableHeader().setBackground(WHITE);
        userTable.getTableHeader().setForeground(TEXT);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(LIGHT_GREEN);
        tablePanel.add(sectionTitle("System Users"), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(userTable);
        scroll.setBorder(BorderFactory.createLineBorder(SECTION_BORDER));
        tablePanel.add(scroll, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setBackground(LIGHT_GREEN);
        center.add(formCard, BorderLayout.NORTH);
        center.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getUserTable() { return userTable; }
    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getRole() { return (String) roleCombo.getSelectedItem(); }
    public void setFields(String username, String role) {
        usernameField.setText(username);
        roleCombo.setSelectedItem(role);
        passwordField.setText("");
    }
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
    public JButton getAddBtn() { return addBtn; }
    public JButton getUpdateBtn() { return updateBtn; }
    public JButton getDeleteBtn() { return deleteBtn; }
    public JButton getBackBtn() { return backBtn; }
}