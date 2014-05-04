package com.glue.webapp.beans;

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
public abstract class StreamSearchBean extends AbstractPaginatedSearch<Void>
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

    public void toggleDisplay() {
	String displayParam = FacesUtil
		.getRequestParameter(QueryParams.PARAM_DISPLAY);
	setDisplay(DisplayType.valueOf(displayParam.toUpperCase()));
	LOG.debug("Toggle display = " + display);

	switch (display) {
	case GRID:
	    setRowsPerPage(11);
	    break;

	case TABLE:
	    setRowsPerPage(15);
	    break;

	default: // LIST
	    setRowsPerPage(10);
	    break;
	}

	first();
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

	first();
    }

    public void searchToday() {
	// TODO: Get the client time zone somehow
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusDays(1);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.TODAY);

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

	setInterval(IntervalType.WEEK_END);

	first();
    }

    public void searchWeek() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusWeeks(1);
	end = end.withDayOfWeek(DateTimeConstants.MONDAY);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.WEEK);

	first();
    }

    public void searchMonth() {
	DateTime start = new DateTime(DateTimeZone.UTC);
	start = start.withTimeAtStartOfDay();

	DateTime end = start.plusMonths(1);
	end = end.withDayOfMonth(1);

	setStartDate(start.toDate());
	setEndDate(end.toDate());

	setInterval(IntervalType.MONTH);

	first();
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
	String cat = FacesUtil.getRequestParameter(QueryParams.PARAM_CAT);
	LOG.debug("Toggle category = " + cat);

	if (!getCatSelection().remove(cat)) {
	    getCatSelection().add(cat);
	}

	first();
    }

    public void selectCategory() {
	String cat = FacesUtil.getRequestParameter(QueryParams.PARAM_CAT);
	LOG.debug("Select category = " + cat);

	// Select only the chosen category
	getCatSelection().clear();
	getCatSelection().add(cat);

	first();
    }

    @Override
    public abstract Void search() throws Exception;

    @Override
    public Void first() {
	try {
	    super.first();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
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
    public Void next() throws Exception, NoSuchElementException {
	try {
	    super.next();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
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
    public Void previous() throws Exception, NoSuchElementException {
	try {
	    super.previous();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

    @Override
    public Void last() throws Exception {
	try {
	    super.last();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

    @Override
    public Void get(int pageNumber) throws Exception, NoSuchElementException {
	try {
	    super.get(pageNumber);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return null;
    }

}
