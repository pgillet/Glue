package com.glue.bot.command;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Venue;

public class AddressCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(AddressCommand.class);

    private String venueAddressSelectorKey = SelectorKeys.VENUE_ADDRESS_SELECTOR_KEY;

    public String getVenueAddressSelectorKey() {
	return venueAddressSelectorKey;
    }

    public void setVenueAddressSelectorKey(String venueAddressSelectorKey) {
	this.venueAddressSelectorKey = venueAddressSelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Venue venue = (Venue) context.get(getVenueKey());
	String venueAddressSelector = (String) context
		.get(getVenueAddressSelectorKey());

	if (venueAddressSelector != null) {
	    String address = elem.select(venueAddressSelector).text();
	    venue.setAddress(StringUtils.defaultIfBlank(address,
		    venue.getAddress()));
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
