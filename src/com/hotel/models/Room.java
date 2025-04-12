package com.hotel.models;

public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private String status; // Room status: Available, Occupied, Maintenance

    /**
     * Constructor for Room without an ID (used before saving to database).
     *
     * @param roomNumber the room number
     * @param type the type of the room (e.g., Single, Double, Suite)
     * @param status the current status of the room
     */
    public Room(String roomNumber, String type, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.status = status;
    }

    /**
     * Constructor for Room with an ID (used when retrieving from database).
     *
     * @param id the unique identifier of the room
     * @param roomNumber the room number
     * @param type the type of the room (e.g., Single, Double, Suite)
     * @param status the current status of the room
     */
    public Room(int id, String roomNumber, String type, String status) {
        this(roomNumber, type, status);
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
}
