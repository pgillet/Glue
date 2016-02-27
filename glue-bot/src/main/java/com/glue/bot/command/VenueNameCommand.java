package com.glue.bot.command;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.bot.Validate;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.chain.Contract;
import com.glue.domain.Venue;

public class VenueNameCommand extends BaseCommand implements Contract {

    public static final Logger LOG = LoggerFactory
	    .getLogger(VenueNameCommand.class);

    private String venueNameSelectorKey = SelectorKeys.VENUE_NAME_SELECTOR_KEY;

    public String getVenueNameSelectorKey() {
	return venueNameSelectorKey;
    }

    public void setVenueNameSelectorKey(String venueNameSelectorKey) {
	this.venueNameSelectorKey = venueNameSelectorKey;
    }

    @Override
    public void require(Context context) throws Exception {
	// No op

    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Venue venue = (Venue) context.get(getVenueKey());

	String venueNameSelector = (String) context
		.get(getVenueNameSelectorKey());

	Elements elems = elem.select(venueNameSelector);
	Validate.single(elems);
	String name = elems.text();
	venue.setName(StringUtils.defaultIfBlank(name, venue.getName()));

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

    @Override
    public void ensure(Context context) throws Exception {
	Venue venue = (Venue) context.get(getVenueKey());
	String name = venue.getName();

	if (name == null) {
	    throw new IllegalArgumentException("Name is null");
	}

    }

}
