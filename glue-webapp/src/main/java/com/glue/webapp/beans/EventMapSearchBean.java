package com.glue.webapp.beans;

import java.io.Serializable;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.omnifaces.cdi.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ViewScoped
public class EventMapSearchBean implements
	Serializable {

    static final Logger LOG = LoggerFactory.getLogger(EventMapSearchBean.class);

    @Inject
    private EventSearchBean searchBean;

    public String search() throws Exception {

	// Date startDate = searchBean.getStartDate();
	// Date endDate = new DateTime(startDate).plusDays(1).toDate();
	// searchBean.setEndDate(endDate);
	//
	// // Pagination controls
	// searchBean.setStart(0);
	// searchBean.setDisplay(DisplayType.MAP);
	//
	// searchBean.search();

	return null;
    }

    public String next() throws Exception, NoSuchElementException {
	DateTime dateTime = new DateTime(searchBean.getStartDate());
	searchBean.setStartDate(dateTime.plusDays(1).toDate());
	search();
	return outcome();
    }


    public String previous() throws Exception, NoSuchElementException {
	DateTime dateTime = new DateTime(searchBean.getStartDate());
	searchBean.setStartDate(dateTime.minusDays(1).toDate());
	search();
	return outcome();
    }

    public String enableCategory() {
	searchBean.enableCategory();

	return outcome();
    }

    public String enableAllCategories() {
	searchBean.enableAllCategories();

	return outcome();
    }

    protected String outcome() {
	return "/stream/search-map?faces-redirect=true&amp;includeViewParams=true";
    }

}
