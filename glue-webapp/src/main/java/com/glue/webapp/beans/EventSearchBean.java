package com.glue.webapp.beans;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import com.glue.webapp.logic.EventController;

@Named
@ViewScoped
public class EventSearchBean extends StreamSearchBean implements Serializable {

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
	controller.setStart(getStart());
	controller.setRowsPerPage(getRowsPerPage());

	this.events = controller.search();

	// Update the total number of rows
	setTotalRows(controller.getTotalRows());

	return null;
    }

    @Override
    protected String outcome() {
	return "/stream/search?faces-redirect=true&amp;includeViewParams=true";
    }

}
