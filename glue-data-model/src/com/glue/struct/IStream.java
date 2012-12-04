package com.glue.struct;

import java.util.Map;

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
	 * Tells whether this stream is public or private. Private by default.
	 * 
	 * <p>
	 * Only participants and guests can watch a private stream.
	 * </p>
	 * 
	 * @return true if public, false otherwise
	 */
	boolean isPublicc();

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

	/**
	 * Returns a map that maps the email addresses of the persons invited to
	 * participate to this stream to their names.
	 * 
	 * @return a Map that maps Strings to Strings, or an empty Map if none.
	 */
	Map<String, String> getInvitedParticipants();

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
	boolean isShouldRequestToParticipate();

	/**
	 * Returns the start date of this stream. Should be the current date and
	 * time by default.
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
