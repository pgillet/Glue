package com.glue.feed.html.demo;

import static com.glue.feed.html.EventSelectorsBuilder.newEventSelectors;
import static com.glue.feed.html.SiteMapBuilder.newSiteMap;
import static com.glue.feed.html.VenueSelectorsBuilder.newVenueSelectors;

import java.util.Locale;

import com.glue.domain.Event;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.html.EventMappingStrategy;
import com.glue.feed.html.EventSelectors;
import com.glue.feed.html.HTMLFeedParser;
import com.glue.feed.html.HTMLMappingStrategy;
import com.glue.feed.html.SiteMap;
import com.glue.feed.html.VenueSelectors;
import com.glue.feed.listener.StreamMessageListener;

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
		.url("http://culture.toulouse.fr/web/guest/agenda/-/agenda/event/")
		// finds sibling <a> element immediately preceded by sibling
		// <strong> element designing the current page
		.next("strong.journal-article-page-number + a.journal-article-page-number")
		.last("div#nextpage:not(:has(a))").build();

	VenueSelectors venueSelectors = newVenueSelectors()
		.withName("div.content > h1.title")
		.withAddress("div.address > div.description > p")
		.withThumbnail("img.location-image")
		.withDescription("div.desccription")
		.withWebsite("div.address > div.description > div > a").build();

	// 2nd step: describe the structure of an event details page
	// Note: The selectors are the same as the ones defined in the
	// BikiniEvent class
	EventSelectors eventSelectors = newEventSelectors()
		.withRootBlock("div.article-event")
		.withTitle("span.title-text")
		.withDescription("div.description")
		// .withDatePattern("E dd MMM yyyy")
		// Ex: vendredi 21 février 2014 à 20:30
		// .withEventType("div#blocContenu > div#type")
		.withThumbnail("div.image-event > img")
		// .withPrice("div#blocContenu > div#prix")
		.withDates("h1.event-title + div.block-child")
		.withLocale(Locale.FRENCH)
		.withVenueLink("a[href*=/locations]")
		.withVenueSelectors(venueSelectors)
		.build();



	HTMLMappingStrategy<Event> mappingStrategy = new EventMappingStrategy(
		eventSelectors);

	HTMLFeedParser<Event> parser = new HTMLFeedParser<>(siteMap,
		mappingStrategy);

	final FeedMessageListener feedMsgListener = new StreamMessageListener();
	// Uncomment to persist in db
	// parser.setFeedMessageListener(feedMsgListener);

	parser.read();
	parser.close();

	System.out.println("We're done here.");
    }
}
