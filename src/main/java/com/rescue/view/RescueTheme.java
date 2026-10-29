package com.rescue.view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;

/**
 * The look of DonationView (colors, fonts, fields, rounded button) in one place,
 * so every screen can match it. Values copied from DonationView.
 */
public final class RescueTheme {

    public static final Color DARK_GREEN = new Color(31, 92, 57);
    public static final Color GREEN = new Color(49, 130, 82);
    public static final Color LIGHT_GREEN = new Color(239, 247, 241);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT = new Color(48, 61, 52);
    public static final Color FIELD_BORDER = new Color(205, 220, 209);
    public static final Color SECTION_BORDER = new Color(215, 228, 218);

    public static final Font TITLE = new Font("SansSerif", Font.BOLD, 28);
    public static final Font SUBTITLE = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font SECTION = new Font("SansSerif", Font.BOLD, 18);
    public static final Font LABEL = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FIELD = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font BUTTON = new Font("SansSerif", Font.BOLD, 14);

    private RescueTheme() {
    }

    /** "Animal Rescue" title with a green subtitle, as at the top of DonationView. */
    public static JPanel header(String subtitleText) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(LIGHT_GREEN);

        JLabel title = new JLabel("Animal Rescue");
        title.setFont(TITLE);
        title.setForeground(DARK_GREEN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setFont(SUBTITLE);
        subtitle.setForeground(GREEN);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);
        return header;
    }

    public static JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SECTION);
        label.setForeground(DARK_GREEN);
        return label;
    }

    public static void styleField(JTextComponent field) {
        field.setFont(FIELD);
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(5, 9, 5, 9)));
    }

    public static void styleCombo(JComponent combo) {
        combo.setFont(FIELD);
        combo.setBackground(WHITE);
        combo.setPreferredSize(new Dimension(250, 38));
    }

    /** Adds "Label | field" as one row of a GridBagLayout form card, spaced like DonationView. */
    public static void addField(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(0, 0, 14, 14);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(LABEL);
        fieldLabel.setForeground(TEXT);
        panel.add(fieldLabel, c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 14, 0);
        panel.add(field, c);
    }

    /** The green rounded button from DonationView. */
    public static class RoundedButton extends JButton {

        private static final long serialVersionUID = 1L;

        public RoundedButton(String text) {
            super(text);
            setFont(BUTTON);
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() || getModel().isRollover() ? DARK_GREEN : GREEN);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
