package com.glue.webapp.beans;

import java.net.URISyntaxException;
import java.util.Collection;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewParameter;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMetadata;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see <a href=
 *      "http://www.ninthavenue.com.au/preserving-jsf-request-parameters-and-rest-urls"
 *      />
 * 
 * @see <a href=
 *      "http://stackoverflow.com/questions/10352641/handling-view-parameters-in-jsf-after-post"
 *      />
 * 
 * @author pgillet
 * 
 */
public class ViewParamsHandler extends ViewHandlerWrapper {

    static final Logger LOG = LoggerFactory.getLogger(ViewParamsHandler.class);

    private ViewHandler wrapped;

    public ViewParamsHandler(ViewHandler wrapped) {
	this.wrapped = wrapped;
    }

    @Override
    public ViewHandler getWrapped() {
	return wrapped;
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {

	String actionURL = super.getActionURL(context, viewId);

	Collection<UIViewParameter> viewParams = ViewMetadata
		.getViewParameters(context.getViewRoot());

	if (!viewParams.isEmpty()) {
	    try {
		URIBuilder ub = new URIBuilder(actionURL);

		for (UIViewParameter viewParam : viewParams) {

		    String value = viewParam.getStringValue(context);

		    if (StringUtils.isNotBlank(value)) {
			ub.addParameter(viewParam.getName(), value);
		    }
		}

		actionURL = ub.toString();
	    } catch (URISyntaxException e) {
		LOG.error(e.getMessage(), e);
	    }

	}

	return actionURL;
    }

}
