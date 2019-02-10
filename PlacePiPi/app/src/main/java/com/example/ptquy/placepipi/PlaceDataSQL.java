package com.example.ptquy.placepipi;

public class PlaceDataSQL {
	private String ID;
	private String name;
	private String address;
	private float rating;

	public PlaceDataSQL(String ID, String name, String address, float rating) {
		this.ID = ID;
		this.name = name;
		this.address = address;
		this.rating = rating;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
}
