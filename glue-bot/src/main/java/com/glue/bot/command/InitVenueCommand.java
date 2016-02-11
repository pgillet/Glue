package com.glue.bot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Venue;

public class InitVenueCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(InitVenueCommand.class);

    private String venueTemplateKey = SelectorKeys.VENUE_TEMPLATE_KEY;

    public String getVenueTemplateKey() {
	return venueTemplateKey;
    }

    public void setVenueTemplateKey(String venueTemplateKey) {
	this.venueTemplateKey = venueTemplateKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Venue venueTemplate = (Venue) context.get(getVenueTemplateKey());

	Venue venueValue = new Venue();
	context.put(getVenueKey(), venueValue);

	if (venueTemplate != null) {


	    venueValue.setAddress(venueTemplate.getAddress());
	    venueValue.setChildren(venueTemplate.getChildren());
	    venueValue.setCity(venueTemplate.getCity());
	    venueValue.setComments(venueTemplate.getComments());
	    venueValue.setCountry(venueTemplate.getCountry());
	    venueValue.setCountryThreeLetterAbbreviation(venueTemplate
		    .getCountryThreeLetterAbbreviation());
	    venueValue.setCountryTwoLetterAbbreviation(venueTemplate
		    .getCountryTwoLetterAbbreviation());
	    venueValue.setCreated(venueTemplate.getCreated());
	    venueValue.setDescription(venueTemplate.getDescription());
	    venueValue.setEvents(venueTemplate.getEvents());
	    venueValue.setGeocodeType(venueTemplate.getGeocodeType());
	    venueValue.setId(venueTemplate.getId());
	    venueValue.setImages(venueTemplate.getImages());
	    venueValue.setLatitude(venueTemplate.getLatitude());
	    venueValue.setLongitude(venueTemplate.getLongitude());
	    venueValue.setLinks(venueTemplate.getLinks());
	    venueValue.setName(venueTemplate.getName());
	    venueValue.setParent(venueTemplate.getParent());
	    venueValue.setPostalCode(venueTemplate.getPostalCode());
	    venueValue.setProperties(venueTemplate.getProperties());
	    venueValue.setReference(venueTemplate.isReference());
	    venueValue.setRegion(venueTemplate.getRegion());
	    venueValue.setRegionAbbreviation(venueTemplate
		    .getRegionAbbreviation());
	    venueValue.setSource(venueTemplate.getSource());
	    venueValue.setTags(venueTemplate.getTags());
	    venueValue.setTimeZone(venueTemplate.getTimeZone());
	    venueValue.setType(venueTemplate.getType());
	    venueValue.setUrl(venueTemplate.getUrl());
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
