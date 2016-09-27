package com.glue.catalog.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

public class HTMLPatternLayoutEncoder extends PatternLayoutEncoder{
	
	@Override
	  public void start() {
		super.start();
		PatternLayout patternLayout = new HTMLPatternLayout();
	    patternLayout.setContext(context);
	    patternLayout.setPattern(getPattern());
	    patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
	    patternLayout.start();
	    this.layout = patternLayout;
	  }

}
