package com.hotel.database;

import com.hotel.models.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    /**
     * Adds a new room to the database.
     *
     * @param room the Room object containing room details
     * @return true if the room was successfully added, false otherwise
     */
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, type, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getType());
            stmt.setString(3, room.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all rooms from the database.
     *
     * @return a list of all Room objects
     */
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("status")
                );
                list.add(room);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching rooms: " + e.getMessage());
        }

        return list;
    }

    /**
     * Retrieves the first available room from the database.
     *
     * @return an available Room object, or null if none found
     */
    public Room getAvailableRoom() {
        String sql = "SELECT * FROM rooms WHERE status = 'Available' LIMIT 1";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching available room: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves a room by its ID.
     * Commonly used for housekeeping purposes.
     *
     * @param id the room ID
     * @return the Room object, or null if not found
     */
    public Room getRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching room by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves an available room of a specific type.
     *
     * @param type the desired room type
     * @return a matching Room object, or null if none found
     */
    public Room getAvailableRoomByType(String type) {
        String sql = "SELECT * FROM rooms WHERE type = ? AND status = 'Available' LIMIT 1";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching room by type: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates the status of a specific room.
     *
     * @param roomId the room ID
     * @param newStatus the new status to assign
     * @return true if the update was successful, false otherwise
     */
    public boolean updateRoomStatus(int roomId, String newStatus) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, roomId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating room status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the status of a specific room to 'Occupied'.
     *
     * @param roomId the room ID
     */
    public void updateRoomStatusToOccupied(int roomId) {
        updateRoomStatus(roomId, "Occupied");
    }

    /**
     * Checks if a room ID exists in the database.
     *
     * @param roomId the room ID to check
     * @return true if the room exists, false otherwise
     */
    public boolean roomIdExists(int roomId) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE id = ?";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking room ID: " + e.getMessage());
        }

        return false;
    }

    /**
     * Updates the status of a specific room to 'Available'.
     * Typically used during check-out process.
     *
     * @param roomId the room ID
     */
    public void updateRoomStatusToAvailable(int roomId) {
        updateRoomStatus(roomId, "Available");
    }

    /**
     * Retrieves a room by its room number.
     * Useful for reverse lookup during check-out.
     *
     * @param roomNumber the room number to search
     * @return the Room object, or null if not found
     */
    public Room getRoomByNumber(String roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching room by number: " + e.getMessage());
        }

        return null;
    }
}
