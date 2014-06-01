package com.glue.webapp.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;

@Named
@ViewScoped
public class EventMapSearchBean extends EventSearchBean implements Serializable {

    @Override
    public Date getEndDate() {
	return new DateTime(getStartDate()).plusDays(1).toDate();
    }

    public void updateDate(SelectEvent event) throws Exception {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	ExternalContext extContext = facesContext.getExternalContext();

	ViewHandler viewHandler = new ViewParamsHandler(facesContext
		.getApplication().getViewHandler());

	String url = viewHandler.getActionURL(facesContext, facesContext
		.getViewRoot().getViewId());

	try {
	    extContext.redirect(url);
	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	    facesContext.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}
    }

    @Override
    public String next() throws Exception, NoSuchElementException {
	DateTime dateTime = new DateTime(getStartDate());
	setStartDate(dateTime.plusDays(1).toDate());
	search();
	return outcome();
    }

    @Override
    public String previous() throws Exception, NoSuchElementException {
	DateTime dateTime = new DateTime(getStartDate());
	setStartDate(dateTime.minusDays(1).toDate());
	search();
	return outcome();
    }

    @Override
    protected String outcome() {
	return "/stream/search-map?faces-redirect=true&amp;includeViewParams=true";
    }

}
