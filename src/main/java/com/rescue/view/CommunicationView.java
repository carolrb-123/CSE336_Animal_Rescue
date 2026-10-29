package com.rescue.view;

import com.rescue.model.ChannelType;
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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static com.rescue.view.RescueTheme.*;

/**
 * MVC: View for the Communication screen, styled like DonationView.
 * It only shows data and reports clicks; all decisions are in CommunicationController.
 */
public class CommunicationView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String AUTO = "Auto (by recipient type)";
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("dd MMM HH:mm");

    private final JComboBox<RecipientType> typeBox = new JComboBox<>();
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JComboBox<Object> channelBox = new JComboBox<>();
    private final JLabel autoHint = new JLabel(" ");
    private final JTextArea messageArea = new JTextArea(4, 20);
    private final JButton sendButton = new RoundedButton("Send Message");

    private final DefaultTableModel historyModel = new DefaultTableModel(
            new Object[]{"Time", "To", "Channel", "Status", "Message"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable historyTable = new JTable(historyModel);
    private final DefaultListModel<String> feedModel = new DefaultListModel<>();
    private final JList<String> feedList = new JList<>(feedModel);

    public CommunicationView() {
        setTitle("Animal Rescue | Communication");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 780));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(LIGHT_GREEN);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setBackground(LIGHT_GREEN);
        center.add(buildFormCard(), BorderLayout.NORTH);
        center.add(buildHistory(), BorderLayout.CENTER);

        mainPanel.add(header("Communication"), BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildFormCard() {
        JPanel formCard = new JPanel(new java.awt.GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(new EmptyBorder(22, 22, 22, 22));

        styleCombo(typeBox);
        styleCombo(channelBox);
        styleField(nameField);
        styleField(emailField);
        styleField(phoneField);
        styleField(messageArea);
        for (JTextField f : new JTextField[]{nameField, emailField, phoneField}) {
            f.setPreferredSize(new Dimension(250, 38));
        }
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        autoHint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        autoHint.setForeground(GREEN);

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(BorderFactory.createEmptyBorder());

        int row = 0;
        addField(formCard, "Recipient Type", typeBox, row++);
        addField(formCard, "Name", nameField, row++);
        addField(formCard, "Email Address", emailField, row++);
        addField(formCard, "Phone Number", phoneField, row++);
        addField(formCard, "Channel", channelBox, row++);
        addField(formCard, "", autoHint, row++);
        addField(formCard, "Message", messageScroll, row++);

        sendButton.setPreferredSize(new Dimension(180, 42));
        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 0;
        b.gridy = row;
        b.gridwidth = 2;
        b.anchor = GridBagConstraints.WEST;
        b.insets = new Insets(8, 0, 0, 0);
        formCard.add(sendButton, b);
        return formCard;
    }

    private JPanel buildHistory() {
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        historyTable.setForeground(TEXT);
        historyTable.setRowHeight(24);
        historyTable.setGridColor(SECTION_BORDER);
        historyTable.setSelectionBackground(LIGHT_GREEN);
        historyTable.setSelectionForeground(TEXT);
        historyTable.getTableHeader().setFont(LABEL);
        historyTable.getTableHeader().setBackground(WHITE);
        historyTable.getTableHeader().setForeground(DARK_GREEN);
        int[] widths = {90, 100, 70, 50, 300};
        for (int i = 0; i < widths.length; i++) {
            historyTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        feedList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        feedList.setForeground(TEXT);

        // Messages get about 60% of the width, the feed the rest.
        JPanel grid = new JPanel(new java.awt.GridBagLayout());
        grid.setBackground(LIGHT_GREEN);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 0.6;
        c.insets = new Insets(0, 0, 0, 20);
        grid.add(section("Recent Messages", historyTable), c);
        c.weightx = 0.4;
        c.insets = new Insets(0, 0, 0, 0);
        grid.add(section("Activity Feed", feedList), c);
        grid.setPreferredSize(new Dimension(840, 230));
        return grid;
    }

    private static JPanel section(String title, java.awt.Component content) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(LIGHT_GREEN);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createLineBorder(SECTION_BORDER));
        scroll.getViewport().setBackground(WHITE);
        panel.add(sectionTitle(title), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ----- filled by the controller -----

    public void setRecipientTypes(RecipientType[] types) {
        typeBox.removeAllItems();
        for (RecipientType t : types) {
            typeBox.addItem(t);
        }
    }

    public void setChannels(Collection<ChannelType> channels) {
        channelBox.removeAllItems();
        channelBox.addItem(AUTO);
        channels.forEach(channelBox::addItem);
    }

    public void setAutoChannelHint(ChannelType channel) {
        autoHint.setText(channel == null ? " " : "Auto sends this recipient type by " + channel.label());
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
        historyModel.setRowCount(0);
        for (SentMessage m : messages) {
            historyModel.addRow(new Object[]{
                    TIME.format(m.getSentAt()),
                    m.getRecipientName(),
                    m.getChannel() == null ? "-" : m.getChannel().label(),
                    m.getStatus(),
                    m.isSent() ? m.getText().replace('\n', ' ') : m.getFailureReason()
            });
        }
    }

    public void showFeed(List<String> entries) {
        feedModel.clear();
        entries.forEach(feedModel::addElement);
    }

    // ----- read by the controller -----

    public RecipientType getRecipientType() { return (RecipientType) typeBox.getSelectedItem(); }
    public String getRecipientName() { return nameField.getText().trim(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getMessageText() { return messageArea.getText().trim(); }

    /** The channel picked by hand, or null for "Auto". */
    public ChannelType getSelectedChannel() {
        return channelBox.getSelectedItem() instanceof ChannelType channel ? channel : null;
    }

    public void addSendListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public void addRecipientTypeListener(ActionListener listener) {
        typeBox.addActionListener(listener);
    }
}
