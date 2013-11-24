package com.glue.webapp.beans;

import javax.faces.context.FacesContext;

public class FacesUtil {

	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}

	public static Object getRequestAttribute(String name) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestMap().get(name);
	}

}
