package com.glue.bot.command;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Venue;

public class WebsiteCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(WebsiteCommand.class);

    private String websiteSelectorKey = SelectorKeys.WEBSITE_SELECTOR_KEY;

    public String getWebsiteSelectorKey() {
	return websiteSelectorKey;
    }

    public void setWebsiteSelectorKey(String websiteSelectorKey) {
	this.websiteSelectorKey = websiteSelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Venue venue = (Venue) context.get(getVenueKey());
	String websiteSelector = (String) context
		.get(getWebsiteSelectorKey());

	if (websiteSelector != null) {
	    String url = elem.select(websiteSelector).text();
	    venue.setUrl(StringUtils.defaultIfBlank(url, venue.getUrl()));
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
