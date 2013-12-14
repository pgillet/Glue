package com.glue.feed.error;

import java.io.IOException;

/**
 * <p>
 * An abstract adapter class for receiving error events. The methods in this
 * class are empty. This class exists as convenience for creating error listener
 * objects.
 * </p>
 * 
 * <p>
 * Extend this class to create a <code>ErrorEvent</code> listener and override
 * the methods for the events of interest. (If you implement the
 * <code>ErrorListener</code> interface, you have to define all of the methods
 * in it. This abstract class defines null methods for them all, so you can only
 * have to define methods for events you care about.)
 * </p>
 * 
 * <p>
 * Create a listener object using the extended class and then register it with a
 * component using the component's addErrorListener method. When an error event
 * is fired, the relevant method in the listener object is invoked and the
 * <code>ErrorEvent</code> is passed to it.
 * </p>
 * 
 * @author pgillet
 */
public class ErrorAdapter implements ErrorListener {

	@Override
	public void error(ErrorEvent e) {
	}

	@Override
	public void info(ErrorEvent e) {
	}

	@Override
	public void warning(ErrorEvent e) {
	}

	@Override
	public void flush() throws IOException {
	}
}
