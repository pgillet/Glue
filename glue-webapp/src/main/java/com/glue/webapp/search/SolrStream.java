package com.glue.webapp.search;

import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.beans.Field;

import com.glue.struct.IStream;
import com.glue.struct.IVenue;

/**
 * A technical implementation of a Stream that adds @Field annotation allowing
 * to read Documents returned by Solr directly as beans.
 * 
 * @author pgillet
 * 
 */
public class SolrStream implements IStream {

	@Field
	private long id;

	@Field
	private String title;

	@Field
	private String description;

	private String url;

	private boolean publicc;

	private boolean open;

	private Map<String, String> invitedParticipants;

	private String sharedSecretQuestion;

	private String sharedSecretAnswer;

	private boolean shouldRequestToParticipate;

	private long startDate;

	private long endDate;

	private Set<String> tags;

	private int numberOfParticipant;

	private String thumbPath;

	private IVenue venue;

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
	 * @return the publicc
	 */
	public boolean isPublicc() {
		return publicc;
	}

	/**
	 * @param publicc
	 *            the publicc to set
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
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the numberOfParticipant
	 */
	public int getNumberOfParticipant() {
		return numberOfParticipant;
	}

	/**
	 * @param numberOfParticipant
	 *            the numberOfParticipant to set
	 */
	public void setNumberOfParticipant(int numberOfParticipant) {
		this.numberOfParticipant = numberOfParticipant;
	}

	/**
	 * @return the thumbPath
	 */
	public String getThumbPath() {
		return thumbPath;
	}

	/**
	 * @param thumbPath
	 *            the thumbPath to set
	 */
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	/**
	 * @return the venue
	 */
	public IVenue getVenue() {
		return venue;
	}

	/**
	 * @param venue
	 *            the venue to set
	 */
	public void setVenue(IVenue venue) {
		this.venue = venue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SolrStream [id=" + id + ", title=" + title + ", description="
				+ description + ", url=" + url + ", publicc=" + publicc
				+ ", open=" + open + ", invitedParticipants="
				+ invitedParticipants + ", sharedSecretQuestion="
				+ sharedSecretQuestion + ", sharedSecretAnswer="
				+ sharedSecretAnswer + ", shouldRequestToParticipate="
				+ shouldRequestToParticipate + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", tags=" + tags
				+ ", numberOfParticipant=" + numberOfParticipant
				+ ", thumbPath=" + thumbPath + ", venue=" + venue + "]";
	}

}
