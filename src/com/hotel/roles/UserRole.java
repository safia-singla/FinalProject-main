package com.hotel.roles;

/**
 * UserRole interface defines the contract for different user roles in the system.
 * Each role must implement the method to launch their respective dashboards.
 */
public interface UserRole {

    /**
     * Launches the dashboard associated with the user role.
     */
    void launchDashboard();
}
