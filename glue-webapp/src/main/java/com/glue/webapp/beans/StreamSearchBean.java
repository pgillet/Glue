package com.glue.webapp.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.webapp.search.AbstractPaginatedSearch;

/**
 * <p>
 * Integrated the Omnifaces utility library for JSF 2 to support CDI in the view
 * scope (NPE with Tomee 1.6.0/MyFaces 2.1.13).
 * </p>
 * <p>
 * Note: we can get rid of Omnifaces if we migrate to MyFaces 2.2.0 (by
 * replacing the jars in the Tomee lib directory): it works well but it ends up
 * with bad UI rendering...
 * </p>
 * 
 * @author pgillet
 * 
 */
public abstract class StreamSearchBean extends AbstractPaginatedSearch<String>
	implements Serializable {

    static final Logger LOG = LoggerFactory.getLogger(StreamSearchBean.class);

    private String query;
    private Date startDate;
    private Date endDate;

    private List<String> catSelection = new ArrayList<>();

    private EventCategory[] categories = EventCategory.values();

    protected transient List<Event> events;

    private DisplayType display = DisplayType.LIST; // Default

    private IntervalType interval;

    public DisplayType getDisplay() {
	return display;
    }

    public String getDisplayLabel() {
	return display.getLabelKey();
    }

    public void setDisplay(DisplayType display) {
	this.display = display;
    }

    public IntervalType getInterval() {
	return interval;
    }

    public void setInterval(IntervalType interval) {
	this.interval = interval;
    }

    // TODO: may be moved somewhere else
    public int getRowsPerPage(DisplayType display) {

	int rows;

	switch (display) {
	case GRID:
	    rows = 11;
	    break;

	case TABLE:
	    rows = 15;
	    break;

	case MAP:
	    rows = 500;
	    break;

	default: // LIST
	    rows = 10;
	    break;
	}

	return rows;
    }

    public String getQuery() {
	return query;
    }

    public void setQuery(String query) {
	this.query = query;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public List<String> getCatSelection() {
	return catSelection;
    }

    public void setCatSelection(List<String> catSelection) {
	this.catSelection = catSelection;
    }

    /**
     * @return the events
     */
    public List<Event> getEvents() {
	return events;
    }

    /**
     * @return the categories
     */
    public EventCategory[] getCategories() {
	return categories;
    }

    public void searchFrom(SelectEvent event) {
	// Reset end date
	setEndDate(null);
	setInterval(IntervalType.FROM_TODAY);
	setStart(0);

	try {
	    FacesUtil.redirectIncludingViewParams();
	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext.getCurrentInstance().addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}
    }

    public String searchToday() {
	// TODO: Get the client time zone somehow
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusDays(1);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.TODAY);

	return first();
    }

    public String searchNextWeekEnd() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	if (start.getDayOfWeek() < DateTimeConstants.FRIDAY) {
	    start = start.withDayOfWeek(DateTimeConstants.FRIDAY);
	}
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusWeeks(1);
	end = end.withDayOfWeek(DateTimeConstants.MONDAY);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.WEEK_END);

	return first();
    }

    public String searchWeek() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusWeeks(1);
	end = end.withDayOfWeek(DateTimeConstants.MONDAY);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.WEEK);

	return first();
    }

    public String searchMonth() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusMonths(1);
	end = end.withDayOfMonth(1);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.MONTH);

	return first();
    }

    public String enableCategory() {
	String cat = FacesUtil.getRequestParameter("selectedCat");
	LOG.debug("Toggle category = " + cat);

	if (!getCatSelection().remove(cat)) {
	    getCatSelection().add(cat);
	}

	return first();
    }

    @Override
    public abstract String search() throws Exception;

    @Override
    public String first() {
	try {
	    super.first();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return outcome();
    }

    /**
     * For EL access.
     * 
     * @return
     */
    public boolean isNext() {
	return hasNext();
    }

    @Override
    public String next() throws Exception, NoSuchElementException {
	try {
	    super.next();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return outcome();
    }

    /**
     * For EL access.
     * 
     * @return
     */
    public boolean isPrevious() {
	return hasPrevious();
    }

    @Override
    public String previous() throws Exception, NoSuchElementException {
	try {
	    super.previous();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return outcome();
    }

    @Override
    public String last() throws Exception {
	try {
	    super.last();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return outcome();
    }

    @Override
    public String get(int pageNumber) throws Exception, NoSuchElementException {
	try {
	    super.get(pageNumber);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return outcome();
    }

    protected String outcome() {
	return null;
    }

}
