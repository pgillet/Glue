package com.glue.feed.fnac.event;

import com.glue.feed.URLFilter;

/**
 * An URL filter that rejects the default Fnac image.
 * 
 * @author pgillet
 * 
 */
public class FnacImageFilter implements URLFilter {

    private static final String REGEX = "http://www.francebillet.com/images/picto_.\\.gif";

    @Override
    public boolean accept(String url) {
	return !url.matches(REGEX);
    }

}
