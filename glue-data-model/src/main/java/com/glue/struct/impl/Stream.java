package com.glue.struct.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.glue.struct.Category;
import com.glue.struct.IStream;
import com.glue.struct.IVenue;

public class Stream implements IStream, Serializable {

	private static final long serialVersionUID = -7751127081320950633L;

	private long id;

	private String title;

	private String description;

	private String url;

	private boolean publicc;

	private boolean open = true;

	private Map<String, String> invitedParticipants = new HashMap<String, String>();

	private String sharedSecretQuestion;

	private String sharedSecretAnswer;

	private boolean shouldRequestToParticipate;

	private long startDate = Calendar.getInstance().getTimeInMillis();

	private long endDate;

	private Set<String> tags;

	private int numberOfParticipant;

	private String thumbPath;

	private IVenue venue;

	private Category category;

	private String price;

	public Stream() {
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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

	public boolean isPublicc() {
		return publicc;
	}

	public void setPublicc(boolean publicc) {
		this.publicc = publicc;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isShouldRequestToParticipate() {
		return shouldRequestToParticipate;
	}

	public void setShouldRequestToParticipate(boolean shouldRequestToParticipate) {
		this.shouldRequestToParticipate = shouldRequestToParticipate;
	}

	@Override
	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public int getNumberOfParticipant() {
		return numberOfParticipant;
	}

	@Override
	public void setNumberOfParticipant(int nbOfParticipant) {
		this.numberOfParticipant = nbOfParticipant;
	}

	@Override
	public String getThumbPath() {
		return thumbPath;
	}

	@Override
	public void setThumbPath(String path) {
		this.thumbPath = path;
	}

	/**
	 * @return the venue
	 */
	@Override
	public IVenue getVenue() {
		return venue;
	}

	/**
	 * @param venue
	 *            the venue to set
	 */
	@Override
	public void setVenue(IVenue venue) {
		this.venue = venue;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public void setCategory(Category category) {
		this.category = category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Stream [title=" + title + ", description=" + description + ", url=" + url + ", publicc=" + publicc
				+ ", open=" + open + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stream other = (Stream) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String getPrice() {
		return price;
	}

	@Override
	public void setPrice(String price) {
		this.price = price;
	}
}
