package com.glue.exceptions;

public class GlueException extends Exception {

	private static final long serialVersionUID = -7705437538155276889L;

	public GlueException(Throwable e) {
		super(e);
	}

	public GlueException(String msg) {
		super(msg);
	}

}
