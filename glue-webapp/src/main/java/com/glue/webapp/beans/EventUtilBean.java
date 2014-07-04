package com.glue.webapp.beans;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.content.ContentManager;
import com.glue.content.ImageRendition;
import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Image;
import com.glue.domain.Link;
import com.glue.domain.LinkType;

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

    /**
     * Returns the CSS styles to apply to the given category selector.
     * 
     * @param cat
     *            the category name
     * @param javascriptSyntax
     *            a boolean telling whether the method should return the
     *            JavaScript syntax or not
     * @param enabled
     *            a boolean telling whether the category is selected or not.
     * @param onmouseover
     *            a boolean telling whether the method should return the CSS
     *            styles to be applied when the pointer is moved onto the
     *            element (onMouseOver attribute) or away from it (onMouseOut
     *            attribute). This parameter is ignored if javascriptSyntax is
     *            set to false.
     * @return
     */
    public String getCategoryStyle(String cat, boolean javascriptSyntax,
	    boolean enabled, boolean onmouseover) {
	String styleAttr;
	if (javascriptSyntax) {
	    // JavaScript syntax
	    styleAttr = "this.style.paddingBottom = '%dpx'; this.style.borderBottom = '%dpx solid %s' ;";
	} else {
	    styleAttr = "padding-bottom: %dpx; border-bottom: %dpx solid %s ;";
	}
	// We compensate for the height of the border with the padding and vice
	// versa
	int borderWidth;
	int paddingBottom;
	if (enabled || onmouseover) {
	    paddingBottom = 3;
	    borderWidth = 5;
	} else {
	    paddingBottom = 6;
	    borderWidth = 2;
	}

	styleAttr = String.format(styleAttr, paddingBottom, borderWidth,
		EventCategory.valueOf(cat).getColor());

	return styleAttr;
    }

    public String getBookingLink(Event event) {
	Set<Link> links = event.getLinks();
	for (Link link : links) {
	    if (LinkType.TICKET.equals(link.getType())) {
		return link.getUrl();
	    }
	}

	return null;
    }

}
