package com.glue.feed.html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVisitorListener implements VisitorListener {

    static final Logger LOG = LoggerFactory
	    .getLogger(DefaultVisitorListener.class);

    @Override
    public void processLink(String linkHref) throws Exception {
	LOG.info("Event details page = " + linkHref);
    }

}
