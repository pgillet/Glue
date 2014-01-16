package com.glue.webapp.beans;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class FacesUtil {

	public static final String COMMON_BASENAME = "com.glue.messages.Messages";
	public static final String SERVER_BASENAME = "com.glue.messages.server";

	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}

	public static Object getRequestAttribute(String name) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestMap().get(name);
	}

	public static String getString(String key) {
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();

		ResourceBundle bundle = ResourceBundle.getBundle(SERVER_BASENAME,
				locale);

		String str;
		try {
			str = bundle.getString(key);
		} catch (Exception e) {
			// Fallback
			bundle = ResourceBundle.getBundle(COMMON_BASENAME, locale);
			str = bundle.getString(key);
		}

		return str;
	}
}
