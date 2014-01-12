package com.glue.feed.nominatim;

import com.glue.struct.IVenue;

public class NominatimVenueAdapter implements IVenue {

	private NominatimVenue venue;
	
	public NominatimVenueAdapter(NominatimVenue venue) {
		this.venue = venue;
	}
	
	@Override
	public void setId(Long id) {
	}

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setUrl(String url) {
	}

	@Override
	public String getUrl() {
		return null;
	}

	@Override
	public void setAddress(String address) {
	}

	@Override
	public String getAddress() {
		return venue.getAddress().getFull();
	}

	@Override
	public void setLongitude(double longitude) {
	}

	@Override
	public double getLongitude() {
		return venue.getLon();
	}

	@Override
	public void setLatitude(double latitude) {	
	}

	@Override
	public double getLatitude() {
		return venue.getLat();
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public String getName() {
		return venue.getName();
	}

	@Override
	public void setCity(String city) {		
	}

	@Override
	public String getCity() {
		return venue.getAddress().getCity();
	}

	@Override
	public String toString() {
		return "Venue [adress=" + getAddress() + ", long=" + getLongitude() + ", lat=" + getLatitude() + ", name="
				+ getName() + ", city=" + getCity() + "]";
	}
	
	

}
