package com.glue.bot;

import java.io.IOException;

import org.jsoup.nodes.Element;

import com.glue.bot.command.DateTimeCommand;
import com.glue.bot.command.DescriptionCommand;
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
import com.glue.time.DateTimeProcessor;

public class EventMapper implements HtmlMapper<Event> {

    private EventSelectors selectors;

    private Event eventTemplate = new Event();

    private DateTimeProcessor dateTimeProcessor;

    private HtmlFetcher hf = new HtmlFetcher();

    public EventMapper(EventSelectors selectors) throws IOException {
	this(selectors, null);
    }

    public EventMapper(EventSelectors selectors, Event eventTemplate)
	    throws IOException {
	this.selectors = selectors;
	this.eventTemplate = eventTemplate;
	dateTimeProcessor = new DateTimeProcessor();
    }

    public Event getEventTemplate() {
	return eventTemplate;
    }

    public void setEventTemplate(Event eventTemplate) {
	this.eventTemplate = eventTemplate;
    }

    @Override
    public Event parse(Element e) throws Exception {

	Chain chain = new ChainBase();

	chain.addCommand(new ForwardCommand());
	chain.addCommand(new NarrowerCommand());
	chain.addCommand(new InitEventCommand());
	chain.addCommand(new TitleCommand());
	chain.addCommand(new DescriptionCommand());
	chain.addCommand(new DateTimeCommand());
	chain.addCommand(new ImageCommand());
	chain.addCommand(new PriceListCommand());

	Context context = new ContextBase();
	context.put(SelectorKeys.ELEMENT_KEY, e);
	context.put(SelectorKeys.ROOT_BLOCK_SELECTOR_KEY, selectors.getRootBlock());
	context.put(SelectorKeys.EVENT_TEMPLATE_KEY, eventTemplate);
	context.put(SelectorKeys.TITLE_SELECTOR_KEY, selectors.getTitle());
	context.put(SelectorKeys.DESCRIPTION_SELECTOR_KEY,
		selectors.getDescription());
	context.put(SelectorKeys.DATE_PATTERN_KEY,
		selectors.getDatePattern());
	context.put(SelectorKeys.DATE_SELECTOR_KEY, selectors.getDates());
	context.put(SelectorKeys.LOCALE_KEY, selectors.getLocale());
	context.put(SelectorKeys.IMAGE_SELECTOR_KEY, selectors.getThumbnail());
	context.put(SelectorKeys.PRICE_SELECTOR_KEY, selectors.getPrice());

	chain.execute(context);

	Event event = (Event) context.get(SelectorKeys.EVENT_KEY);

	// Venue
	VenueSelectors venueSelectors = selectors.getVenueSelectors();
	if (venueSelectors != null) {

	    // Venue description for each event description

	    VenueMapper vms = new VenueMapper(venueSelectors);
	    if (eventTemplate != null) {
		vms.setVenueTemplate(eventTemplate.getVenue());
	    }

	    Venue venue = vms.parse(e);
	    event.setVenue(venue);
	}

	return event;
    }

}
