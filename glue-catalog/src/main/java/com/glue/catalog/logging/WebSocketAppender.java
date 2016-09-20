package com.glue.catalog.logging;

import java.io.BufferedOutputStream;
import java.io.IOException;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class WebSocketAppender extends AppenderBase<ILoggingEvent> {

	PatternLayoutEncoder encoder;

	WebSocketOutputStream outputStream;

	@Override
	public void start() {
		if (this.encoder == null) {
			addError("No encoder set for the appender named [" + name + "].");
			return;
		}

		if (this.outputStream == null) {
			addError("No output stream set for the appender named [" + name + "].");
			return;
		}

		try {
			encoder.init(new BufferedOutputStream(outputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.start();
	}

	public void append(ILoggingEvent event) {
		// output the events as formatted by our layout
		try {
			this.encoder.doEncode(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PatternLayoutEncoder getEncoder() {
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoder encoder) {
		this.encoder = encoder;
	}

	public WebSocketOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(WebSocketOutputStream outputStream) {
		this.outputStream = outputStream;
	}

}
