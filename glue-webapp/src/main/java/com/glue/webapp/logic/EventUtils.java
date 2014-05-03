package com.glue.webapp.logic;

import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class EventUtils {

    private static final String ELLIPSIS = "...";
    private static final int SUMMARY_LIMIT = 200;

    public static String summarize(String html) {
	// Keep only text nodes: all HTML will be stripped.
	String onlytext = Jsoup.clean(html, Whitelist.none());
	String summary = WordUtils.abbreviate(onlytext, SUMMARY_LIMIT, -1,
		ELLIPSIS);

	return summary;
    }

}
