package com.hotel.models;

import java.sql.Date;

public class HousekeepingTask {
    private int id;
    private int roomId;
    private String assignedTo;
    private String status; // Task status: Pending, In Progress, Completed
    private Date scheduledDate;

    /**
     * Constructor for HousekeepingTask without an ID (used before database insertion).
     *
     * @param roomId the ID of the room associated with the task
     * @param assignedTo the staff member assigned to the task
     * @param status the current status of the task
     * @param scheduledDate the date the task is scheduled for
     */
    public HousekeepingTask(int roomId, String assignedTo, String status, Date scheduledDate) {
        this.roomId = roomId;
        this.assignedTo = assignedTo;
        this.status = status;
        this.scheduledDate = scheduledDate;
    }

    /**
     * Constructor for HousekeepingTask with an ID (used when retrieving from the database).
     *
     * @param id the unique task ID
     * @param roomId the ID of the room associated with the task
     * @param assignedTo the staff member assigned to the task
     * @param status the current status of the task
     * @param scheduledDate the date the task is scheduled for
     */
    public HousekeepingTask(int id, int roomId, String assignedTo, String status, Date scheduledDate) {
        this(roomId, assignedTo, status, scheduledDate);
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public String getAssignedTo() { return assignedTo; }
    public String getStatus() { return status; }
    public Date getScheduledDate() { return scheduledDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public void setStatus(String status) { this.status = status; }
    public void setScheduledDate(Date scheduledDate) { this.scheduledDate = scheduledDate; }
}
