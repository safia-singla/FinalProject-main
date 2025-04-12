package com.hotel.roles;

import com.hotel.models.User;

/**
 * Factory class to create instances of UserRole based on the user's role.
 * Helps in decoupling the role creation logic from the rest of the application.
 */
public class UserRoleFactory {

    /**
     * Returns the appropriate UserRole implementation based on the user's role.
     *
     * @param user the User object whose role needs to be resolved
     * @return an instance of UserRole matching the user's role, or null if no match is found
     */
    public static UserRole getRole(User user) {
        switch (user.getRole().toLowerCase()) {
            case "admin":
                return new AdminRole(user);
            case "housekeeping":
                return new HousekeepingRole(user);
            case "receptionist":
                return new ReceptionistRole(user);
            default:
                return null;
        }
    }
}
