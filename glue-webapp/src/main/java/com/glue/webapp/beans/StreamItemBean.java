package com.glue.webapp.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Venue;
import com.glue.webapp.logic.EventController;
import com.glue.webapp.logic.InternalServerException;

@ManagedBean
public class StreamItemBean {

    static final Logger LOG = LoggerFactory.getLogger(StreamItemBean.class);

    @Inject
    private EventController eventController;

    private static final String PARAM_ID = "id";
    private Event item;
    private Venue venue;

    @PostConstruct
    public void init() {
	String id = FacesUtil.getRequestParameter(PARAM_ID);

	if (id != null) {
	    FacesContext context = FacesContext.getCurrentInstance();

	    try {
		item = eventController.search(id);

		if (item == null) {
		    context.addMessage(
			    null,
			    new FacesMessage(FacesUtil
				    .getString("no.such.stream")));
		}

		venue = item.getVenue();

		if (venue.getParent() != null) {
		    // The venue has a reference venue
		    venue = venue.getParent();
		}

	    } catch (NumberFormatException e) {
		LOG.error(e.getMessage(), e);
		context.addMessage(null,
			new FacesMessage(FacesUtil.getString("no.such.stream")));
	    } catch (InternalServerException e) {
		LOG.error(e.getMessage(), e);
		context.addMessage(null,
			new FacesMessage(FacesUtil.getString("error.generic")));
	    }
	}
    }

    /**
     * @return the item
     */
    public Event getItem() {
	return item;
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(Event item) {
	this.item = item;
    }

    /**
     * @return the ref venue
     */
    public Venue getVenue() {
	return venue;
    }

    /**
     * @param venue
     *            the venue to set
     */
    public void setVenue(Venue venue) {
	this.venue = venue;
    }

}
