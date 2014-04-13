package com.glue.webapp.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.cdi.param.ParamValue;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;
import com.glue.webapp.search.PageIterator;

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
@Named
@ViewScoped
public class StreamSearchBean implements PageIterator<Void>, Serializable {

    private static final String PARAM_CAT = "cat";
    private static final String PARAM_DISPLAY = "display";
    private static final String PARAM_ROWS_PER_PAGE = "rowsperpage";

    @Inject
    @Param
    // Like <f:viewParam name="q" value="#{bean.q}">
    private ParamValue<String> q;

    static final Logger LOG = LoggerFactory.getLogger(StreamSearchBean.class);

    @Inject
    private StreamController streamController;

    private EventCategory[] categories = EventCategory.values();

    private transient List<Event> events;

    private DisplayType display = DisplayType.LIST; // Default

    @PostConstruct
    private void init() {
	String rowsPerPageParam = FacesUtil
		.getRequestParameter(PARAM_ROWS_PER_PAGE);
	if (rowsPerPageParam != null) {
	    setRowsPerPage(Integer.valueOf(rowsPerPageParam));
	}
	setQuery(q.getValue());

	LOG.info("Query param = " + q);

	first();
    }

    public DisplayType getDisplay() {
	return display;
    }

    public String getDisplayLabel() {
	return display.getLabelKey();
    }

    public void setDisplay(DisplayType display) {
	this.display = display;
    }

    public String toggleDisplay() {
	String displayParam = FacesUtil.getRequestParameter(PARAM_DISPLAY);
	setDisplay(DisplayType.valueOf(displayParam.toUpperCase()));
	LOG.debug("Toggle display = " + display);

	switch (display) {
	case GRID:
	    return "event-search-grid";

	case TABLE:
	    return "event-search-table";

	default: // LIST
	    return "event-search";
	}
    }

    /**
     * @return the query
     */
    public String getQuery() {
	return streamController.getQueryString();
    }

    /**
     * @param query
     *            the query to set
     */
    public void setQuery(String query) {
	streamController.setQueryString(query);
    }

    /**
     * @return the location
     */
    public String getLocation() {
	return streamController.getLocation();
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
	streamController.setLocation(location);
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
	return streamController.getLatitude();
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude) {
	streamController.setLatitude(latitude);
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
	return streamController.getLongitude();
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(double longitude) {
	streamController.setLongitude(longitude);
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
	return streamController.getStartDate();
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
	streamController.setStartDate(startDate);
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
	return streamController.getEndDate();
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
	streamController.setEndDate(endDate);
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

    /**
     * @return the catSelection
     */
    public List<String> getCatSelection() {
	return streamController.getCategories();
    }

    /**
     * @param catSelection
     *            the catSelection to set
     */
    public void setCatSelection(List<String> catSelection) {
	streamController.setCategories(catSelection);
    }

    /**
     * @param events
     *            the events to set
     */
    public void setEvents(List<Event> events) {
	this.events = events;
    }

    public String search() {
	// Redirect
	return "event-search";
    }

    public void searchFrom(SelectEvent event) {
	// Reset end date
	setEndDate(null);
	first();
    }

    public void searchToday() {
	// TODO: Get the client time zone somehow
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusDays(1);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	first();
    }

    public void searchNextWeekEnd() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	if (start.getDayOfWeek() < DateTimeConstants.FRIDAY) {
	    start = start.withDayOfWeek(DateTimeConstants.FRIDAY);
	}
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusWeeks(1);
	end = end.withDayOfWeek(DateTimeConstants.MONDAY);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	first();
    }

    public void searchWeek() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusWeeks(1);
	end = end.withDayOfWeek(DateTimeConstants.MONDAY);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	first();
    }

    public void searchMonth() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusMonths(1);
	end = end.withDayOfMonth(1);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	first();
    }

    @Override
    public boolean hasNext() {
	return streamController.hasNext();
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
    public Void next() {
	FacesContext context = FacesContext.getCurrentInstance();

	try {
	    events = streamController.next();
	} catch (NoSuchElementException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

    @Override
    public boolean hasPrevious() {
	return streamController.hasPrevious();
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
    public Void previous() {
	FacesContext context = FacesContext.getCurrentInstance();

	try {
	    events = streamController.previous();
	} catch (NoSuchElementException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

    @Override
    public Void first() {
	FacesContext context = FacesContext.getCurrentInstance();

	try {
	    events = streamController.first();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

    @Override
    public Void last() {
	FacesContext context = FacesContext.getCurrentInstance();

	try {
	    events = streamController.last();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

    @Override
    public Void get(int pageNumber) throws NoSuchElementException {
	// TODO Not yet implemented
	return null;
    }

    @Override
    public int getStart() {
	return streamController.getStart();
    }

    @Override
    public void setStart(int start) {
	streamController.setStart(start);

    }

    @Override
    public int getRowsPerPage() {
	return streamController.getRowsPerPage();
    }

    @Override
    public void setRowsPerPage(int rows) {
	streamController.setRowsPerPage(rows);
    }

    @Override
    public long getTotalRows() {
	return streamController.getTotalRows();
    }

    /**
     * For pagination control from request to request.
     */
    public void setTotalRows(long totalRows) {
	streamController.setTotalRows(totalRows);
    }

    @Override
    public int getPageIndex() {
	return streamController.getPageIndex();
    }

    @Override
    public int getTotalPages() {
	return streamController.getTotalPages();
    }

    /**
     * Returns the CSS styles to apply to the given category selector.
     * 
     * @param cat
     *            the category name
     * @param javascriptSyntax
     *            a boolean telling whether the method should return the
     *            JavaScript syntax or not
     * @param onmouseover
     *            a boolean telling whether the method should return the CSS
     *            styles to be applied when the pointer is moved onto the
     *            element (onMouseOver attribute) or away from it (onMouseOut
     *            attribute). This parameter is ignored if javascriptSyntax is
     *            set to false.
     * @return
     */
    public String getCategoryStyle(String cat, boolean javascriptSyntax,
	    boolean onmouseover) {
	String styleAttr;
	if (javascriptSyntax) {
	    // JavaScript syntax
	    styleAttr = "this.style.paddingBottom = '%dpx'; this.style.borderBottom = '%dpx solid %s' ;";
	} else {
	    styleAttr = "padding-bottom: %dpx; border-bottom: %dpx solid %s ;";
	}
	// We compensate for the height of the border with the padding and vice
	// versa
	int borderWidth;
	int paddingBottom;
	if (getCatSelection().contains(cat) || onmouseover) {
	    paddingBottom = 3;
	    borderWidth = 5;
	} else {
	    paddingBottom = 6;
	    borderWidth = 2;
	}

	styleAttr = String.format(styleAttr, paddingBottom, borderWidth,
		EventCategory.valueOf(cat).getColor());

	return styleAttr;
    }

    public void enableCategory() {
	String cat = FacesUtil.getRequestParameter(PARAM_CAT);
	LOG.debug("Toggle category = " + cat);

	if (!getCatSelection().remove(cat)) {
	    getCatSelection().add(cat);
	}

	try {
	    events = streamController.first();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}
    }

    public void selectCategory() {
	String cat = FacesUtil.getRequestParameter(PARAM_CAT);
	LOG.debug("Select category = " + cat);

	// Select only the chosen category
	getCatSelection().clear();
	getCatSelection().add(cat);

	try {
	    events = streamController.first();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}
    }

}
