package com.glue.api.application;

import com.glue.api.conf.ConfigurationImpl;

/**
 * A factory class for Glue.
 * 
 */

public final class GlueFactory implements java.io.Serializable {

	private static final long serialVersionUID = -4031763609960119795L;

	private static final Glue SINGLETON = new GlueImpl(new ConfigurationImpl());

	/**
	 * Creates a GlueFactory.
	 */
	public GlueFactory() {
	}

	/**
	 * Returns a instance of Glue.
	 * 
	 * @return default singleton instance
	 */
	public Glue getInstance() {
		return SINGLETON;
	}

}
