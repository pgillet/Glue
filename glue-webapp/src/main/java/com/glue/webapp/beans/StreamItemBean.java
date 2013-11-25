package com.glue.webapp.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;

@ManagedBean
public class StreamItemBean {

	static final Logger LOG = LoggerFactory.getLogger(StreamItemBean.class);

	private static final String ERROR_MESSAGE = "Holy guacamole! You got an error.";

	private static final String ERROR_MESSAGE_2 = "This stream does not exist";

	@Inject
	private StreamController streamController;

	private static final String PARAM_ID = "id";
	private IStream item;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		String id = FacesUtil.getRequestParameter(PARAM_ID);

		try {
			item = streamController.search(Long.valueOf(id));

			if (item == null) {
				context.addMessage(null, new FacesMessage(ERROR_MESSAGE_2));
			}

		} catch (NumberFormatException e) {
			LOG.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(ERROR_MESSAGE_2));
		} catch (InternalServerException e) {
			LOG.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(ERROR_MESSAGE));
		}
	}

	/**
	 * @return the item
	 */
	public IStream getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(IStream item) {
		this.item = item;
	}

}
