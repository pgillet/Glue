package com.glue.webapp.beans;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;

@ManagedBean
public class StreamSearchBean {

	@Inject
	private StreamController streamController;

	private String query;

	private String location;

	private double latitude;

	private double longitude;

	private Date startDate;

	private Date endDate;

	private List<IStream> streams;

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
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

	/**
	 * @return the streams
	 */
	public List<IStream> getStreams() {
		return streams;
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
			streams = streamController.search(query);
		} catch (InternalServerException e) {
			context.addMessage(null, new FacesMessage(
					"Holy guacamole! You got an error."));
		}

		return "stream-search";
	}

}
