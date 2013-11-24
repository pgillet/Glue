package com.glue.webapp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.struct.Category;
import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;
import com.glue.webapp.search.PageIterator;

@ManagedBean
public class StreamSearchBean implements PageIterator<Void>, Serializable {

	private static final String PARAM_CAT = "cat";

	static final Logger LOG = LoggerFactory.getLogger(StreamSearchBean.class);

	@Inject
	private StreamController streamController;

	private String location;

	private double latitude;

	private double longitude;

	private Date startDate;

	private Date endDate;

	private DateTimeConverter convertDate;

	private Category[] categories = Category.values();

	private List<String> catSelection = new ArrayList<>();

	private List<IStream> streams;

	@PostConstruct
	public void init() {
		for (Category category : categories) {
			catSelection.add(category.name());
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
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public DateTimeConverter getConvertDate() {
		return convertDate;
	}

	public void setConvertDate(DateTimeConverter convertDate) {
		final String basename = "com.glue.messages.Messages";
		final String key1 = "date_format_long";
		// final String key2 = "time_format";

		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle(basename, context.getViewRoot().getLocale());

		String dateFormat = bundle.getString(key1);
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
		return catSelection;
	}

	/**
	 * @param catSelection
	 *            the catSelection to set
	 */
	public void setCatSelection(List<String> catSelection) {
		this.catSelection = catSelection;
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
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
		}

		return "stream-search";
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
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
		} catch (InternalServerException e) {
			LOG.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
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
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
		} catch (InternalServerException e) {
			LOG.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
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
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
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
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
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
	 * Returns the content of the style attribute to apply to the given category
	 * selector that has just been toggled.
	 * 
	 * @param cat
	 * @return
	 */
	public String getStyle(String cat) {
		String styleAttr = "";
		if (catSelection.contains(cat)) {
			styleAttr = "border-bottom: 3px solid " + Category.valueOf(cat).getColor() + ";";
		}

		return styleAttr;
	}

	public void enableCategory() {
		String cat = FacesUtil.getRequestParameter(PARAM_CAT);
		LOG.debug("Toggle category = " + cat);

		if (!catSelection.remove(cat)) {
			catSelection.add(cat);
		}

		streamController.setCategories(catSelection.toArray(new String[catSelection.size()]));

		try {
			streams = streamController.search();
			// streams = streamController.next();
		} catch (InternalServerException e) {
			LOG.error(e.getMessage(), e);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Holy guacamole! You got an error."));
		}
	}

}
