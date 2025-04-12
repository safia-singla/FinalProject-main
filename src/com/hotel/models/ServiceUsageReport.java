package com.hotel.models;

public class ServiceUsageReport {
    private String serviceName;
    private int usageCount;
    private double totalRevenue;

    /**
     * Constructor for ServiceUsageReport.
     *
     * @param serviceName the name of the service (e.g., Spa, Dining)
     * @param usageCount the number of times the service has been used
     * @param totalRevenue the total revenue generated from the service
     */
    public ServiceUsageReport(String serviceName, int usageCount, double totalRevenue) {
        this.serviceName = serviceName;
        this.usageCount = usageCount;
        this.totalRevenue = totalRevenue;
    }

    // Getters
    public String getServiceName() { return serviceName; }
    public int getUsageCount() { return usageCount; }
    public double getTotalRevenue() { return totalRevenue; }

    // Setters
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}
