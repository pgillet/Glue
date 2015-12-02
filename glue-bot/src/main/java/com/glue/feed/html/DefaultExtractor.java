package com.glue.feed.html;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExtractor implements Extractor {

    static final Logger LOG = LoggerFactory
	    .getLogger(DefaultExtractor.class);

    @Override
    public void process(Element e) throws Exception {
	LOG.info(e.toString());
    }

}
