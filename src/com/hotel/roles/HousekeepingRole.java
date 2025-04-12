package com.hotel.roles;

import com.hotel.models.User;
import com.hotel.views.MainMenuView;

/**
 * Represents the Housekeeping role behavior in the system.
 * Housekeeping users have access to features related to room maintenance and tasks.
 */
public class HousekeepingRole implements UserRole {

    private final User user;

    /**
     * Constructor for HousekeepingRole.
     *
     * @param user the User object representing the housekeeping staff
     */
    public HousekeepingRole(User user) {
        this.user = user;
    }

    /**
     * Launches the housekeeping dashboard view.
     * Opens the main menu interface tailored for housekeeping staff.
     */
    @Override
    public void launchDashboard() {
        new MainMenuView(user);
    }
}
