package com.glue.webapp.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.content.ContentManager;
import com.glue.domain.Event;
import com.glue.domain.Image;

@ManagedBean
@ApplicationScoped
public class EventUtilBean {

    static final Logger LOG = LoggerFactory.getLogger(EventUtilBean.class);

    @Inject
    private ContentManager cm;

    public boolean hasStickyImage(Event event) {
	return (getStickyImage(event) != null);
    }

    public Image getStickyImage(Event event) {

	List<Image> images = event.getImages();

	Iterator<Image> it = images.iterator();
	while (it.hasNext()) {
	    Image image = it.next();
	    if (image.isSticky()) {
		return image;
	    }
	}

	return null;
    }

    public String getStickyImageURI(Event event) {
	Image image = getStickyImage(event);
	if (image != null) {
	    String url = image.getOriginal().getUrl();
	    String name = FilenameUtils.getName(url);

	    String other = cm.getEventCAO().getDocumentURL(name, event);
	    return (other != null ? other : url);
	}

	return null;
    }

}
