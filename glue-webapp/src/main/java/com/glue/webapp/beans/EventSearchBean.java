package com.glue.webapp.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.cdi.param.ParamValue;

import com.glue.webapp.logic.EventController;

@Named
@ViewScoped
public class EventSearchBean extends StreamSearchBean implements Serializable {

    private String location;
    private double latitude;
    private double longitude;

    @Inject
    @Param
    // Like <f:viewParam name="q" value="#{bean.q}">
    private ParamValue<String> q;

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

    @PostConstruct
    private void init() {
	String rowsPerPageParam = FacesUtil
		.getRequestParameter(QueryParams.PARAM_ROWS_PER_PAGE);
	if (rowsPerPageParam != null) {
	    setRowsPerPage(Integer.valueOf(rowsPerPageParam));
	}
	setQuery(q.getValue());

	LOG.info("Query param = " + q);

	first();
    }

    public String search0() {

	String viewId = FacesContext.getCurrentInstance().getViewRoot()
		.getViewId();
	// TODO: should reference view id in a more consistent way!
	if ("/stream/search.xhtml".equals(viewId)) {
	    first();
	    return null;
	}

	// Redirect
	return "event-search";
    }

    @Override
    public Void search() throws Exception {

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

}
