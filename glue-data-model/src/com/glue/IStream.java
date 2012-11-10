package com.glue;

import java.util.List;

public interface IStream {

	/**
	 * Returns the title of this stream.
	 * 
	 * @return a String
	 */
	String getTitle();

	/**
	 * Returns the tags of this stream as a String.
	 * 
	 * @return a String
	 */
	String getDescription();

	/**
	 * Tells whether this stream is public or private.
	 * 
	 * <p>
	 * Only participants and guests can watch a stream.
	 * </p>
	 * 
	 * @return true if public, false otherwise
	 */
	boolean isPublic();

	/**
	 * Tells whether this stream is open or closed for contribution.
	 * 
	 * <p>
	 * Anyone can participate to an open stream, while only invited people can
	 * participate to a closed stream.
	 * </p>
	 * 
	 * @return true if open, false otherwise
	 */
	boolean isOpen();

	/**
	 * Returns a list of the email addresses of this stream's guests.
	 * 
	 * @return a List of String, or an empty list if none.
	 */
	List<String> getGuests();

	/**
	 * Returns a list of the email addresses of the persons invited to
	 * participate to this stream.
	 * 
	 * @return a List of String, or an empty list if none.
	 */
	List<String> getInvitedParticipants();

	/**
	 * Returns the shared secret question.
	 * 
	 * @return a String, or null if none is set
	 */
	String getSharedSecretQuestion();

	/**
	 * Returns the shared secret answer.
	 * 
	 * @return a String, or null if none is set
	 */
	String getSharedSecretAnswer();

	/**
	 * Tells whether or not users must request the administrator of this stream
	 * in order to participate.
	 * 
	 * @return true if users users must request the administrator to
	 *         participate, false otherwise
	 */
	boolean shouldRequestToParticipate();

	/**
	 * Returns the start date of this stream.
	 * 
	 * @return
	 */
	long getStartDate();

	/**
	 * Returns the end date of this stream.
	 * 
	 * @return
	 */
	long getEndDate();

	/**
	 * Returns the latitude of this stream.
	 * 
	 * @returns a double value
	 */
	double getLatitude();

	/**
	 * Returns the longitude of this stream.
	 * 
	 * @returns a double value
	 */
	double getLongitude();

	/**
	 * Returns a string describing the location of this stream.
	 * 
	 * @return a String, or null if none is set
	 */
	String getAddress();
}
