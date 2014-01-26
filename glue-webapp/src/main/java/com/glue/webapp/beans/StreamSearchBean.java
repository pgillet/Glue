package com.glue.webapp.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Category;
import com.glue.domain.IStream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;
import com.glue.webapp.search.PageIterator;

@ManagedBean
public class StreamSearchBean implements PageIterator<Void>, Serializable {

    private static final String PARAM_CAT = "cat";

    static final Logger LOG = LoggerFactory.getLogger(StreamSearchBean.class);

    @Inject
    private StreamController streamController;

    private DateTimeConverter convertDate;

    private Category[] categories = Category.values();

    private List<IStream> streams;

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

    public DateTimeConverter getConvertDate() {
	return convertDate;
    }

    public void setConvertDate(DateTimeConverter convertDate) {
	final String key1 = "date_format_long";
	// final String key2 = "time_format";

	String dateFormat = FacesUtil.getString(key1);
	// String timeFormat = bundle.getString(key2);

	// convertDate.setPattern(dateFormat + " " + timeFormat);
	convertDate.setPattern(dateFormat);

	TimeZone tz = TimeZone.getTimeZone("UTC");
	convertDate.setTimeZone(tz);

	this.convertDate = convertDate;
    }

    /**
     * @return the streams
     */
    public List<IStream> getStreams() {
	return streams;
    }

    /**
     * @return the categories
     */
    public Category[] getCategories() {
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
     * @param streams
     *            the streams to set
     */
    public void setStreams(List<IStream> streams) {
	this.streams = streams;
    }

    public String search() {

	FacesContext context = FacesContext.getCurrentInstance();

	try {
	    streams = streamController.search();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}

	return "stream-search";
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
	    streams = streamController.next();
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
	    streams = streamController.previous();
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
	    streams = streamController.first();
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
	    streams = streamController.last();
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
	    styleAttr = "this.style.borderBottom = '%dpx solid %s' ;";
	} else {
	    styleAttr = "border-bottom: %dpx solid %s ;";
	}
	int borderWidth;
	if (getCatSelection().contains(cat) || onmouseover) {
	    borderWidth = 3;
	} else {
	    borderWidth = 2;
	}

	styleAttr = String.format(styleAttr, borderWidth, Category.valueOf(cat)
		.getColor());

	return styleAttr;
    }

    public void enableCategory() {
	String cat = FacesUtil.getRequestParameter(PARAM_CAT);
	LOG.debug("Toggle category = " + cat);

	if (!getCatSelection().remove(cat)) {
	    getCatSelection().add(cat);
	}

	try {
	    streams = streamController.first();
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
	    streams = streamController.first();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null,
		    new FacesMessage(FacesUtil.getString("error.generic")));
	}
    }

}
