package com.glue.webapp.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Tag;
import com.glue.domain.User;
import com.glue.domain.Venue;
import com.glue.webapp.logic.EventController;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.UserController;

@ManagedBean
public class StreamBean {

    static final Logger LOG = LoggerFactory.getLogger(StreamBean.class);

    @Inject
    EventController eventController;
    UserController userController = new UserController();

    private String title;

    private String description;

    private Date startDate;

    private Date endDate;

    private double latitude;

    private double longitude;

    private String address;

    private Set<String> tags;

    private String thumbPath;

    public StreamBean() {
	Calendar calendar = Calendar.getInstance();
	startDate = calendar.getTime();
	calendar.add(Calendar.HOUR_OF_DAY, 2);
	endDate = calendar.getTime();
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
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
    public void setStartTime(Date startDate) {
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
     * @return the address
     */
    public String getAddress() {
	return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
	this.address = address;
    }

    /**
     * @return the tags
     */
    public Set<String> getTags() {
	return tags;
    }

    /**
     * @param tags
     *            the tags to set
     */
    public void setTags(Set<String> tags) {
	this.tags = tags;
    }

    /**
     * @return the thumbPath
     */
    public String getThumbPath() {
	return thumbPath;
    }

    /**
     * @param thumbPath
     *            the thumbPath to set
     */
    public void setThumbPath(String thumbPath) {
	this.thumbPath = thumbPath;
    }

    public String create() {

	FacesContext context = FacesContext.getCurrentInstance();
	HttpServletRequest request = (HttpServletRequest) context
		.getExternalContext().getRequest();

	User authenticatedUser = (User) request.getUserPrincipal();

	Event event = new Event();
	event.setDescription(description);
	event.setStartTime(startDate);
	event.setStopTime(endDate);

	Set<Tag> l = new HashSet<>();
	for (String str : tags) {
	    Tag tag = new Tag();
	    tag.setTitle(str);
	    l.add(tag);
	}

	event.setTags(l);

	event.setTitle(title);

	Venue venue = new Venue();
	venue.setAddress(address);
	venue.setLatitude(latitude);
	venue.setLongitude(longitude);

	try {
	    eventController.createEvent(event);
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    context.addMessage(null, new FacesMessage(e.getMessage()));
	}

	return "main";
    }

}
