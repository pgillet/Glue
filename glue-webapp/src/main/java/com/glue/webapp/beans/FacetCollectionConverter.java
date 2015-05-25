package com.glue.webapp.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.solr.client.solrj.response.FacetField;

@FacesConverter("FacetCollectionConverter")
public class FacetCollectionConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {
	List<FacetField.Count> l = null;
	if (value != null) {
	    String str = value.trim();
	    if (str.length() > 0) {
		String[] tokens = str.split(",");
		l = new ArrayList<>();

		for (String token : tokens) {
		    int i = token.indexOf(":");
		    FacetField ff = new FacetField(token.substring(0, i));
		    FacetField.Count obj = new FacetField.Count(ff,
			    token.substring(i + 1), -1);
		    l.add(obj);
		}

	    } else {
		// Empty list
		l = new ArrayList<>();
	    }
	}

	return l;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
	    Object value) {
	if (value == null) {
	    // Return a zero-length string
	    return "";
	}

	Iterator<FacetField.Count> it = ((Collection<FacetField.Count>) value)
		.iterator();
	if (!it.hasNext()) {
	    // Return a zero-length string
	    return "";
	}

	StringBuilder sb = new StringBuilder();

	for (;;) {
	    FacetField.Count e = it.next();

	    // String filterQuery = e.getFacetField().getName() + ":" + "\""
	    // + e.getName() + "\"";

	    String filterQuery = e.getFacetField().getName() + ":"
		    + e.getName();

	    sb.append(filterQuery);
	    if (!it.hasNext()) {
		return sb.toString();
	    }
	    sb.append(",");
	}
    }

}
