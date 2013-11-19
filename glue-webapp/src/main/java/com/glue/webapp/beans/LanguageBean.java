package com.glue.webapp.beans;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.webapp.i18n.I18nFilter;

@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable {
	
	static final Logger LOG = LoggerFactory.getLogger(LanguageBean.class);

	private static final long serialVersionUID = 1L;

	/**
	 * List of available locales.
	 */
	private static Locale[] locales = new Locale[] { Locale.ENGLISH,
			Locale.FRENCH, };

	private List<SelectItem> localeItems;

	private Locale locale = FacesContext.getCurrentInstance().getViewRoot()
			.getLocale();

	@PostConstruct
	public void init() {
		localeItems = new ArrayList<SelectItem>();

		for (Locale locale : locales) {
			localeItems.add(new SelectItem(locale.getLanguage(), locale
					.getDisplayLanguage(locale)));
		}
	}

	public List<SelectItem> getLocales() {
		return localeItems;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public void setLanguage(String language) throws IOException {
		locale = new Locale(language);
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot viewRoot = context.getViewRoot();

		// Store the current locale before change
		Locale oldLocale = viewRoot.getLocale();

		// Set the new locale
		viewRoot.setLocale(locale);

		ExternalContext externalContext = context.getExternalContext();

		// Binds the user language to the session.
		externalContext.getSessionMap().put(I18nFilter.USER_LANGUAGE, language);

		// Redirect to the default document at the server's document root
		HttpServletRequest request = (HttpServletRequest) externalContext
				.getRequest();
		StringBuffer requestURL = request.getRequestURL();

		String requestedPath = externalContext.getRequestServletPath();
		final String prefix = "/" + I18nFilter.getLocaleTag(oldLocale) + "/";
		if (requestedPath.startsWith(prefix)) {
			String defaultPath = requestedPath.substring(prefix.length() - 1);
			int index = requestURL.indexOf(requestedPath);
			requestURL.replace(index, index + requestedPath.length(),
					defaultPath);
		}

		externalContext.redirect(requestURL.toString());
		// I18nFilter is responsible for redirecting to the right document
		// localized in a directory named with the user's language...

	}

	// value change event listener
	public void countryLocaleCodeChanged(ValueChangeEvent e) {

		if ((e != null) && (e.getNewValue() != null)) {
			String language = e.getNewValue().toString();
			try {
				setLanguage(language);
			} catch (IOException ex) {
				LOG.error(ex.getMessage(), ex);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("An unexpected error occured."));
			}
		}
	}

}
