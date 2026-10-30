package com.rescue.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AnimalView extends JFrame {

    private static final Color DARK_GREEN =
            new Color(31, 92, 57);

    private static final Color GREEN =
            new Color(49, 130, 82);

    private static final Color LIGHT_GREEN =
            new Color(239, 247, 241);

    private static final Color WHITE = Color.WHITE;

    private static final Color TEXT =
            new Color(48, 61, 52);

    private final JTextField idField = new JTextField(20);
    private final JTextField nameField = new JTextField(20);
    private final JComboBox<String> speciesComboBox =
            new JComboBox<>(new String[]{
                    "Select species...", "Dog", "Cat", "Rabbit", "Bird",
                    "Guinea Pig", "Hamster", "Other"
            });
    private final JSpinner ageYearsSpinner =
            new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    private final JSpinner ageMonthsSpinner =
            new JSpinner(new SpinnerNumberModel(0, 0, 11, 1));
    private final JComboBox<String> healthComboBox =
            new JComboBox<>(new String[]{
                    "Select health status...", "Healthy", "Mildly Ill", "At-Risk"
            });
    private final JTextArea descriptionArea = new JTextArea(3, 20);

    private final JCheckBox availableCheckBox =
            new JCheckBox("Available for adoption");

    private final JButton addButton =
            new RoundedButton("Add");

    private final JButton updateButton =
            new RoundedButton("Update");

    private final JButton deleteButton =
            new RoundedButton("Delete");

    private final JButton clearButton =
            new RoundedButton("Clear");

    private final JButton backButton =
            new RoundedButton("← Back");

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{
                            "ID",
                            "Name",
                            "Species",
                            "Age",
                            "Health",
                            "Available",
                            "Description"
                    },
                    0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable animalTable =
            new JTable(tableModel);

    public AnimalView() {

        setTitle("Animal Rescue | Animals");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(850, 750));

        JPanel mainPanel =
                new JPanel(new BorderLayout(0, 20));

        mainPanel.setBackground(LIGHT_GREEN);

        mainPanel.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        // Header
        JPanel header = new JPanel();

        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.setBackground(LIGHT_GREEN);

        JLabel title = new JLabel("Animal Rescue");

        title.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        title.setForeground(DARK_GREEN);

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle =
                new JLabel("Animal Management");

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        subtitle.setForeground(GREEN);

        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(12));
        styleButton(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(backButton);

        // Form Card
        JPanel formCard =
                new JPanel(new GridBagLayout());

        formCard.setBackground(WHITE);

        formCard.setBorder(
                new EmptyBorder(22, 22, 22, 22)
        );

        styleField(idField);
        styleField(nameField);
        idField.setEditable(false);
        idField.setText("Assigned automatically");
        idField.setToolTipText("Animal IDs are assigned automatically.");
        styleChoice(speciesComboBox);
        styleChoice(healthComboBox);
        styleAgeSpinner(ageYearsSpinner);
        styleAgeSpinner(ageMonthsSpinner);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setForeground(TEXT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(new EmptyBorder(5, 9, 5, 9));

        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        agePanel.setBackground(WHITE);
        agePanel.add(ageYearsSpinner);
        agePanel.add(new JLabel("years"));
        agePanel.add(ageMonthsSpinner);
        agePanel.add(new JLabel("months"));

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(new Dimension(250, 78));
        descriptionScrollPane.setBorder(
                BorderFactory.createLineBorder(new Color(205, 220, 209))
        );

        int row = 0;

        addField(formCard, "Animal ID", idField, row++);
        addField(formCard, "Animal Name", nameField, row++);
        addField(formCard, "Species", speciesComboBox, row++);
        addField(formCard, "Age", agePanel, row++);
        addField(formCard, "Health Status", healthComboBox, row++);
        addField(formCard, "Description (optional)", descriptionScrollPane, row++);

        availableCheckBox.setBackground(WHITE);

        availableCheckBox.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        addField(
                formCard,
                "Availability",
                availableCheckBox,
                row++
        );

        // Buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        buttonPanel.setBackground(WHITE);

        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(clearButton);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        GridBagConstraints buttonConstraints =
                new GridBagConstraints();

        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = row;
        buttonConstraints.gridwidth = 2;
        buttonConstraints.anchor = GridBagConstraints.WEST;

        buttonConstraints.insets =
                new Insets(18, 0, 0, 0);

        formCard.add(buttonPanel, buttonConstraints);

        // Animals Table Section
        JPanel tablePanel =
                new JPanel(new BorderLayout(0, 10));

        tablePanel.setBackground(LIGHT_GREEN);

        JLabel tableTitle =
                new JLabel("Animals");

        tableTitle.setFont(
                new Font("SansSerif", Font.BOLD, 18)
        );

        tableTitle.setForeground(DARK_GREEN);

        animalTable.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        animalTable.setForeground(TEXT);
        animalTable.setBackground(WHITE);
        animalTable.setGridColor(new Color(215, 228, 218));
        animalTable.setSelectionBackground(LIGHT_GREEN);
        animalTable.setSelectionForeground(DARK_GREEN);

        animalTable.setRowHeight(25);

        animalTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        animalTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        animalTable.getTableHeader().setBackground(WHITE);
        animalTable.getTableHeader().setForeground(TEXT);

        animalTable.setFillsViewportHeight(true);

        JScrollPane scrollPane =
                new JScrollPane(animalTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(215, 228, 218)
                )
        );

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Center
        JPanel center =
                new JPanel(new BorderLayout(0, 20));

        center.setBackground(LIGHT_GREEN);

        center.add(formCard, BorderLayout.NORTH);
        center.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        setContentPane(mainPanel);

        pack();

        setLocationRelativeTo(null);
    }

    private void styleField(JTextField field) {

        field.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        field.setForeground(TEXT);
        field.setBackground(WHITE);

        field.setPreferredSize(
                new Dimension(250, 38)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(205, 220, 209)
                        ),
                        new EmptyBorder(5, 9, 5, 9)
                )
        );
    }

        private void styleChoice(JComboBox<String> choice) {
                choice.setFont(new Font("SansSerif", Font.PLAIN, 14));
                choice.setForeground(TEXT);
                choice.setBackground(WHITE);
                choice.setPreferredSize(new Dimension(250, 38));
        }

        private void styleAgeSpinner(JSpinner spinner) {
                spinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
                spinner.setPreferredSize(new Dimension(75, 38));
        }

    private void addField(
            JPanel panel,
            String label,
            JComponent field,
            int row) {

        GridBagConstraints c =
                new GridBagConstraints();

        c.gridy = row;

        c.insets =
                new Insets(0, 0, 14, 14);

        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        JLabel fieldLabel = new JLabel(label);

        fieldLabel.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        fieldLabel.setForeground(TEXT);

        panel.add(fieldLabel, c);

        c.gridx = 1;
        c.weightx = 1;

        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets =
                new Insets(0, 0, 14, 0);

        panel.add(field, c);
    }

    private void styleButton(JButton button) {

        button.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        button.setPreferredSize(
                new Dimension(150, 42)
        );
    }

    public JTextField getIdField() {
        return idField;
    }

    public JTextField getNameField() {
        return nameField;
    }

        public JComboBox<String> getSpeciesComboBox() {
                return speciesComboBox;
    }

        public JSpinner getAgeYearsSpinner() {
                return ageYearsSpinner;
    }

        public JSpinner getAgeMonthsSpinner() {
                return ageMonthsSpinner;
        }

        public JComboBox<String> getHealthComboBox() {
                return healthComboBox;
        }

        public JTextArea getDescriptionArea() {
                return descriptionArea;
    }

    public JCheckBox getAvailableCheckBox() {
        return availableCheckBox;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

        public JButton getBackButton() {
                return backButton;
        }

    public JTable getAnimalTable() {
        return animalTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void showMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message);
    }

    private static class RoundedButton extends JButton {

        public RoundedButton(String text) {

            super(text);

            setForeground(WHITE);
            setBackground(GREEN);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);

            setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            setMargin(
                    new Insets(10, 18, 10, 18)
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color buttonColor =
                    getModel().isPressed()
                            ? DARK_GREEN
                            : getModel().isRollover()
                            ? DARK_GREEN
                            : GREEN;

            g2.setColor(buttonColor);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    18,
                    18
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
