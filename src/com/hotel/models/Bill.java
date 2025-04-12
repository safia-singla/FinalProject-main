package com.hotel.models;

import java.util.List;

public class Bill {
    private int id;
    private String guestName;
    private String roomType;
    private int nights;
    private List<Service> services;  // List of services associated with the bill
    private double baseCharge;
    private double tax;
    private double discount;
    private double totalAmount;

    /**
     * Constructor for Bill without an ID (used when creating a new bill before persisting to the database).
     *
     * @param guestName the name of the guest
     * @param roomType the type of room booked
     * @param nights the number of nights stayed
     * @param services the list of services availed
     * @param baseCharge the base room charge
     * @param tax the applicable tax amount
     * @param discount the discount applied
     * @param totalAmount the total bill amount
     */
    public Bill(String guestName, String roomType, int nights, List<Service> services,
                double baseCharge, double tax, double discount, double totalAmount) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.services = services;
        this.baseCharge = baseCharge;
        this.tax = tax;
        this.discount = discount;
        this.totalAmount = totalAmount;
    }

    /**
     * Constructor for Bill with an ID (used when retrieving a bill from the database).
     *
     * @param id the bill ID
     * @param guestName the name of the guest
     * @param roomType the type of room booked
     * @param nights the number of nights stayed
     * @param services the list of services availed
     * @param baseCharge the base room charge
     * @param tax the applicable tax amount
     * @param discount the discount applied
     * @param totalAmount the total bill amount
     */
    public Bill(int id, String guestName, String roomType, int nights, List<Service> services,
                double baseCharge, double tax, double discount, double totalAmount) {
        this(guestName, roomType, nights, services, baseCharge, tax, discount, totalAmount);
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public List<Service> getServices() { return services; }
    public double getBaseCharge() { return baseCharge; }
    public double getTax() { return tax; }
    public double getDiscount() { return discount; }
    public double getTotalAmount() { return totalAmount; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setNights(int nights) { this.nights = nights; }
    public void setServices(List<Service> services) { this.services = services; }
    public void setBaseCharge(double baseCharge) { this.baseCharge = baseCharge; }
    public void setTax(double tax) { this.tax = tax; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
