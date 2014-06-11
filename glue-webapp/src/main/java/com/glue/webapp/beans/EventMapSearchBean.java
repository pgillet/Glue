package com.glue.webapp.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.webapp.logic.EventController;

@Named
@ViewScoped
public class EventMapSearchBean extends StreamSearchBean implements
	Serializable {

    static final Logger LOG = LoggerFactory.getLogger(EventMapSearchBean.class);

    private String location;
    private double latitude;
    private double longitude;

    @Inject
    private transient EventController controller;

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public double getLatitude() {
	return latitude;
    }

    public void setLatitude(double latitude) {
	this.latitude = latitude;
    }

    public double getLongitude() {
	return longitude;
    }

    public void setLongitude(double longitude) {
	this.longitude = longitude;
    }

    @Override
    public String search() throws Exception {

	if (FacesContext.getCurrentInstance().isPostback()) {
	    return null;
	}

	// Common params
	controller.setQueryString(getQuery());
	controller.setStartDate(getStartDate());
	controller.setEndDate(getEndDate());
	controller.setCategories(getCatSelection());

	// Specific params
	controller.setLocation(location);
	controller.setLatitude(latitude);
	controller.setLongitude(longitude);

	// Pagination controls
	controller.setStart(0);
	controller.setRowsPerPage(500);

	this.events = controller.search();

	// Update the total number of rows
	setTotalRows(controller.getTotalRows());

	return null;
    }

    @Override
    public Date getEndDate() {
	Date startDate = getStartDate();
	Date endDate = new DateTime(startDate).plusDays(1).toDate();
	return endDate;
    }

    public void updateDate(SelectEvent event) throws Exception {
	try {
	    FacesUtil.redirectIncludingViewParams();
	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext.getCurrentInstance().addMessage(null,
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
