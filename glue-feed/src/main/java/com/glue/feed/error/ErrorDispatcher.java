package com.glue.feed.error;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A class that holds a list of error event listeners. A single instance can be
 * used to hold all error listeners for the instance using the list. This class
 * provides methods which dispatch event notification methods to the error
 * listeners in the list according to the passed error level.
 * 
 * <p>
 * A class that has interest for handling error listeners should implement the
 * <code>ErrorHandler</code> interface on top of an underlying error dispatcher:
 * 
 * <pre>
 *  Class Sample implements ErrorHandler {
 *  private ErrorDispatcher dispatcher = new ErrorDispatcher();
 *  public ErrorListener[] getErrorListeners() {
 *  return dispatcher.getErrorListeners();
 *  }
 *  public void addErrorListener(ErrorListener l) {
 *  dispatcher.addErrorListener(l);
 *  }
 *  public void removeErrorListener(ErrorListener l) {
 *  dispatcher.removeErrorListener(l);
 *  }
 *  public void foo(){
 *  ...
 *  dispatcher.fireErrorEvent("error in foo method", Level.ERROR);
 *  ...
 *  dispatcher.flush();
 *  ...
 *  }
 *  }
 * </pre>
 * 
 * </p>
 * 
 * @author pgillet
 */
public class ErrorDispatcher implements ErrorHandler, ErrorManager {

	/** The list of error event listeners. */
	private List listeners;

	/**
	 * Tells whether or not the list of error listeners is in its initial state.
	 */
	private boolean removeDefault = true;

	/**
	 * Creates a new instance of ErrorDispatcher.
	 */
	public ErrorDispatcher() {
		listeners = new LinkedList();
		listeners.add(new DefaultErrorListener());
	}

	@Override
	public ErrorListener[] getErrorListeners() {
		return (ErrorListener[]) listeners.toArray(new ErrorListener[listeners
				.size()]);
	}

	@Override
	public void addErrorListener(ErrorListener l) {
		if (listeners.add(l) && removeDefault) {
			listeners.remove(0);
			removeDefault = false;
		}
	}

	@Override
	public void removeErrorListener(ErrorListener l) {
		listeners.remove(l);
		if (listeners.isEmpty()) {
			removeDefault = listeners.add(new DefaultErrorListener());
		}
	}

	@Override
	public void fireErrorEvent(ErrorLevel lvl, String message, Throwable cause,
			String source, int lineNumber) {
		if (lvl != ErrorLevel.OFF) {

			ErrorListener[] listeners = getErrorListeners();

			SourceLocator locator = null;
			if (source != null) {
				locator = new SourceLocator(source, lineNumber);
			}

			ErrorEvent ex = new ErrorEvent(message, cause, locator);

			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length - 1; i >= 0; i--) {
				if (lvl == ErrorLevel.INFO) {
					listeners[i].info(ex);
				} else if (lvl == ErrorLevel.WARN) {
					listeners[i].warning(ex);
				} else if (lvl == ErrorLevel.ERROR) {
					listeners[i].error(ex);
				}
			}
		}
	}

	@Override
	/**
	 * Flushes all the listeners registered.
	 */
	public void flush() throws IOException {
		ErrorListener[] listeners = getErrorListeners();

		// Flushes the listeners last to first
		for (int i = listeners.length - 1; i >= 0; i--) {
			try {
				listeners[i].flush();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
