package com.glue.webapp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * A converter for a list of strings.
 * 
 * @author pgillet
 */
@FacesConverter("StringCollectionConverter")
public class StringCollectionConverter implements Converter {

    /**
     * Converts the given string value composed of strings separated by a comma
     * and followed by a space character into a <code>List</code> of
     * <code>String</code>s.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {
	List<String> l = null;
	if (value != null) {
	    String str = value.trim();
	    if (str.length() > 0) {
		String[] tokens = str.split(",");
		l = new ArrayList<>(Arrays.asList(tokens));
	    } else {
		// Empty list
		l = new ArrayList<>();
	    }
	}

	return l;
    }

    /**
     * Returns a string representation of the given collection. The string
     * representation consists of a list of the collection's elements in the
     * order they are returned by its iterator. Adjacent elements are separated
     * by the character "," (comma). Elements are converted to strings as by
     * String.valueOf(Object).
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
	    Object value) {

	if (value == null) {
	    // Return a zero-length string
	    return "";
	}

	Iterator it = ((Collection) value).iterator();
	if (!it.hasNext()) {
	    // Return a zero-length string
	    return "";
	}

	StringBuilder sb = new StringBuilder();

	for (;;) {
	    Object e = it.next();
	    sb.append(e.toString());
	    if (!it.hasNext()) {
		return sb.toString();
	    }
	    sb.append(",");
	}
    }
}
