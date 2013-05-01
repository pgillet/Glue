package com.glue.webapp.logic;

public class AlreadyExistsException extends GlueApplicationException {

	public AlreadyExistsException(String message) {
		super(message);
	}

	public AlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
