package com.hotel.models;

import java.sql.Date;

public class Reservation {
    private int id;
    private String guestName;
    private Date checkIn;
    private Date checkOut;
    private String roomType;
    private String paymentStatus;
    private String specialRequests;
    private String roomNumber;
    private String groupName;
    private boolean lateCheckout; // Field to indicate if late checkout is requested

    /**
     * Constructor for Reservation without ID (used before database insertion).
     *
     * @param guestName the guest's name
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @param roomType the type of room booked
     * @param paymentStatus the payment status of the reservation
     * @param specialRequests any special requests by the guest
     * @param roomNumber the allocated room number
     * @param groupName the group name if part of a group booking
     * @param lateCheckout indicates if late checkout is requested
     */
    public Reservation(String guestName, Date checkIn, Date checkOut, String roomType,
                       String paymentStatus, String specialRequests, String roomNumber,
                       String groupName, boolean lateCheckout) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomType = roomType;
        this.paymentStatus = paymentStatus;
        this.specialRequests = specialRequests;
        this.roomNumber = roomNumber;
        this.groupName = groupName;
        this.lateCheckout = lateCheckout;
    }

    /**
     * Constructor for Reservation with ID (used when retrieving from database).
     *
     * @param id the reservation ID
     * @param guestName the guest's name
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @param roomType the type of room booked
     * @param paymentStatus the payment status of the reservation
     * @param specialRequests any special requests by the guest
     * @param roomNumber the allocated room number
     * @param groupName the group name if part of a group booking
     * @param lateCheckout indicates if late checkout is requested
     */
    public Reservation(int id, String guestName, Date checkIn, Date checkOut, String roomType,
                       String paymentStatus, String specialRequests, String roomNumber,
                       String groupName, boolean lateCheckout) {
        this(guestName, checkIn, checkOut, roomType, paymentStatus, specialRequests, roomNumber, groupName, lateCheckout);
        this.id = id;
    }

    /**
     * Constructor without groupName or lateCheckout (for backward compatibility).
     *
     * @param guestName the guest's name
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @param roomType the type of room booked
     * @param paymentStatus the payment status of the reservation
     * @param specialRequests any special requests by the guest
     * @param roomNumber the allocated room number
     */
    public Reservation(String guestName, Date checkIn, Date checkOut, String roomType,
                       String paymentStatus, String specialRequests, String roomNumber) {
        this(guestName, checkIn, checkOut, roomType, paymentStatus, specialRequests, roomNumber, null, false);
    }

    /**
     * Constructor with ID, without groupName or lateCheckout (for backward compatibility).
     *
     * @param id the reservation ID
     * @param guestName the guest's name
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @param roomType the type of room booked
     * @param paymentStatus the payment status of the reservation
     * @param specialRequests any special requests by the guest
     * @param roomNumber the allocated room number
     */
    public Reservation(int id, String guestName, Date checkIn, Date checkOut, String roomType,
                       String paymentStatus, String specialRequests, String roomNumber) {
        this(guestName, checkIn, checkOut, roomType, paymentStatus, specialRequests, roomNumber, null, false);
        this.id = id;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public Date getCheckIn() { return checkIn; }
    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public boolean isLateCheckout() { return lateCheckout; }
    public void setLateCheckout(boolean lateCheckout) { this.lateCheckout = lateCheckout; }
}
