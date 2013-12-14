package com.glue.feed.error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An error listener that stores all error events with the ERROR level, and logs
 * all the error messages in one shot to build a final runtime report.
 * 
 * @author pgillet
 * 
 * @see ErrorDispatcher
 */
public class StoreErrorListener implements ErrorListener {

	static final Logger LOG = LoggerFactory.getLogger(StoreErrorListener.class);

	List<ErrorEvent> errorEvents = new ArrayList<>();

	/**
	 * Creates a new instance of DefaultErrorListener.
	 */
	public StoreErrorListener() {
	}

	@Override
	public void error(ErrorEvent e) {
		errorEvents.add(e);
	}

	@Override
	public void info(ErrorEvent e) {
		// Does nothing
	}

	@Override
	public void warning(ErrorEvent e) {
		// Does nothing
	}

	@Override
	public void flush() throws IOException {
		for (ErrorEvent ev : errorEvents) {
			LOG.error(ev.toString());
		}
	}
}
