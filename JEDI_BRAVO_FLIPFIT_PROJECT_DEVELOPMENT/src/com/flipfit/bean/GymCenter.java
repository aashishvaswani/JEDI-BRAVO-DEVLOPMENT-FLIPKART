package com.flipfit.bean;

import java.util.List;

public class GymCenter {
    private String centreId;    // maps to source [cite: 20]
    private String city;        // maps to source [cite: 21]
    private String location;    // maps to source [cite: 23]
    private List<GymSlot> slots; // maps to source [cite: 24, 25]

    // Getters and Setters
    public String getCentreId() { return centreId; }
    public void setCentreId(String centreId) { this.centreId = centreId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<GymSlot> getSlots() { return slots; }
    public void setSlots(List<GymSlot> slots) { this.slots = slots; }
}