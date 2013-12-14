package com.glue.feed.error;

/**
 * This interface is primarily for the purposes of reporting where an error
 * occurred in an input source.
 * 
 * @author pgillet
 */
public class SourceLocator {

	/** The source name. */
	private String name;

	/**
	 * the line number at which the error event occurred.
	 */
	private int lineNumber = -1;

	/**
	 * Creates a <code>sourceLocator</code> object immediately useful.
	 * 
	 * @param name
	 *            a string containing the source name. Should not be
	 *            <code>null</code>
	 * @param lineNumber
	 *            the line number at which the error event occurred
	 */
	public SourceLocator(String name, int lineNumber) {
		this(name);
		this.setLineNumber(lineNumber);
	}

	/**
	 * Creates a new SourceLocator object with a single name.
	 * 
	 * @param name
	 *            a string containing the source name. Should not be
	 *            <code>null</code>
	 */
	public SourceLocator(String name) {
		this.setName(name);
	}

	/**
	 * Sets the name of this source.
	 * 
	 * @param name
	 *            a string containing the source name
	 * 
	 * @throws NullPointerException
	 *             if the given name is <code>null</code>
	 * @throws IllegalArgumentException
	 *             If the given name is an empty string.
	 */
	public void setName(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		if (name.length() == 0) {
			throw new IllegalArgumentException("Empty string argument.");
		}
		this.name = name;
	}

	/**
	 * Returns the name of this source.
	 * 
	 * @return A string containing the source name, or <code>null</code> if none
	 *         is available.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the line number where the error occurred.
	 * 
	 * @param lineNumber
	 *            the line number at which the error event occurred. A negative
	 *            number has no effect.
	 */
	public void setLineNumber(int lineNumber) {
		if (lineNumber > -1) {
			this.lineNumber = lineNumber;
		}
	}

	/**
	 * Returns the line number where the error occurred. Returns <code>-1</code>
	 * if the line number has not been set.
	 * 
	 * @return the line number at which the error event occurred, or
	 *         <code>-1</code> if the line number has not been set.
	 */
	public long getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Returns a string representation of this <code>SourceLocator</code>.
	 * 
	 * @return a string representation of this <code>SourceLocator</code>.
	 */
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer = buffer.append(name);
		if (lineNumber > -1L) {
			buffer = buffer.append(": ");
			buffer = buffer.append(lineNumber);
		}
		return buffer.toString();
	}
}
