package com.hotel.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for showing styled pop-up messages in the application.
 * Provides methods to display success, error, and confirmation dialogs.
 */
public class PopupUtil {

    /**
     * Displays a success pop-up with a green background.
     *
     * @param parent the parent component relative to which the pop-up is positioned
     * @param message the success message to display
     */
    public static void showSuccess(Component parent, String message) {
        showPopup(parent, "✅ " + message, new Color(240, 255, 240), new Color(34, 139, 34)); // light green bg
    }

    /**
     * Displays an error pop-up with a red background.
     *
     * @param parent the parent component relative to which the pop-up is positioned
     * @param message the error message to display
     */
    public static void showError(Component parent, String message) {
        showPopup(parent, "❌ " + message, new Color(255, 240, 240), new Color(178, 34, 34)); // light red bg
    }

    /**
     * Displays a confirmation dialog with Yes/No options.
     *
     * @param parent the parent component relative to which the dialog is positioned
     * @param message the confirmation message to display
     * @return JOptionPane.YES_OPTION if 'Yes' is clicked, JOptionPane.NO_OPTION otherwise
     */
    public static int showConfirmation(Component parent, String message) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Confirm Delete", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(420, 160);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        panel.setLayout(new BorderLayout(10, 10));

        JLabel textLabel = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        final int[] result = {-1};

        JButton noBtn = new JButton("No");
        noBtn.setBackground(Color.WHITE);
        noBtn.setForeground(Color.BLACK);
        noBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noBtn.setFocusPainted(false);
        noBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        noBtn.addActionListener(e -> {
            result[0] = JOptionPane.NO_OPTION;
            dialog.dispose();
        });

        JButton yesBtn = new JButton("Yes");
        yesBtn.setBackground(new Color(70, 130, 180));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        yesBtn.setFocusPainted(false);
        yesBtn.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        yesBtn.addActionListener(e -> {
            result[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });

        buttonPanel.add(noBtn);
        buttonPanel.add(yesBtn);

        panel.add(textLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);

        return result[0];
    }

    /**
     * Helper method to show a custom pop-up dialog with specified styles.
     *
     * @param parent the parent component relative to which the dialog is positioned
     * @param message the message to display
     * @param bgColor the background color of the pop-up
     * @param textColor the text color of the message
     */
    private static void showPopup(Component parent, String message, Color bgColor, Color textColor) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(window, "Message", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(400, 140);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton okButton = new JButton("OK");
        okButton.setForeground(new Color(70, 130, 180));
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        okButton.setFocusPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));
        okButton.setMaximumSize(new Dimension(100, 35));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(okButton);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }
}
