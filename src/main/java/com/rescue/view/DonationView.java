
package com.rescue.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DonationView extends JFrame {

    private static final Color DARK_GREEN =
            new Color(31, 92, 57);
    private static final Color GREEN =
            new Color(49, 130, 82);
    private static final Color LIGHT_GREEN =
            new Color(239, 247, 241);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT =
            new Color(48, 61, 52);

    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);

    private final JComboBox<String> typeBox =
            new JComboBox<>(
                    new String[]{"Online", "Cheque", "In-kind"});

    private final JTextField amountField = new JTextField(20);
    private final JTextField receiptField = new JTextField(20);

    private final JCheckBox recurringCheckBox =
            new JCheckBox("Recurring donation");

    private final JCheckBox inMemoryOfCheckBox =
            new JCheckBox("In memory of");

    private final JTextField memoryNameField =
            new JTextField(20);

    private final JButton saveButton =
            new RoundedButton("Save Donation");

    private final JTextArea outputArea = new JTextArea(8, 35);

    public DonationView() {
        setTitle("Animal Rescue | Donations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 650));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(LIGHT_GREEN);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(LIGHT_GREEN);

        JLabel title = new JLabel("Animal Rescue");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK_GREEN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Donation Management");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(GREEN);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        // Form card
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(new EmptyBorder(22, 22, 22, 22));

        styleField(nameField);
        styleField(emailField);
        styleField(phoneField);
        styleField(amountField);
        styleField(receiptField);

        typeBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        typeBox.setBackground(WHITE);
        typeBox.setPreferredSize(new Dimension(250, 38));

        int row = 0;
        addField(formCard, "Donor Name", nameField, row++);
        addField(formCard, "Email Address", emailField, row++);
        addField(formCard, "Phone Number", phoneField, row++);
        addField(formCard, "Donation Type", typeBox, row++);
        addField(formCard, "Amount (EGP)", amountField, row++);
        addField(formCard, "Receipt Message", receiptField, row++);

        recurringCheckBox.setBackground(WHITE);
        inMemoryOfCheckBox.setBackground(WHITE);

        recurringCheckBox.setFont(
                new Font("SansSerif", Font.PLAIN, 14));
        inMemoryOfCheckBox.setFont(
                new Font("SansSerif", Font.PLAIN, 14));

        styleField(memoryNameField);

        addField(formCard, "Recurring",
                recurringCheckBox, row++);

        addField(formCard, "In Memory Of",
                inMemoryOfCheckBox, row++);

        addField(formCard, "Person's Name",
                memoryNameField, row++);

        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(180, 42));

        GridBagConstraints buttonConstraints =
                new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = row;
        buttonConstraints.gridwidth = 2;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(18, 0, 0, 0);

        formCard.add(saveButton, buttonConstraints);

        // Saved donations section
        JPanel historyPanel = new JPanel(new BorderLayout(0, 10));
        historyPanel.setBackground(LIGHT_GREEN);

        JLabel historyTitle = new JLabel("Recent Donations");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        historyTitle.setForeground(DARK_GREEN);

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setForeground(TEXT);
        outputArea.setBackground(WHITE);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(215, 228, 218)));

        historyPanel.add(historyTitle, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setBackground(LIGHT_GREEN);
        center.add(formCard, BorderLayout.NORTH);
        center.add(historyPanel, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);

        memoryNameField.setEnabled(false);

        inMemoryOfCheckBox.addActionListener(e ->
                memoryNameField.setEnabled(
                        inMemoryOfCheckBox.isSelected()));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setPreferredSize(new Dimension(250, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(205, 220, 209)),
                new EmptyBorder(5, 9, 5, 9)));
    }

    private void addField(
            JPanel panel, String label, JComponent field, int row) {

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(0, 0, 14, 14);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        fieldLabel.setForeground(TEXT);
        panel.add(fieldLabel, c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 14, 0);

        panel.add(field, c);
    }

    public String getDonorName() {
        return nameField.getText().trim();
    }

    public String getDonorEmail() {
        return emailField.getText().trim();
    }

    public String getDonorPhone() {
        return phoneField.getText().trim();
    }

    public String getDonationType() {
        return (String) typeBox.getSelectedItem();
    }

    public String getAmountText() {
        return amountField.getText().trim();
    }

    public String getReceiptText() {
        return receiptField.getText().trim();
    }

    public boolean isRecurringSelected() {
        return recurringCheckBox.isSelected();
    }

    public boolean isInMemoryOfSelected() {
        return inMemoryOfCheckBox.isSelected();
    }

    public String getMemoryName() {
        return memoryNameField.getText().trim();
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayDonation(String message) {
        outputArea.append(message + "\n\n");
    }

    public void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        amountField.setText("");
        receiptField.setText("");
        typeBox.setSelectedIndex(0);
        recurringCheckBox.setSelected(false);
        inMemoryOfCheckBox.setSelected(false);
        memoryNameField.setText("");
        memoryNameField.setEnabled(false);
    }


    private static class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);
            setForeground(WHITE);
            setBackground(GREEN);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(10, 18, 10, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color buttonColor = getModel().isPressed()
                    ? DARK_GREEN
                    : getModel().isRollover()
                    ? DARK_GREEN
                    : GREEN;

            g2.setColor(buttonColor);
            g2.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 18, 18);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
