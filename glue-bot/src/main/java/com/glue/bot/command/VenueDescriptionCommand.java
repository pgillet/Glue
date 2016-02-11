package com.glue.bot.command;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Venue;

public class VenueDescriptionCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(VenueDescriptionCommand.class);

    private String descriptionSelectorKey = SelectorKeys.DESCRIPTION_SELECTOR_KEY;


    public String getDescriptionSelectorKey() {
	return descriptionSelectorKey;
    }

    public void setDescriptionSelectorKey(String descriptionSelectorKey) {
	this.descriptionSelectorKey = descriptionSelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Venue venue = (Venue) context.get(getVenueKey());
	String descriptionSelector = (String) context
		.get(getDescriptionSelectorKey());

	if (descriptionSelector != null) {
	    String description = elem.select(descriptionSelector).text();
	    venue.setDescription(StringUtils.defaultIfBlank(description,
		    venue.getDescription()));
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
