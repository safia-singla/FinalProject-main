package com.hotel.roles;

import com.hotel.models.User;
import com.hotel.views.MainMenuView;

/**
 * Represents the Receptionist role behavior in the system.
 * Receptionists have access to guest reservations, check-ins, and check-outs.
 */
public class ReceptionistRole implements UserRole {

    private final User user;

    /**
     * Constructor for ReceptionistRole.
     *
     * @param user the User object representing the receptionist
     */
    public ReceptionistRole(User user) {
        this.user = user;
    }

    /**
     * Launches the receptionist dashboard view.
     * Opens the main menu interface tailored for receptionist operations.
     */
    @Override
    public void launchDashboard() {
        new MainMenuView(user);
    }
}