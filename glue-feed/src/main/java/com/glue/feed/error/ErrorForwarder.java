package com.glue.feed.error;

/**
 * An <code>ErrorHandler</code> that simply delegates error events to its parent
 * <code>ErrorHandler</code>.
 * 
 * @author pgillet
 */
public class ErrorForwarder implements ErrorNode {

	/** The parent <code>ErrorHandler</code>. */
	private ErrorHandler parent;

	/**
	 * Creates a new instance of ErrorForwarder.
	 */
	public ErrorForwarder() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParent(ErrorHandler parent) {
		this.parent = parent;
	}

	/**
	 * Get the specified error handler to be responsible for handling the error
	 * events sent from this object.
	 * 
	 * @return the parent<code>ErrorHandler</code>.
	 */
	public ErrorHandler getParent() {
		return parent;
	}

	/**
	 * Sends an error event with the specified level to the parent
	 * <code>ErrorHandler</code> which is responsible for processing it.
	 * 
	 * <p>
	 * If no error handler is registered, reports all warnings and errors to
	 * <code>System.err</code> and does not throw any <code>Exception</code>s. <br>
	 * Applications are strongly encouraged to register and use
	 * <code>ErrorHandler</code>s that insure proper behavior for warnings and
	 * errors
	 * </p>
	 * .
	 * 
	 * @param lvl
	 *            The error level
	 * @param message
	 *            A string message
	 * @param cause
	 *            A Throwable associated with the string message. May be
	 *            <code>null</code>.
	 * @param source
	 *            The name of the source where the error occured. May be
	 *            <code>null</code> if none is available.
	 * @param lineNumber
	 *            the offset from the beginning of the file, in bytes, at which
	 *            the error event occurred. A negative number if undefined.
	 */
	public void fireErrorEvent(ErrorLevel lvl, String message, Throwable cause,
			String source, int lineNumber) {
		if (parent != null) {
			parent.fireErrorEvent(lvl, message, cause, source, lineNumber);
		} else {
			ErrorEvent ex = new ErrorEvent(message, cause, new SourceLocator(
					source, lineNumber));
			System.err.println(lvl.getName() + ": " + ex.toString());
		}
	}
}
