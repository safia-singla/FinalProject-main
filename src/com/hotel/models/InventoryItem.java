package com.hotel.models;

public class InventoryItem {
    private int id;
    private String itemName;
    private int quantity;
    private int threshold;
    private String department; // Field to categorize the item by department

    /**
     * Constructor with all fields including department.
     *
     * @param id the unique identifier for the inventory item
     * @param itemName the name of the inventory item
     * @param quantity the current quantity in stock
     * @param threshold the threshold quantity for alerts
     * @param department the department to which the item belongs
     */
    public InventoryItem(int id, String itemName, int quantity, int threshold, String department) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.threshold = threshold;
        this.department = department;
    }

    /**
     * Constructor without ID (used before the item is saved in the database).
     *
     * @param itemName the name of the inventory item
     * @param quantity the current quantity in stock
     * @param threshold the threshold quantity for alerts
     * @param department the department to which the item belongs
     */
    public InventoryItem(String itemName, int quantity, int threshold, String department) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.threshold = threshold;
        this.department = department;
    }

    /**
     * Constructor for backward compatibility (without department field).
     * Assigns default department as "Uncategorized".
     *
     * @param id the unique identifier for the inventory item
     * @param itemName the name of the inventory item
     * @param quantity the current quantity in stock
     * @param threshold the threshold quantity for alerts
     */
    public InventoryItem(int id, String itemName, int quantity, int threshold) {
        this(itemName, quantity, threshold, "Uncategorized");
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public int getThreshold() { return threshold; }
    public String getDepartment() { return department; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setThreshold(int threshold) { this.threshold = threshold; }
    public void setDepartment(String department) { this.department = department; }
}
