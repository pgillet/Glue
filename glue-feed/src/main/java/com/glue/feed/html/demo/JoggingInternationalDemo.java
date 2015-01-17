package com.glue.feed.html.demo;

import static com.glue.feed.html.EventSelectorsBuilder.newEventSelectors;
import static com.glue.feed.html.SiteMapBuilder.newSiteMap;
import static com.glue.feed.html.VenueSelectorsBuilder.newVenueSelectors;

import java.util.Locale;

import com.glue.domain.Event;
import com.glue.feed.html.EventMappingStrategy;
import com.glue.feed.html.EventSelectors;
import com.glue.feed.html.HTMLFeedParser;
import com.glue.feed.html.HTMLMappingStrategy;
import com.glue.feed.html.SiteMap;
import com.glue.feed.html.VenueSelectors;

/**
 * Demonstration of the DirectMappingStrategy.
 * 
 * @see ScrapingDemo
 * 
 * @author pgillet
 * 
 */
public class JoggingInternationalDemo {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.jogging-international.net/courses/calendrier/france-metropolitaine#resultats")
		.list("div.course_line").next("p#navigation span.next")
		.last("p#navigation:not(:has(span.next))")
		// .url("http://www.jogging-international.net/courses/calendrier/france-metropolitaine/")
		.build();

	EventSelectors eventSelectors = newEventSelectors()
		.withRootBlock("div.contenu")
		.withTitle("div.item_titre > h2")
		.withDescription("div.item_titre > h2")
		// .withEventType("div#blocContenu > div#type")
		// .withThumbnail("div#blocImage > a")
		// .withPrice("div#blocContenu > div#prix")
		.withDates("table.tab_identite tr:eq(0) td.droite")
		// .withDatePattern("dd/MM/yyyy")
		// 05/01/2014
		.withLocale(Locale.FRENCH).build();

	VenueSelectors venueSelectors = newVenueSelectors()
		.withName("table.tab_identite tr:eq(2) td.droite")
		.withAddress("table.tab_identite tr:eq(2) td.droite").build();

	HTMLMappingStrategy<Event> mappingStrategy = new EventMappingStrategy(
		eventSelectors);

	HTMLFeedParser<Event> parser = new HTMLFeedParser<>(siteMap,
		mappingStrategy);

	// final FeedMessageListener feedMsgListener = new
	// StreamMessageListener();
	// Uncomment to persist in db
	// parser.setFeedMessageListener(feedMsgListener);

	parser.read();
	parser.close();

	System.out.println("We're done here.");
    }
}
