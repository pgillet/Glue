package com.glue.feed.error;

import java.io.IOException;

/**
 * If an application does not register its own custom <code>ErrorListener</code>
 * , this default <code>ErrorListener</code> implementation will be used which
 * reports all warnings and errors to <code>System.err</code> and does not throw
 * any <code>Exception</code>s.<br>
 * Applications are strongly encouraged to register and use
 * <code>ErrorListener</code>s that insure proper behavior for warnings and
 * errors.
 * 
 * @author pgillet
 * 
 * @see ErrorDispatcher
 */
public class DefaultErrorListener implements ErrorListener {

	/**
	 * Creates a new instance of DefaultErrorListener.
	 */
	public DefaultErrorListener() {
	}

	@Override
	public void error(ErrorEvent e) {
		System.err.println(ErrorLevel.ERROR.getName() + ": " + e.toString());
	}

	@Override
	public void info(ErrorEvent e) {
		System.err.println(ErrorLevel.INFO.getName() + ": " + e.toString());
	}

	@Override
	public void warning(ErrorEvent e) {
		System.err.println(ErrorLevel.WARN.getName() + ": " + e.toString());
	}

	@Override
	/**
	 * This implementation does nothing.
	 */
	public void flush() throws IOException {
	}
}
