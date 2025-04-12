package com.hotel.database;

import com.hotel.models.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    /**
     * Adds a new reservation to the database.
     * Includes fields for late checkout and group name.
     *
     * @param res the reservation object to add
     * @return true if the reservation was successfully added, false otherwise
     */
    public boolean addReservation(Reservation res) {
        String sql = "INSERT INTO reservations (guest_name, check_in, check_out, room_type, payment_status, special_requests, room_number, group_name, late_checkout) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, res.getGuestName());
            stmt.setDate(2, res.getCheckIn());
            stmt.setDate(3, res.getCheckOut());
            stmt.setString(4, res.getRoomType());
            stmt.setString(5, res.getPaymentStatus());
            stmt.setString(6, res.getSpecialRequests());
            stmt.setString(7, res.getRoomNumber());
            stmt.setString(8, res.getGroupName());
            stmt.setBoolean(9, res.isLateCheckout());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting reservation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves distinct guest names for individual reservations (excluding group bookings).
     *
     * @return a list of individual guest names
     */
    public List<String> getOnlyIndividualGuestNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT DISTINCT guest_name FROM reservations WHERE group_name IS NULL ORDER BY guest_name";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                names.add(rs.getString("guest_name"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching individual guest names: " + e.getMessage());
        }

        return names;
    }

    /**
     * Checks if a reservation already exists for a guest within the specified dates.
     *
     * @param guestName the name of the guest
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @return true if reservation exists, false otherwise
     */
    public boolean reservationExists(String guestName, Date checkIn, Date checkOut) {
        String query = "SELECT COUNT(*) FROM reservations WHERE guest_name = ? AND check_in = ? AND check_out = ?";
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, guestName);
            stmt.setDate(2, checkIn);
            stmt.setDate(3, checkOut);

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error checking reservation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing reservation in the database.
     *
     * @param res the reservation object with updated details
     * @return true if the update was successful, false otherwise
     */
    public boolean updateReservation(Reservation res) {
        String sql = "UPDATE reservations SET guest_name=?, check_in=?, check_out=?, room_type=?, payment_status=?, special_requests=?, group_name=?, late_checkout=? WHERE id=?";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, res.getGuestName());
            stmt.setDate(2, res.getCheckIn());
            stmt.setDate(3, res.getCheckOut());
            stmt.setString(4, res.getRoomType());
            stmt.setString(5, res.getPaymentStatus());
            stmt.setString(6, res.getSpecialRequests());
            stmt.setString(7, res.getGroupName());
            stmt.setBoolean(8, res.isLateCheckout());
            stmt.setInt(9, res.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating reservation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a reservation from the database by its ID.
     *
     * @param id the reservation ID
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReservationById(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all distinct guest names from the reservations.
     *
     * @return a list of guest names
     */
    public List<String> getAllGuestNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT DISTINCT guest_name FROM reservations ORDER BY guest_name";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                names.add(rs.getString("guest_name"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching guest names: " + e.getMessage());
        }

        return names;
    }

    /**
     * Retrieves all reservations from the database.
     *
     * @return a list of reservations
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reservation res = new Reservation(
                        rs.getInt("id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getString("room_type"),
                        rs.getString("payment_status"),
                        rs.getString("special_requests"),
                        rs.getString("room_number"),
                        rs.getString("group_name"),
                        rs.getBoolean("late_checkout")
                );
                list.add(res);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching reservations: " + e.getMessage());
        }

        return list;
    }

     /**
     * Retrieves the most recent reservation for a specific guest by name.
     *
     * @param guestName the name of the guest
     * @return the most recent reservation for the guest, or null if not found
     */
    public Reservation getReservationByGuestName(String guestName) {
        String sql = "SELECT * FROM reservations WHERE guest_name = ? ORDER BY check_in DESC LIMIT 1";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, guestName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reservation(
                        rs.getInt("id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getString("room_type"),
                        rs.getString("payment_status"),
                        rs.getString("special_requests"),
                        rs.getString("room_number"),
                        rs.getString("group_name"),
                        rs.getBoolean("late_checkout")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching reservation by guest name: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves all reservations under a specific group name.
     *
     * @param groupName the group name
     * @return a list of reservations in the group
     */
    public List<Reservation> getReservationsByGroup(String groupName) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE group_name = ?";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation res = new Reservation(
                        rs.getInt("id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getString("room_type"),
                        rs.getString("payment_status"),
                        rs.getString("special_requests"),
                        rs.getString("room_number"),
                        rs.getString("group_name"),
                        rs.getBoolean("late_checkout")
                );
                list.add(res);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching group reservations: " + e.getMessage());
        }

        return list;
    }

    /**
     * Retrieves all distinct group names from reservations.
     *
     * @return a list of group names
     */
    public List<String> getAllGroupNames() {
        List<String> groupNames = new ArrayList<>();
        String sql = "SELECT DISTINCT group_name FROM reservations WHERE group_name IS NOT NULL";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                groupNames.add(rs.getString("group_name"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching group names: " + e.getMessage());
        }

        return groupNames;
    }
}
