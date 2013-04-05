package com.glue.webapp.services;

import javax.ws.rs.ApplicationPath;

import com.sun.jersey.api.core.PackagesResourceConfig;

@ApplicationPath("services")
public class GlueApplication extends PackagesResourceConfig {
	public GlueApplication() {
		super("com.glue.webapp.services");
	}

}
