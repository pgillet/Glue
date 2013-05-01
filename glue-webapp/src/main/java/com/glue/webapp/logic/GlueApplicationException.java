package com.glue.webapp.logic;

public class GlueApplicationException extends Exception {

	public GlueApplicationException(String message) {
		super(message);
	}

	public GlueApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GlueApplicationException(Throwable cause) {
		super(cause);
	}

}
