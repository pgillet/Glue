package com.glue.webapp.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.cdi.param.ParamValue;

import com.glue.domain.Venue;
import com.glue.webapp.logic.VenueController;

@Named
@ViewScoped
public class VenueSearchBean extends StreamSearchBean implements Serializable {

    @Inject
    @Param
    // Like <f:viewParam name="q" value="#{bean.q}">
    private ParamValue<String> q;

    @Inject
    @Param
    // Like <f:viewParam name="v" value="#{bean.v}">
    private ParamValue<String> v;

    @Inject
    private transient VenueController controller;

    private Venue venue;

    @PostConstruct
    private void init() {
	String rowsPerPageParam = FacesUtil
		.getRequestParameter(QueryParams.PARAM_ROWS_PER_PAGE);
	if (rowsPerPageParam != null) {
	    setRowsPerPage(Integer.valueOf(rowsPerPageParam));
	}
	setQuery(q.getValue());
	LOG.debug("Query param = " + q);

	String venueId = v.getValue();
	venue = controller.getVenue(venueId);

	if (venue == null) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("no.such.venue")));
	} else {
	    first();
	}
    }

    public Venue getVenue() {
	return venue;
    }

    public void setVenue(Venue venue) {
	this.venue = venue;
    }

    @Override
    public Void search() throws Exception {

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

}

