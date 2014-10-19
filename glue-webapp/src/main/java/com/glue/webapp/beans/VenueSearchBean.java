package com.glue.webapp.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import com.glue.domain.Venue;
import com.glue.webapp.logic.VenueController;

@Named
@ViewScoped
public class VenueSearchBean extends StreamSearchBean implements Serializable {

    @Inject
    private transient VenueController controller;

    private String venueId;

    private Venue venue;

    public void init() {
	venue = controller.getVenue(venueId);

	if (venue == null) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("no.such.venue")));
	} else {

	    if (venue.getParent() != null) {
		// The venue has a reference venue
		venue = venue.getParent();
	    }
	}
    }

    public String getVenueId() {
	return venueId;
    }

    public void setVenueId(String venueId) {
	this.venueId = venueId;
    }

    public Venue getVenue() {
	return venue;
    }

    public void setVenue(Venue venue) {
	this.venue = venue;
    }

    @Override
    public String search() throws Exception {

	// Common params
	controller.setQueryString(getQuery());
	controller.setStartDate(getStartDate());
	controller.setEndDate(getEndDate());
	controller.setCategories(getCatSelection());

	// Specific params
	controller.setVenueId(venue.getId());

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
	return FacesContext.getCurrentInstance().getViewRoot().getViewId()
		+ "?faces-redirect=true&amp;includeViewParams=true";
    }

}
