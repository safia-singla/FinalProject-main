package com.hotel.database;

import com.hotel.models.InventoryItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    /**
     * Adds a new inventory item to the database.
     *
     * @param item the inventory item to add
     * @return true if the item was successfully added, false otherwise
     */
    public boolean addItem(InventoryItem item) {
        String sql = "INSERT INTO inventory_items (item_name, quantity, threshold, department) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setInt(2, item.getQuantity());
            stmt.setInt(3, item.getThreshold());
            stmt.setString(4, item.getDepartment()); 
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding inventory item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all inventory items from the database.
     *
     * @return a list of all inventory items
     */
    public List<InventoryItem> getAllItems() {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory_items";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                InventoryItem item = new InventoryItem(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getInt("threshold"),
                        rs.getString("department")
                );
                list.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error loading inventory items: " + e.getMessage());
        }

        return list;
    }

    /**
     * Updates the quantity of a specific inventory item.
     *
     * @param id the ID of the inventory item
     * @param newQuantity the new quantity to set
     */
    public void updateQuantity(int id, int newQuantity) {
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE inventory_items SET quantity = ? WHERE id = ?")) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an inventory item exists in the database by its name.
     *
     * @param itemName the name of the inventory item
     * @return true if the item exists, false otherwise
     */
    public boolean itemExists(String itemName) {
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id FROM inventory_items WHERE LOWER(item_name) = ?")) {

            stmt.setString(1, itemName.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the available stock of an item is sufficient for the requested quantity.
     *
     * @param itemName the name of the inventory item
     * @param requiredQuantity the required quantity to check
     * @return true if the stock is sufficient, false otherwise
     */
    public boolean isStockSufficient(String itemName, int requiredQuantity) {
        String sql = "SELECT quantity FROM inventory_items WHERE item_name = ?";
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int availableQuantity = rs.getInt("quantity");
                return availableQuantity >= requiredQuantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Generates a usage report of inventory items.
     * Aggregates total usage per item.
     *
     * @return a list of usage report entries as strings
     */
    public List<String> generateUsageReport() {
        List<String> report = new ArrayList<>();
        String sql = "SELECT item_name, SUM(quantity) AS total_usage FROM usage_log GROUP BY item_name";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int totalUsage = rs.getInt("total_usage");
                report.add(itemName + ": " + totalUsage);
            }

        } catch (SQLException e) {
            System.out.println("Error generating usage report: " + e.getMessage());
        }

        return report;
    }

    /**
     * Logs the usage of an inventory item.
     * Steps:
     * 1. Check if stock is sufficient.
     * 2. Log the usage in the usage log.
     * 3. Update the inventory to reflect the reduced quantity.
     *
     * @param itemName the name of the item used
     * @param quantityUsed the quantity used
     * @return true if the usage was successfully logged, false otherwise
     */
    public boolean logUsage(String itemName, int quantityUsed) {
        String logSql = "INSERT INTO usage_log (item_name, quantity, usage_date) VALUES (?, ?, CURDATE())";
        String updateSql = "UPDATE inventory_items SET quantity = quantity - ? WHERE item_name = ?";
        String checkStockSql = "SELECT quantity FROM inventory_items WHERE item_name = ?";

        try (Connection conn = DBConnection.getInstance()) {

            // Step 1: Check if stock is sufficient
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
                checkStmt.setString(1, itemName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int currentStock = rs.getInt("quantity");
                    if (currentStock < quantityUsed) {
                        System.out.println("Not enough stock to log usage.");
                        return false;
                    }
                } else {
                    System.out.println("Item not found in inventory.");
                    return false;
                }
            }

            // Step 2: Log usage
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, itemName);
                logStmt.setInt(2, quantityUsed);
                logStmt.executeUpdate();
            }

            // Step 3: Update inventory
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantityUsed);
                updateStmt.setString(2, itemName);
                updateStmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error logging usage: " + e.getMessage());
            return false;
        }
    }

}
