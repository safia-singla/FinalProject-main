package com.hotel.models;

public class Service {
    private String serviceName;
    private double price;  // Field for service price

    /**
     * Constructor with service name and price.
     *
     * @param serviceName the name of the service (e.g., Spa, Laundry)
     * @param price the price of the service
     */
    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    /**
     * Constructor with only service name.
     * Defaults the price to 0.0.
     *
     * @param serviceName the name of the service
     */
    public Service(String serviceName) {
        this(serviceName, 0.0); // Default price as 0.0 if not provided
    }

    // Getters
    public String getServiceName() { return serviceName; }
    public double getPrice() { return price; }

    // Setters
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setPrice(double price) { this.price = price; }
}
