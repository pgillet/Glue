package com.glue.webapp.logic;

public class InternalServerException extends GlueApplicationException {

	public InternalServerException(String message) {
		super(message);
	}

	public InternalServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalServerException(Throwable cause) {
		super(cause);
	}

}
