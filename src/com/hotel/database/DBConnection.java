package com.hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";   // JDBC URL for the hotel database
    private static final String USER = "root";  // Database username
    private static final String PASSWORD = "tanishabansal"; // Database password

    private static Connection connection;   // Singleton instance of the database connection

    // Private constructor to prevent instantiation of this utility class
    private DBConnection() {
        // Prevent instantiation
    }

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
