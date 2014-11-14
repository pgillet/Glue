package com.glue.feed.html.demo;

import static com.glue.feed.html.EventDetailsPageBuilder.newDetails;
import static com.glue.feed.html.SiteMapBuilder.newSiteMap;

import java.util.Locale;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.html.DirectMappingStrategy;
import com.glue.feed.html.EventDetailsPage;
import com.glue.feed.html.HTMLFeedParser;
import com.glue.feed.html.HTMLMappingStrategy;
import com.glue.feed.html.SiteMap;
import com.glue.feed.listener.StreamMessageListener;

/**
 * Demonstration of the DirectMappingStrategy.
 * 
 * @see ScrapingDemo
 * 
 * @author pgillet
 * 
 */
public class ScrapingDemo2 {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.lebikini.com/programmation/index/date/new")
		.url("http://www.lebikini.com/programmation/concert/").build();

	// 2nd step: describe the structure of an event details page
	// Note: The selectors are the same as the ones defined in the
	// BikiniEvent class
	EventDetailsPage details = newDetails()
		.withRootBlock("div#encartDetailSpectacle")
		.withTitle("div#blocContenu > h2").withDescription("div#texte")
		.withEventType("div#blocContenu > div#type")
		.withThumbnail("div#blocImage > a")
		.withPrice("div#blocContenu > div#prix")
		.withStartDate("div#blocContenu > div#date")
		.withDatePattern("E dd MMM yyyy")
		// Ex: vendredi 21 février 2014 à 20:30
		.withVenueName("div#infos > div#salle > h3")
		.withVenueAddress("div#infos > div#salle > div#adresse")
		.withCategory(EventCategory.MUSIC)
		.withLocale(Locale.FRENCH)
		.build();

	HTMLMappingStrategy<Event> mappingStrategy = new DirectMappingStrategy(
		details);

	HTMLFeedParser<Event> parser = new HTMLFeedParser<>(siteMap,
		mappingStrategy);

	final FeedMessageListener feedMsgListener = new StreamMessageListener();
	// Uncomment to persist in db
	parser.setFeedMessageListener(feedMsgListener);

	parser.read();
	parser.close();

	System.out.println("We're done here.");
    }
}
