package com.glue.struct.impl;

import java.io.Serializable;

import com.glue.struct.IVenue;

public class Venue implements IVenue, Serializable {
	
	private Long id;
	
	private String name;
	
	private double latitude;

	private double longitude;

	private String address;
	
	private String url;

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the latitude
	 */
	@Override
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	@Override
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	@Override
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	@Override
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	@Override
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the url
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

}
