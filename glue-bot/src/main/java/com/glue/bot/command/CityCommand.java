package com.glue.bot.command;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Venue;

public class CityCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory.getLogger(CityCommand.class);

    private String citySelectorKey = SelectorKeys.CITY_SELECTOR_KEY;

    public String getCitySelectorKey() {
	return citySelectorKey;
    }

    public void setCitySelectorKey(String citySelectorKey) {
	this.citySelectorKey = citySelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Venue venue = (Venue) context.get(getVenueKey());

	String citySelector = (String) context.get(getCitySelectorKey());

	if (citySelector != null) {
	    String city = elem.select(citySelector).text();
	    venue.setCity(StringUtils.defaultIfBlank(city, venue.getCity()));
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
