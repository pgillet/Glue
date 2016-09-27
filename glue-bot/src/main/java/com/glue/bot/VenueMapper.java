package com.glue.bot;

import org.jsoup.nodes.Element;

import com.glue.bot.command.AddressCommand;
import com.glue.bot.command.CityCommand;
import com.glue.bot.command.ForwardCommand;
import com.glue.bot.command.InitVenueCommand;
import com.glue.bot.command.NarrowerCommand;
import com.glue.bot.command.VenueDescriptionCommand;
import com.glue.bot.command.VenueImageCommand;
import com.glue.bot.command.VenueNameCommand;
import com.glue.bot.command.WebsiteCommand;
import com.glue.bot.domain.VenueSelectors;
import com.glue.chain.Chain;
import com.glue.chain.Context;
import com.glue.chain.impl.ChainBase;
import com.glue.chain.impl.ContextBase;
import com.glue.domain.Venue;

public class VenueMapper implements HtmlMapper<Venue> {

    private HtmlFetcher hf = new HtmlFetcher();
    private VenueSelectors selectors;
    private Venue venueTemplate = new Venue();
    private Chain chain;

    public VenueMapper(VenueSelectors selectors) {
	this(selectors, null);
    }

    public VenueMapper(VenueSelectors selectors, Venue venueTemplate) {
	this.selectors = selectors;
	this.venueTemplate = venueTemplate;

	init();
    }

    public Venue getVenueTemplate() {
	return venueTemplate;
    }

    public void setVenueTemplate(Venue venueTemplate) {
	this.venueTemplate = venueTemplate;
    }

    @Override
    public Venue parse(Element e) throws Exception {

	Context context = new ContextBase();
	context.put(SelectorKeys.ELEMENT_KEY, e);
	context.put(SelectorKeys.ROOT_BLOCK_SELECTOR_KEY,
		selectors.getRootBlock());
	context.put(SelectorKeys.VENUE_TEMPLATE_KEY, venueTemplate);
	context.put(SelectorKeys.VENUE_NAME_SELECTOR_KEY,
		selectors.getVenueName());
	context.put(SelectorKeys.VENUE_ADDRESS_SELECTOR_KEY,
		selectors.getVenueAddress());
	context.put(SelectorKeys.CITY_SELECTOR_KEY, selectors.getCity());
	context.put(SelectorKeys.WEBSITE_SELECTOR_KEY, selectors.getWebsite());
	context.put(SelectorKeys.DESCRIPTION_SELECTOR_KEY,
		selectors.getDescription());
	context.put(SelectorKeys.IMAGE_SELECTOR_KEY, selectors.getThumbnail());

	chain.execute(context);

	Venue venue = (Venue) context.get(SelectorKeys.VENUE_KEY);

	return venue;
    }

    private void init() {
	chain = new ChainBase();

	chain.addCommand(new ForwardCommand());
	chain.addCommand(new NarrowerCommand());
	chain.addCommand(new InitVenueCommand());
	chain.addCommand(new VenueNameCommand());
	chain.addCommand(new AddressCommand());
	chain.addCommand(new CityCommand());
	chain.addCommand(new WebsiteCommand());
	chain.addCommand(new VenueDescriptionCommand());
	chain.addCommand(new VenueImageCommand());
    }

}
