package com.rescue.view;

import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.rescue.view.RescueTheme.*;

// MVC - the View of the Communication screen.
// It only shows things and gives the button clicks to the controller.
public class CommunicationView extends JFrame {

    private final JComboBox<RecipientType> typeBox = new JComboBox<>(RecipientType.values());
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JComboBox<String> channelBox =
            new JComboBox<>(new String[]{"AUTO", "EMAIL", "SMS", "SOCIAL_POST"});
    private final JLabel autoHint = new JLabel(" ");
    private final JTextArea messageArea = new JTextArea(4, 20);

    private final JButton sendButton = new RoundedButton("Send Message");
    private final JButton refreshButton = new RoundedButton("Refresh");
    private final JButton backBtn = new JButton("Back to Menu");

    private final DefaultTableModel historyModel =
            new DefaultTableModel(new String[]{"Time", "To", "Channel", "Status", "Message"}, 0);
    private final JTable historyTable = new JTable(historyModel);
    private final DefaultListModel<String> feedModel = new DefaultListModel<>();
    private final JList<String> feedList = new JList<>(feedModel);

    public CommunicationView() {
        setTitle("Animal Rescue | Communication");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 800));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(LIGHT_GREEN);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Top: title + "Back to Menu" button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GREEN);
        headerPanel.add(header("Communication"), BorderLayout.CENTER);
        backBtn.setFont(FIELD);
        headerPanel.add(backBtn, BorderLayout.EAST);

        // Middle: the form, Bottom: the message log and activity feed
        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setBackground(LIGHT_GREEN);
        center.add(buildForm(), BorderLayout.NORTH);
        center.add(buildHistory(), BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new java.awt.GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(22, 22, 22, 22));

        styleField(nameField);
        styleField(emailField);
        styleField(phoneField);
        styleField(messageArea);
        styleCombo(typeBox);
        styleCombo(channelBox);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        autoHint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        autoHint.setForeground(GREEN);

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(BorderFactory.createEmptyBorder());

        addField(form, "Recipient Type", typeBox, 0);
        addField(form, "Name", nameField, 1);
        addField(form, "Email Address", emailField, 2);
        addField(form, "Phone Number", phoneField, 3);
        addField(form, "Channel", channelBox, 4);
        addField(form, "", autoHint, 5);
        addField(form, "Message", messageScroll, 6);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.setBackground(WHITE);
        sendButton.setPreferredSize(new Dimension(180, 42));
        refreshButton.setPreferredSize(new Dimension(120, 42));
        buttons.add(sendButton);
        buttons.add(javax.swing.Box.createHorizontalStrut(12));
        buttons.add(refreshButton);
        addField(form, "", buttons, 7);
        return form;
    }

    private JPanel buildHistory() {
        historyTable.setDefaultEditor(Object.class, null);   // the table is read-only
        historyTable.setRowHeight(24);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(300);

        JPanel history = new JPanel(new GridLayout(1, 2, 20, 0));
        history.setBackground(LIGHT_GREEN);
        history.add(section("Recent Messages", historyTable));
        history.add(section("Activity Feed", feedList));
        history.setPreferredSize(new Dimension(840, 230));
        return history;
    }

    private JPanel section(String title, java.awt.Component content) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(LIGHT_GREEN);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createLineBorder(SECTION_BORDER));
        scroll.getViewport().setBackground(WHITE);
        panel.add(sectionTitle(title), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------- the controller reads the form with these ----------

    public RecipientType getRecipientType() {
        return (RecipientType) typeBox.getSelectedItem();
    }

    public String getRecipientName() {
        return nameField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPhone() {
        return phoneField.getText().trim();
    }

    // Returns "AUTO", "EMAIL", "SMS" or "SOCIAL_POST".
    public String getSelectedChannel() {
        return (String) channelBox.getSelectedItem();
    }

    public String getMessageText() {
        return messageArea.getText().trim();
    }

    // ---------- the controller updates the screen with these ----------

    public void setAutoHint(String text) {
        autoHint.setText(text);
    }

    public void setContactFieldsEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        phoneField.setEnabled(enabled);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearMessage() {
        messageArea.setText("");
    }

    public void showHistory(List<SentMessage> messages) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd MMM HH:mm");
        historyModel.setRowCount(0);   // clear the table
        for (SentMessage m : messages) {
            String channel = "-";
            if (m.getChannel() != null) {
                channel = m.getChannel().toString();
            }
            String status = "FAILED";
            String details = m.getFailureReason();
            if (m.isSent()) {
                status = "SENT";
                details = m.getText().replace("\n", " ");
            }
            historyModel.addRow(new Object[]{
                    m.getSentAt().format(timeFormat), m.getRecipientName(), channel, status, details});
        }
    }

    public void showFeed(List<String> entries) {
        feedModel.clear();
        for (String entry : entries) {
            feedModel.addElement(entry);
        }
    }

    // ---------- buttons ----------

    public void addSendListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public void addRefreshListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void addRecipientTypeListener(ActionListener listener) {
        typeBox.addActionListener(listener);
    }

    public JButton getBackBtn() {
        return backBtn;
    }
}
