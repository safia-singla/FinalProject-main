package com.hotel.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    /**
     * Constructor for User.
     *
     * @param id the unique identifier for the user
     * @param username the username of the user (used for login)
     * @param password the password of the user (used for authentication)
     * @param role the role of the user (e.g., admin, staff)
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}