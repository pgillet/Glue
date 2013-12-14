package com.glue.feed.error;

/**
 * Defines the requirements for an object that can be used to send error events
 * to an arbitrary destination.
 * 
 * @author pgillet
 */
public interface ErrorHandler {

	/**
	 * Sends an error event with the specified level.
	 * 
	 * @param lvl
	 *            The error level
	 * @param message
	 *            A string message
	 * @param cause
	 *            A Throwable associated with the string message. May be
	 *            <code>null</code>.
	 * @param source
	 *            The name of the source where the error occurred. May be
	 *            <code>null</code> if none is available.
	 * @param lineNumber
	 *            The line number at which the error event occurred. A negative
	 *            number if undefined.
	 * 
	 * @see ErrorLevel
	 */
	void fireErrorEvent(ErrorLevel lvl, String message, Throwable cause,
			String source, int lineNumber);
}
