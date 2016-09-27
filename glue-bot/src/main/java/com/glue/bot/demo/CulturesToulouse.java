package com.glue.bot.demo;

import static com.glue.bot.domain.EventSelectorsBuilder.newEventSelectors;
import static com.glue.bot.domain.SiteMapBuilder.newSiteMap;
import static com.glue.bot.domain.VenueSelectorsBuilder.newVenueSelectors;

import java.util.Locale;

import com.glue.bot.Crawler;
import com.glue.bot.EventMapper;
import com.glue.bot.HtmlMapper;
import com.glue.bot.domain.EventSelectors;
import com.glue.bot.domain.SiteMap;
import com.glue.bot.domain.VenueSelectors;
import com.glue.domain.Event;

/**
 * Demonstration of the DirectMappingStrategy.
 * 
 * @see ScrapingDemo
 * 
 * @author pgillet
 * 
 */
public class CulturesToulouse {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://culture.toulouse.fr/web/guest/agenda")
		// .list("div.info-event a.link-event")
		.li("a[href^=http://culture.toulouse.fr/web/guest/agenda/-/agenda/event/]")
		// finds sibling <a> element immediately preceded by sibling
		// <strong> element designing the current page
		.next("strong.journal-article-page-number + a.journal-article-page-number")
		.last("div#nextpage:not(:has(a))").build();

	VenueSelectors venueSelectors = newVenueSelectors()
		.name("div.content > h1.title")
		.address("div.address > div.description > p")
		.thumbnail("img.location-image")
		.description("div.desccription")
		.website("div.address > div.description > div > a").build();

	// 2nd step: describe the structure of an event details page
	// Note: The selectors are the same as the ones defined in the
	// BikiniEvent class
	EventSelectors eventSelectors = newEventSelectors()
		.rootBlock("div.article-event")
		.title("span.title-text")
		.description("div.description")
		// .withDatePattern("E dd MMM yyyy")
		// Ex: vendredi 21 février 2014 à 20:30
		// .withEventType("div#blocContenu > div#type")
		.thumbnail("div.image-event > img")
		// .withPrice("div#blocContenu > div#prix")
		.dates("h1.event-title + div.block-child")
		.locale(Locale.FRENCH)
		.venueLink("a[href*=/locations]")
		.venueSelectors(venueSelectors)
		.build();



	HtmlMapper<Event> mappingStrategy = new EventMapper(
		eventSelectors);

	Crawler<Event> parser = new Crawler<>(siteMap,
		mappingStrategy);

	parser.run();

	System.out.println("We're done here.");
    }
}
