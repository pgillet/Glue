package com.glue.webapp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable {

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
					.getDisplayLanguage()));
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

	public void setLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	// value change event listener
	public void countryLocaleCodeChanged(ValueChangeEvent e) {

		if ((e != null) && (e.getNewValue() != null)) {
			String language = e.getNewValue().toString();
			setLanguage(language);
		}
	}

}
