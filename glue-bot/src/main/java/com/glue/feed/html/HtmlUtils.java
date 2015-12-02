package com.glue.feed.html;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HtmlUtils {

    private static HtmlCompressor compressor = new HtmlCompressor();

    private HtmlUtils() {
    }

    /**
     * Sanitizes HTML from input HTML, by parsing input HTML and filtering it
     * through a basic white-list of permitted tags and attributes.
     * Additionally, the resulting HTML is minified.
     */
    public static String cleanHtml(String str) {
	String bodyHtml = Jsoup.clean(str, Whitelist.basic());

	// Then compress
	compressor.setRemoveIntertagSpaces(true);
	bodyHtml = compressor.compress(bodyHtml);

	return bodyHtml;
    }

}
