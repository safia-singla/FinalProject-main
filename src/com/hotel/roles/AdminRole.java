package com.hotel.roles;

import com.hotel.models.User;
import com.hotel.views.MainMenuView;

/**
 * Represents the Admin role behavior in the system.
 * Admin users have access to the main dashboard and administrative functions.
 */
public class AdminRole implements UserRole {

    private final User user;

    /**
     * Constructor for AdminRole.
     *
     * @param user the User object representing the admin
     */
    public AdminRole(User user) {
        this.user = user;
    }

    /**
     * Launches the admin dashboard view.
     * Opens the main menu interface tailored for the admin user.
     */
    @Override
    public void launchDashboard() {
        new MainMenuView(user);
    }
}
