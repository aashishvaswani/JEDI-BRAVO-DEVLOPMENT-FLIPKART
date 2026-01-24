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

	private String centerId;
	private String centerLocn;
	private String centerCity;
	private List<GymSlot> centerSlot;
	
	public String getCenterId() {
		return centerId;
	}
	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	public String getCenterLocn() {
		return centerLocn;
	}
	public void setCenterLocn(String centerLocn) {
		this.centerLocn = centerLocn;
	}
	public String getCenterCity() {
		return centerCity;
	}
	public void setCenterCity(String centerCity) {
		this.centerCity = centerCity;
	}
	public List<GymSlot> getCenterSlot() {
		return centerSlot;
	}
	public void setCenterSlot(List<GymSlot> centerSlot) {
		this.centerSlot = centerSlot;
	}
}
