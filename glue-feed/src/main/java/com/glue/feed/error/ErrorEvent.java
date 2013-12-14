package com.glue.feed.error;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The <code>ErrorEvent</code> class is used to provide contextual information
 * about an error event to the handler processing the event.
 * 
 * @author pgillet
 */
public class ErrorEvent {

	/** The source locator. */
	private SourceLocator locator;

	/** The detail message. */
	private String message;

	/** The Throwable associated with this error event. */
	private Throwable cause;

	/**
	 * Creates a new ErrorEvent with the specified message, locator and standard
	 * reference.
	 * 
	 * @param message
	 *            a string message
	 * @param cause
	 *            the Throwable associated with this error event
	 * @param locator
	 *            The locator object for the error or warning. May be
	 *            <code>null</code>.
	 */
	public ErrorEvent(String message, Throwable cause, SourceLocator locator) {
		this.message = message;
		this.cause = cause;
		setLocator(locator);
	}

	/**
	 * Returns the detail message.
	 * 
	 * @return the detail message.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @return the cause
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * Sets an instance of a SourceLocator object that specifies where an error
	 * occured.
	 * 
	 * @param locator
	 *            A SourceLocator object, or<code>null</code> to clear the
	 *            location
	 */
	public void setLocator(SourceLocator locator) {
		this.locator = locator;
	}

	/**
	 * Retrieves an instance of a SourceLocator object that specifies where an
	 * error occured.
	 * 
	 * @return A SourceLocator object, or <code>null</code> if none was
	 *         specified.
	 */
	public SourceLocator getLocator() {
		return locator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if (locator != null) {
			sb.append("Error at line ").append(locator.getLineNumber())
					.append(" in ").append(locator.getName()).append(":\n");
		}
		sb.append(message).append("\n");
		if (cause != null) {
			StringWriter sw = new StringWriter();
			cause.printStackTrace(new PrintWriter(sw, true));
			sb.append(sw.getBuffer());
		}

		return sb.toString();
	}
}
