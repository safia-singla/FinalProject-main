package com.hotel.views;

import com.hotel.database.InventoryDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog window for logging inventory item usage.
 * Allows the user to input the item name and the quantity used,
 * and updates the inventory accordingly.
 */
public class LogUsageDialog extends JDialog {

    private JTextField itemNameField;
    private JTextField quantityField;

    /**
     * Constructs the Log Usage dialog.
     * @param parent the parent frame to which this dialog is modal.
     */
    public LogUsageDialog(Frame parent) {
        super(parent, "Log Inventory Usage", true);
        setSize(350, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Form panel for input fields
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        form.setBackground(Color.WHITE);

        itemNameField = new JTextField();
        quantityField = new JTextField();

        form.add(makeField("Item Name:", itemNameField));
        form.add(makeField("Quantity Used:", quantityField));
        add(form, BorderLayout.CENTER);

        // Buttons panel for actions
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton logButton = new JButton("✔ Log Usage");
        JButton cancelButton = new JButton("✖ Cancel");

        buttons.add(logButton);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);

        // Action listener for logging usage
        logButton.addActionListener(e -> {
            String itemName = itemNameField.getText().trim();
            int quantityUsed;

            // Validate item name
            if (itemName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Item name cannot be empty.");
                return;
            }

            // Validate quantity input
            try {
                quantityUsed = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a valid number.");
                return;
            }

            // Attempt to log the usage in the database
            boolean success = new InventoryDAO().logUsage(itemName, quantityUsed);
            if (success) {
                JOptionPane.showMessageDialog(this, "Usage logged successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to log usage. See console for details.");
            }
        });

        // Action listener to close dialog
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    /**
     * Helper method to create a labeled input field panel.
     * @param label the label text.
     * @param input the input component.
     * @return the assembled JPanel.
     */
    private JPanel makeField(String label, JComponent input) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(250, 60));
        return panel;
    }
} 
