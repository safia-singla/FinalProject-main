package com.hotel.views;

import com.hotel.database.InventoryDAO;
import com.hotel.models.InventoryItem;
import com.hotel.patterns.InventoryAlertService;
import com.hotel.patterns.InventoryNotifier;
import com.hotel.utils.PopupUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * InventoryView provides an interface for managing inventory items.
 * Includes functionalities to add new items, update quantities, and view usage reports.
 */
public class InventoryView extends JFrame {

    private JTextField itemNameField, quantityField, thresholdField;
    private JComboBox<String> departmentFilter;
    private DefaultTableModel tableModel;

    /**
     * Constructs the InventoryView frame, initializes UI components and layout.
     */
    public InventoryView() {
        setTitle("Inventory Management");
        setSize(950, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title label
        JLabel title = new JLabel("\uD83C\uDFE2 Inventory Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Form panel for item input
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        formPanel.setBackground(Color.WHITE);

        itemNameField = new JTextField();
        quantityField = new JTextField();
        thresholdField = new JTextField();

        formPanel.add(makeField("Item Name:", itemNameField));
        formPanel.add(makeField("Quantity:", quantityField));
        formPanel.add(makeField("Minimum Threshold:", thresholdField));

        departmentFilter = new JComboBox<>(new String[]{"All", "Housekeeping", "Kitchen", "Reception"});
        formPanel.add(makeField("Filter by Department:", departmentFilter));
        formPanel.add(Box.createVerticalStrut(15));

        JButton addBtn = new JButton("➕ Add Item");
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(this::handleAddItem);
        formPanel.add(addBtn);
        formPanel.add(Box.createVerticalStrut(10));

        JButton logUsageBtn = new JButton("\uD83D\uDCC9 Log Usage");
        logUsageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logUsageBtn.addActionListener(e -> new LogUsageDialog(this));
        formPanel.add(logUsageBtn);
        formPanel.add(Box.createVerticalStrut(10));

        JButton viewUsageBtn = new JButton("\uD83D\uDCCA View Usage Report");
        viewUsageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewUsageBtn.addActionListener(e -> new UsageReportView());
        formPanel.add(viewUsageBtn);
        formPanel.add(Box.createVerticalStrut(15));

        JButton backBtn = new JButton("\u2190 Back to Dashboard");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> {
            dispose();
            new MainMenuView(LoginView.loggedInUser);
        });
        formPanel.add(backBtn);

        add(formPanel, BorderLayout.WEST);

        // Table panel for displaying inventory items
        String[] columns = {"ID", "Item Name", "Quantity", "Threshold", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 2; // Only quantity is editable
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Handle quantity update in the table
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 2) {
                try {
                    int itemId = (int) tableModel.getValueAt(row, 0);
                    int newQuantity = Integer.parseInt(tableModel.getValueAt(row, col).toString());
                    new InventoryDAO().updateQuantity(itemId, newQuantity);
                    PopupUtil.showSuccess(this, "Inventory updated successfully!");
                    loadItems();
                } catch (Exception ex) {
                    PopupUtil.showError(this, "Invalid quantity update: " + ex.getMessage());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Observer setup for inventory alerts
        InventoryNotifier.addObserver(new InventoryAlertService(this));

        loadItems();
        setVisible(true);
    }

    /**
     * Utility method to create a labeled input field panel.
     */
    private JPanel makeField(String label, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(l, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(250, 70));
        return p;
    }

    /**
     * Handles adding a new inventory item based on form input.
     */
    private void handleAddItem(ActionEvent e) {
        try {
            String name = itemNameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());
            String department = (String) departmentFilter.getSelectedItem();

            if (name.isEmpty() || quantity < 0 || threshold < 0) {
                PopupUtil.showError(this, "Invalid input values.");
                return;
            }

            InventoryDAO dao = new InventoryDAO();
            if (dao.itemExists(name)) {
                PopupUtil.showError(this, "This item already exists in inventory!");
                return;
            }

            InventoryItem item = new InventoryItem(name, quantity, threshold, department);
            if (dao.addItem(item)) {
                PopupUtil.showSuccess(this, "Item added!");
                itemNameField.setText("");
                quantityField.setText("");
                thresholdField.setText("");
                loadItems();
            } else {
                PopupUtil.showError(this, "Failed to add item.");
            }
        } catch (Exception ex) {
            PopupUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Loads inventory items into the table for display.
     * Also triggers low stock alerts if applicable.
     */
    private void loadItems() {
        tableModel.setRowCount(0);
        List<InventoryItem> items = new InventoryDAO().getAllItems();
        List<String> lowStock = new ArrayList<>();

        for (InventoryItem i : items) {
            tableModel.addRow(new Object[]{
                    i.getId(),
                    i.getItemName(),
                    i.getQuantity(),
                    i.getThreshold(),
                    i.getDepartment()
            });

            if (i.getQuantity() < i.getThreshold()) {
                lowStock.add("• " + i.getItemName() + " (" + i.getQuantity() + " left)");
            }
        }

        if (!lowStock.isEmpty()) {
            InventoryNotifier.notifyObservers(lowStock);
        }
    }
}