package com.glue.struct;

import java.util.Map;
import java.util.Set;

public interface IStream {

	long getId();

	void setId(long id);

	/**
	 * Returns the title of this stream.
	 * 
	 * @return a String
	 */
	String getTitle();

	void setTitle(String title);

	/**
	 * Tells whether this stream is public or private. Private by default.
	 * 
	 * <p>
	 * Only participants and guests can watch a private stream.
	 * </p>
	 * 
	 * @return true if public, false otherwise
	 */
	boolean isPublicc();

	void setPublicc(boolean publicc);

	/**
	 * Tells whether this stream is open or closed for contribution. Open by
	 * default.
	 * 
	 * <p>
	 * Anyone can participate to an open stream, while only invited people can
	 * participate to a closed stream.
	 * </p>
	 * 
	 * @return true if open, false otherwise
	 */
	boolean isOpen();

	void setOpen(boolean open);

	/**
	 * Returns a a set of tags.
	 * 
	 * @return a Set of tags.
	 */
	Set<String> getTags();

	void setTags(Set<String> tags);

	/**
	 * Returns a map that maps the email addresses of the persons invited to
	 * participate to this stream to their names.
	 * 
	 * @return a Map that maps Strings to Strings, or an empty Map if none.
	 */
	Map<String, String> getInvitedParticipants();

	void setInvitedParticipants(Map<String, String> ip);

	/**
	 * Returns the shared secret question.
	 * 
	 * @return a String, or null if none is set
	 */
	String getSharedSecretQuestion();

	void setSharedSecretQuestion(String question);

	/**
	 * Returns the shared secret answer.
	 * 
	 * @return a String, or null if none is set
	 */
	String getSharedSecretAnswer();

	void setSharedSecretAnswer(String answer);

	/**
	 * Tells whether or not users must request the administrator of this stream
	 * in order to participate.
	 * 
	 * @return true if users users must request the administrator to
	 *         participate, false otherwise
	 */
	boolean isShouldRequestToParticipate();

	void setShouldRequestToParticipate(boolean request);

	/**
	 * Returns the start date of this stream. Should be the current date and
	 * time by default.
	 * 
	 * @return
	 */
	long getStartDate();

	void setStartDate(long date);

	/**
	 * Returns the end date of this stream.
	 * 
	 * @return
	 */
	long getEndDate();

	void setEndDate(long date);

	/**
	 * Returns the latitude of this stream.
	 * 
	 * @returns a double value
	 */
	double getLatitude();

	void setLatitude(double latitude);

	/**
	 * Returns the longitude of this stream.
	 * 
	 * @returns a double value
	 */
	double getLongitude();

	void setLongitude(double longitude);

	/**
	 * Returns a string describing the location of this stream.
	 * 
	 * @return a String, or null if none is set
	 */
	String getAddress();

	void setAddress(String adress);

	/**
	 * Returns the number of participant of this stream.
	 * 
	 * @return the number of participant
	 */
	int getNumberOfParticipant();

	void setNumberOfParticipant(int nbOfParticipant);

	/**
	 * Returns the path to the thumbnail of the stream
	 * 
	 * @return the path of the thumbnail
	 */
	String getThumbPath();

	void setThumbPath(String path);
}
