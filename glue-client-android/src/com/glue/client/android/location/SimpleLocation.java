package com.glue.client.android.location;

public class SimpleLocation {

	private String addressText;

	private double latitude;

	private double longitude;

	public String getAddressText() {
		return addressText;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
