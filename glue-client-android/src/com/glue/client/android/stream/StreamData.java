package com.glue.client.android.stream;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.glue.struct.IStream;

public class StreamData implements IStream {

	private long id;

	private String title;

	private boolean publicc;

	private boolean open = true;

	private Set<String> tags;

	private Map<String, String> invitedParticipants = new HashMap<String, String>();

	private String sharedSecretQuestion;

	private String sharedSecretAnswer;

	private boolean shouldRequestToParticipate;

	private long startDate = new Date().getTime();

	private long endDate;

	private double latitude;

	private double longitude;

	private String address;

	private int nbOfParticipant;

	private static StreamData instance = new StreamData();

	private StreamData() {
	}

	public static StreamData getInstance() {
		return instance;
	}

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
	 * @return the mPublic
	 */
	public boolean isPublicc() {
		return publicc;
	}

	/**
	 * @param mPublic
	 *            the mPublic to set
	 */
	public void setPublicc(boolean mPublic) {
		this.publicc = mPublic;
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
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * @param guests
	 *            the guests to set
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
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

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int getNumberOfParticipant() {
		return nbOfParticipant;
	}

	@Override
	public void setNumberOfParticipant(int nbOfParticipant) {
		this.nbOfParticipant = nbOfParticipant;
	}

}
