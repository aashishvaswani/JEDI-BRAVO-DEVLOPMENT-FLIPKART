package com.flipfit.bean;

import java.time.LocalTime;

/**
 * Represents a specific time slot at a gym center.
 * Handles availability logic for the booking service.
 */
public class GymSlot {
    private String slotId;          // System-managed unique identifier 
    private String centreId;        // ID of the gym center this slot belongs to 
    private LocalTime startTime;    // Start time of the workout 
    private LocalTime endTime;      // End time of the workout 
    private int totalSeats;         // Maximum capacity of the slot 
    private int availableSeats;     // Current number of seats remaining 

    /**
     * Checks if there are any seats available in this slot.
     * @return true if availableSeats > 0 [cite: 33]
     */
    public boolean isAvailable() {
        return availableSeats > 0;
    }

    /**
     * Reduces the number of available seats by one when a booking is confirmed.
     * Logic used by the FlipFitBookingService. [cite: 34, 65]
     */
    public void decreaseAvailability() {
        if (isAvailable()) {
            this.availableSeats--; 
        }
    }

    // Getters and Setters [cite: 1]
    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }

    public String getCentreId() { return centreId; }
    public void setCentreId(String centreId) { this.centreId = centreId; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}