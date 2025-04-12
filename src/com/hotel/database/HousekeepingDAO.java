package com.hotel.database;

import com.hotel.models.HousekeepingTask;
import com.hotel.models.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingDAO {

    /**
     * Checks if a room is eligible for housekeeping tasks based on its status.
     * Eligible statuses: Available, Maintenance, or Cleaning.
     *
     * @param roomId the ID of the room to check
     * @return true if the room is eligible, false otherwise
     */
    public boolean isRoomEligibleForCleaning(int roomId) {
        String sql = "SELECT status FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                return status.equalsIgnoreCase("Available")
                        || status.equalsIgnoreCase("Maintenance")
                        || status.equalsIgnoreCase("Cleaning");
            }
        } catch (SQLException e) {
            System.out.println("Error checking room status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Adds a new housekeeping task to the database.
     * Checks for duplicate tasks before insertion.
     *
     * @param task the housekeeping task to add
     * @return true if the task was successfully added, false otherwise
     */
    public boolean addTask(HousekeepingTask task) {
        String checkSql = "SELECT COUNT(*) FROM housekeeping_tasks WHERE room_id = ? AND assigned_to = ? AND status = ?";
        String insertSql = "INSERT INTO housekeeping_tasks (room_id, assigned_to, status, scheduled_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check for duplicate tasks
            checkStmt.setInt(1, task.getRoomId());
            checkStmt.setString(2, task.getAssignedTo());
            checkStmt.setString(3, task.getStatus());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("⚠️ Duplicate housekeeping task detected.");
                return false;
            }

            // Insert new housekeeping task
            insertStmt.setInt(1, task.getRoomId());
            insertStmt.setString(2, task.getAssignedTo());
            insertStmt.setString(3, task.getStatus());
            insertStmt.setDate(4, task.getScheduledDate());

            return insertStmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding housekeeping task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the status of a housekeeping task.
     * If the task is completed, updates the corresponding room status to 'Ready'.
     *
     * @param taskId    the ID of the task to update
     * @param newStatus the new status to set
     * @return true if the update was successful, false otherwise
     */
    public boolean updateStatus(int taskId, String newStatus) {
        String sql = "UPDATE housekeeping_tasks SET status = ? WHERE id = ?";
        String roomStatusUpdateSql = "UPDATE rooms SET status = 'Ready' WHERE id = (SELECT room_id FROM housekeeping_tasks WHERE id = ?)";

        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement roomStmt = conn.prepareStatement(roomStatusUpdateSql)) {

            // Update task status
            stmt.setString(1, newStatus);
            stmt.setInt(2, taskId);
            int updated = stmt.executeUpdate();

            // If task completed, update room status
            if (updated > 0 && newStatus.equalsIgnoreCase("Completed")) {
                roomStmt.setInt(1, taskId); // Update room to 'Ready'
                roomStmt.executeUpdate();
                System.out.println("✅ Task completed. Room status set to Ready.");
            }

            return updated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all housekeeping tasks from the database.
     *
     * @return a list of all housekeeping tasks
     */
    public List<HousekeepingTask> getAllTasks() {
        List<HousekeepingTask> list = new ArrayList<>();
        String sql = "SELECT * FROM housekeeping_tasks";

        try (Connection conn = DBConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HousekeepingTask task = new HousekeepingTask(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getString("assigned_to"),
                        rs.getString("status"),
                        rs.getDate("scheduled_date")
                );
                list.add(task);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }

        return list;
    }

    /**
     * Retrieves a specific housekeeping task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return the corresponding HousekeepingTask object, or null if not found
     */
    public HousekeepingTask getTaskById(int taskId) {
        String sql = "SELECT * FROM housekeeping_tasks WHERE id = ?";
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new HousekeepingTask(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getString("assigned_to"),
                        rs.getString("status"),
                        rs.getDate("scheduled_date")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching task by ID: " + e.getMessage());
        }
        return null;
    }
}
