package com.example.ptquy.placepipi;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceData implements Serializable {
	private String placeId;
	private String Name;
	private float Rating;
	private String Address;
	private String phone;
	private String websiteURL;
	private String isOpen;
	private ArrayList<String> week_time;
	private ArrayList<String> type;

	public PlaceData(String placeId, String name, float rating, String address, String phone, String websiteURL
			, String isOpen, ArrayList<String> week_time, ArrayList<String> type) {
		this.placeId = placeId;
		Name = name;
		Rating = rating;
		Address = address;
		this.phone = phone;
		this.websiteURL = websiteURL;
		this.isOpen = isOpen;
		if(week_time != null){
			this.week_time = new ArrayList<>();
			for(int i = 0; i < week_time.size(); i++){
				this.week_time.add(week_time.get(i));
			}
		}
		if(type != null){
			this.type = new ArrayList<>();
			for(int i = 0; i < type.size(); i++){
				this.type.add(type.get(i));
			}
		}
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public float getRating() {
		return Rating;
	}

	public void setRating(float rating) {
		Rating = rating;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public ArrayList<String> getWeek_time() {
		return week_time;
	}

	public void setWeek_time(ArrayList<String> week_time) {
		this.week_time = week_time;
	}

	public ArrayList<String> getType() {
		return type;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}
}