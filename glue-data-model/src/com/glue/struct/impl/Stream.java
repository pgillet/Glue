package com.glue.struct.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.glue.struct.IStream;

public class Stream implements IStream {

	private String title;

	private String description;

	private boolean publicc;

	private boolean open = true;

	private Set<String> guests;

	private Map<String, String> invitedParticipants = new HashMap<String, String>();

	private String sharedSecretQuestion;

	private String sharedSecretAnswer;

	private boolean shouldRequestToParticipate;

	private long startDate = Calendar.getInstance().getTimeInMillis();

	private long endDate;

	private double latitude;

	private double longitude;

	private String address;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the mPublic
	 */
	public boolean isPublicc() {
		return publicc;
	}

	/**
	 * @param mPublic
	 *            the mPublic to set
	 */
	public void setPublicc(boolean publicc) {
		this.publicc = publicc;
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/**
	 * @return the guests
	 */
	public Set<String> getGuests() {
		return guests;
	}

	/**
	 * @param guests
	 *            the guests to set
	 */
	public void setGuests(Set<String> guests) {
		this.guests = guests;
	}

	/**
	 * @return the invitedParticipants
	 */
	public Map<String, String> getInvitedParticipants() {
		return invitedParticipants;
	}

	/**
	 * @param invitedParticipants
	 *            the invitedParticipants to set
	 */
	public void setInvitedParticipants(Map<String, String> invitedParticipants) {
		this.invitedParticipants = invitedParticipants;
	}

	/**
	 * @return the sharedSecretQuestion
	 */
	public String getSharedSecretQuestion() {
		return sharedSecretQuestion;
	}

	/**
	 * @param sharedSecretQuestion
	 *            the sharedSecretQuestion to set
	 */
	public void setSharedSecretQuestion(String sharedSecretQuestion) {
		this.sharedSecretQuestion = sharedSecretQuestion;
	}

	/**
	 * @return the sharedSecretAnswer
	 */
	public String getSharedSecretAnswer() {
		return sharedSecretAnswer;
	}

	/**
	 * @param sharedSecretAnswer
	 *            the sharedSecretAnswer to set
	 */
	public void setSharedSecretAnswer(String sharedSecretAnswer) {
		this.sharedSecretAnswer = sharedSecretAnswer;
	}

	/**
	 * @return the shouldRequestToParticipate
	 */
	public boolean isShouldRequestToParticipate() {
		return shouldRequestToParticipate;
	}

	/**
	 * @param shouldRequestToParticipate
	 *            the shouldRequestToParticipate to set
	 */
	public void setShouldRequestToParticipate(boolean shouldRequestToParticipate) {
		this.shouldRequestToParticipate = shouldRequestToParticipate;
	}

	/**
	 * @return the startDate
	 */
	public long getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public long getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
