package com.glue.webapp.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang.WordUtils;

@ManagedBean
@ApplicationScoped
public class FacesUtilBean {

    public String abbreviate(String str, int lowerLimit) {
	return WordUtils.abbreviate(str, lowerLimit, -1, "...");
    }

}
