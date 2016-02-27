package com.glue.bot;

import java.io.IOException;

import org.jsoup.nodes.Element;

import com.glue.bot.command.CacheVisitedCommand;
import com.glue.bot.command.DateTimeCommand;
import com.glue.bot.command.DescriptionCommand;
import com.glue.bot.command.EventUrlCommand;
import com.glue.bot.command.ForwardCommand;
import com.glue.bot.command.ImageCommand;
import com.glue.bot.command.InitEventCommand;
import com.glue.bot.command.NarrowerCommand;
import com.glue.bot.command.PriceListCommand;
import com.glue.bot.command.TitleCommand;
import com.glue.chain.Chain;
import com.glue.chain.Context;
import com.glue.chain.impl.ChainBase;
import com.glue.chain.impl.ContextBase;
import com.glue.domain.Event;
import com.glue.domain.Venue;

public class EventMapper implements HtmlMapper<Event> {

    private EventSelectors selectors;

    private Event eventTemplate = new Event();

    private Chain chain;

    private VenueMapper venueMapper;

    public EventMapper(EventSelectors selectors) throws Exception {
	this(selectors, null);
    }

    public EventMapper(EventSelectors selectors, Event eventTemplate)
	    throws Exception {
	this.selectors = selectors;
	this.eventTemplate = eventTemplate;
	validate();
	init();
    }

    public Event getEventTemplate() {
	return eventTemplate;
    }

    public void setEventTemplate(Event eventTemplate) {
	this.eventTemplate = eventTemplate;
    }

    @Override
    public Event parse(Element e) throws Exception {

	Event event = null;

	// Build a whole new context for each event webpage
	Context context = new ContextBase();
	context.put(SelectorKeys.ELEMENT_KEY, e);
	context.put(SelectorKeys.ROOT_BLOCK_SELECTOR_KEY,
		selectors.getRootBlock());
	context.put(SelectorKeys.EVENT_TEMPLATE_KEY, eventTemplate);
	context.put(SelectorKeys.TITLE_SELECTOR_KEY, selectors.getTitle());
	context.put(SelectorKeys.DESCRIPTION_SELECTOR_KEY,
		selectors.getDescription());
	context.put(SelectorKeys.DATE_PATTERN_KEY, selectors.getDatePattern());
	context.put(SelectorKeys.DATE_SELECTOR_KEY, selectors.getDates());
	context.put(SelectorKeys.LOCALE_KEY, selectors.getLocale());
	context.put(SelectorKeys.IMAGE_SELECTOR_KEY, selectors.getThumbnail());
	context.put(SelectorKeys.PRICE_SELECTOR_KEY, selectors.getPrice());

	/* if ( */chain.execute(context); /* ) { */

	event = (Event) context.get(SelectorKeys.EVENT_KEY);

	if ((event != null) && (venueMapper != null)) {
	    Venue venue = venueMapper.parse(e);
	    event.setVenue(venue);
	}
	/* } */

	return event;
    }

    private void init() throws IOException {
	chain = new ChainBase();

	chain.addCommand(new ForwardCommand());
	chain.addCommand(new CacheVisitedCommand());
	chain.addCommand(new NarrowerCommand());
	chain.addCommand(new InitEventCommand());
	chain.addCommand(new EventUrlCommand());
	chain.addCommand(new TitleCommand());
	chain.addCommand(new DescriptionCommand());
	chain.addCommand(new DateTimeCommand());
	chain.addCommand(new ImageCommand());
	chain.addCommand(new PriceListCommand());

	VenueSelectors venueSelectors = selectors.getVenueSelectors();
	if (venueSelectors != null) {

	    venueMapper = new VenueMapper(venueSelectors);
	    if (eventTemplate != null) {
		venueMapper.setVenueTemplate(eventTemplate.getVenue());
	    }
	}
    }

    private void validate() {

	String title = selectors.getTitle();
	if (title == null || title.length() == 0) {
	    throw new IllegalArgumentException("Title selector is null");
	}

	String dates = selectors.getDates();
	if (dates == null || dates.length() == 0) {
	    throw new IllegalArgumentException("Dates' selector is null");
	}

	VenueSelectors venueSelectors = selectors.getVenueSelectors();
	if (venueSelectors != null) {
	    String venueName = venueSelectors.getVenueName();
	    if (venueName == null || venueName.length() == 0) {
		throw new IllegalArgumentException(
			"Venue name selector is null");
	    }
	}
    }

}
