package com.glue.webapp.beans;

import java.net.URISyntaxException;
import java.util.Collection;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewParameter;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMetadata;

import org.apache.http.client.utils.URIBuilder;

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
public class GlueViewHandler extends ViewHandlerWrapper {

    private ViewHandler wrapped;

    public GlueViewHandler(ViewHandler wrapped) {
	this.wrapped = wrapped;
    }

    @Override
    public ViewHandler getWrapped() {
	return wrapped;
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {
	// HttpServletRequest request = (HttpServletRequest) context
	// .getExternalContext().getRequest();
	//
	// // remaining on the same view keeps URL state
	// String requestViewID = request.getRequestURI().substring(
	// request.getContextPath().length());
	// if (requestViewID.equals(viewID)) {
	//
	// // keep RESTful URLs and query strings
	// String action = (String) request
	// .getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
	// if (action == null) {
	// action = request.getRequestURI();
	// }
	// if (request.getQueryString() != null) {
	// return action + "?" + request.getQueryString();
	// } else {
	// return action;
	// }
	// } else {
	//
	// // moving to a new view drops old URL state
	// return super.getActionURL(context, viewID);
	// }

	String actionURL = super.getActionURL(context, viewId);

	Collection<UIViewParameter> viewParams = ViewMetadata
		.getViewParameters(context.getViewRoot());

	if (!viewParams.isEmpty()) {
	    try {
		URIBuilder ub = new URIBuilder(actionURL);

		for (UIViewParameter viewParam : viewParams) {
		    String name = viewParam.getName();
		    Object value = viewParam.getValue();
		    System.out.println("ViewParam " + name + " = " + value);

		    ub.addParameter(name, value.toString());
		}

		actionURL = ub.toString();
	    } catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

	return actionURL;
    }

}
