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
import com.glue.content.ImageRendition;
import com.glue.domain.Event;
import com.glue.domain.Image;

@ManagedBean
@ApplicationScoped
public class EventUtilBean {

    static final Logger LOG = LoggerFactory.getLogger(EventUtilBean.class);

    @Inject
    private ContentManager cm;

    private Boolean cmisAvailable;

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
	    String name = FilenameUtils.getBaseName(url);

	    String other = null;
	    if (isCmisAvailable()) {
		other = cm.getEventCAO().getImageURL(name, event,
			ImageRendition.THUMBNAIL);
	    }
	    return (other != null ? other : url);
	}

	return null;
    }

    private boolean isCmisAvailable() {
	if (cmisAvailable == null) {
	    // Cannot perform this test in @PostConstruct
	    // as we cannot be sure that glue-content starts before glue-webapp
	    // starts itself.
	    // According to the Tomcat Wiki, <a
	    // href="http://wiki.apache.org/tomcat/FAQ/Miscellaneous#Q27">What
	    // order do webapps start (or how can I change startup order)?</a>
	    // There is no expected startup order. Neither the Servlet spec nor
	    // Tomcat define one. You can't rely on the apps starting in any
	    // particular order.
	    cmisAvailable = cm.ping();
	}

	return cmisAvailable;
    }

}
