package com.glue.webapp.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;

@ManagedBean
public class StreamItemBean {

    static final Logger LOG = LoggerFactory.getLogger(StreamItemBean.class);

    @Inject
    private StreamController streamController;

    private static final String PARAM_ID = "id";
    private Event item;

    @PostConstruct
    public void init() {
	String id = FacesUtil.getRequestParameter(PARAM_ID);

	if (id != null) {
	    FacesContext context = FacesContext.getCurrentInstance();

	    try {
		item = streamController.search(id);

		if (item == null) {
		    context.addMessage(
			    null,
			    new FacesMessage(FacesUtil
				    .getString("no.such.stream")));
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

}
