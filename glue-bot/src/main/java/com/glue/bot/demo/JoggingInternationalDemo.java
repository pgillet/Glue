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
public class JoggingInternationalDemo {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.jogging-international.net/courses/calendrier/france-metropolitaine#resultats")
		.li("div.course_line").next("p#navigation span.next")
		.last("p#navigation:not(:has(span.next))")
		// .url("http://www.jogging-international.net/courses/calendrier/france-metropolitaine/")
		.build();

	EventSelectors eventSelectors = newEventSelectors()
		.rootBlock("div.contenu")
		.title("div.item_titre > h2")
		.description("div.item_titre > h2")
		// .withEventType("div#blocContenu > div#type")
		// .withThumbnail("div#blocImage > a")
		// .withPrice("div#blocContenu > div#prix")
		.dates("table.tab_identite tr:eq(0) td.droite")
		// .withDatePattern("dd/MM/yyyy")
		// 05/01/2014
		.locale(Locale.FRENCH).build();

	VenueSelectors venueSelectors = newVenueSelectors()
		.name("table.tab_identite tr:eq(2) td.droite")
		.address("table.tab_identite tr:eq(2) td.droite").build();

	HtmlMapper<Event> mappingStrategy = new EventMapper(
		eventSelectors);

	Crawler<Event> parser = new Crawler<>(siteMap,
		mappingStrategy);

	// final FeedMessageListener feedMsgListener = new
	// StreamMessageListener();
	// Uncomment to persist in db
	// parser.setFeedMessageListener(feedMsgListener);

	parser.run();

	System.out.println("We're done here.");
    }
}
