package com.glue.webapp.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.glue.domain.Event;
import com.glue.domain.Image;

@ManagedBean
@ApplicationScoped
public class EventUtilBean {

    public boolean hasStickyImage(Event event) {
	
	List<Image> images = event.getImages();
	
	Iterator<Image> it = images.iterator();
	while (it.hasNext()) {
	    Image image = it.next();
	    if (image.isSticky()) {
		return true;
	    }
	}
	
	return false;
    }

}
