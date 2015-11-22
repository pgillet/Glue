package com.glue.feed.html.demo;

import static com.glue.feed.html.EventSelectorsBuilder.newEventSelectors;
import static com.glue.feed.html.SiteMapBuilder.newSiteMap;
import static com.glue.feed.html.VenueSelectorsBuilder.newVenueSelectors;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Venue;
import com.glue.feed.html.EventMappingStrategy;
import com.glue.feed.html.EventSelectors;
import com.glue.feed.html.HTMLFeedParser;
import com.glue.feed.html.HTMLMappingStrategy;
import com.glue.feed.html.SiteMap;
import com.glue.feed.html.VenueSelectors;

/**
 * Demonstration of the EventMappingStrategy.
 * 
 * See http://jsoup.org/apidocs/org/jsoup/select/Selector.html for the syntax of
 * the CSS-like element selectors.
 * 
 * @see ScrapingDemo
 * 
 * @author pgillet
 * 
 */
public class BikiniScraper {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.lebikini.com/programmation/index/date/new").li(
		"a[href^=/programmation/concert/]")
		.build();

	VenueSelectors venueSelectors = newVenueSelectors()
		.name("div#infos > div#salle > h3")
		.address("div#infos > div#salle > div#adresse").build();

	// 2nd step: describe the structure of an event details page
	// Note: The selectors are the same as the ones defined in the
	// BikiniEvent class
	EventSelectors eventSelectors = newEventSelectors()
		.rootBlock("div#encartDetailSpectacle")
		.title("div#blocContenu > h2").description("div#texte")
		.eventType("div#blocContenu > div#type")
		.thumbnail("div#blocImage > a")
		.price("div#blocContenu > div#prix")
		.dates("div#blocContenu > div#date")
		// .withDatePattern("E dd MMM yyyy 'à' HH:mm")
		// Ex: vendredi 21 février 2014 à 20:30
		// .withLocale(Locale.FRENCH)
		.venueSelectors(venueSelectors).build();

	// Constants
	Venue venueRef = new Venue();
	venueRef.setCity("Toulouse");
	venueRef.setCountry("France");

	Event eventRef = new Event();
	eventRef.setCategory(EventCategory.MUSIC);
	eventRef.setVenue(venueRef); // Important !

	HTMLMappingStrategy<Event> mappingStrategy = new EventMappingStrategy(
		eventSelectors, eventRef);

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
