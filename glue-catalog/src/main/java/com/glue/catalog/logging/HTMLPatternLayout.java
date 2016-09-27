package com.glue.catalog.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class HTMLPatternLayout extends PatternLayout {

	@Override
	public String doLayout(ILoggingEvent event) {

		String txt = super.doLayout(event);

		txt = txt.replaceAll("\\R", "<br>");
		// txt = txt.replaceAll("\\s{3}", "&nbsp;&nbsp;&nbsp;");

		return txt;
	}
}
