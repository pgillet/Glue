package com.glue.feed.error;

import java.io.Flushable;

/**
 * A interface that holds a list of error listeners.
 * 
 * <p>
 * It is the responsability of the class using the <code>ErrorManager</code> to
 * provide methods which dispatch event notification methods to appropriate
 * error listeners on the list.
 * </p>
 * 
 * @author pgillet
 * 
 * @see DefaultErrorListener
 */
public interface ErrorManager extends Flushable {

	/**
	 * Returns an array of all the error listeners registered on this object.
	 * 
	 * @return all of this object's error listeners or an array with a single
	 *         <code>DefaultErrorListener</code> if no error listeners are
	 *         currently registered
	 * 
	 * @see #addErrorListener(ErrorListener)
	 * @see #removeErrorListener(ErrorListener)
	 */
	ErrorListener[] getErrorListeners();

	/**
	 * Adds the specified error listener to receive error events from this
	 * object. The <code>DefaultErrorListener</code> is not registered anymore.
	 * 
	 * @param l
	 *            the error listener
	 * 
	 * @see #getErrorListeners()
	 * @see #removeErrorListener(ErrorListener)
	 */
	void addErrorListener(ErrorListener l);

	/**
	 * Removes the specified error listener so that is no longer receives error
	 * events from this object. If there is no more registered error listeners,
	 * registers a <code>DefaultErrorListener</code> again.
	 * 
	 * @param l
	 *            the error listener
	 * 
	 * @see #addErrorListener(ErrorListener)
	 * @see #getErrorListeners()
	 */
	void removeErrorListener(ErrorListener l);

}
