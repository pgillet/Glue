package com.glue.feed.html.demo;

import static com.glue.feed.html.EventSelectorsBuilder.newEventSelectors;
import static com.glue.feed.html.SiteMapBuilder.newSiteMap;
import static com.glue.feed.html.VenueSelectorsBuilder.newVenueSelectors;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.feed.html.Crawler;
import com.glue.feed.html.EventMappingStrategy;
import com.glue.feed.html.EventSelectors;
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

    public static void testBikini() throws Exception {

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

	// Template
	Venue venueRef = new Venue();
	venueRef.setCity("Toulouse");
	venueRef.setCountry("France");

	Event eventRef = new Event();
	eventRef.setCategory(EventCategory.MUSIC);
	eventRef.setVenue(venueRef); // Important !

	HTMLMappingStrategy<Event> mappingStrategy = new EventMappingStrategy(
		eventSelectors, eventRef);

	Crawler<Event> parser = new Crawler<>(siteMap,
		mappingStrategy);

	parser.run();

	System.out.println("We're done here.");
    }

    public static void testTheatreDuGrandRond() throws Exception {
	// Théâtre du Grand Rond - Tout Public
	SiteMap siteMap = newSiteMap(
		"http://www.grand-rond.org/index.php/grandrond/programmation")
		// .list("td a.savoir_plus:containsOwn(En savoir plus)")
		.li("a.savoir_plus[href^=http://www.grand-rond.org/index.php/grandrond/spectacle?]")
		.build();

	EventSelectors eventSelectors = newEventSelectors()
		.rootBlock("div#principal")
		.title("span.titrespec")
		.description(
			"#principal > table:nth-child(4) > tbody > tr > td")
		.thumbnail("#show > div > a > img")
		.build();
	
	
	// VenueSelectors venueSelectors = newVenueSelectors()
	// .name("#block_gauche > strong")
	// .address(
	// "#block_gauche > br:nth-child(3), #block_gauche > br:nth-child(4)")
	// .phoneNumber("#block_gauche > span").build();

	Venue venueRef = new Venue();
	venueRef.setName("Théâtre du Grand Rond");
	venueRef.setAddress("23 rue des Potiers 31000 Toulouse");
	venueRef.setCity("Toulouse");
	venueRef.setCountry("France");

	Event eventRef = new Event();
	eventRef.setCategory(EventCategory.PERFORMING_ART);
	eventRef.getTags().add(new Tag("Tout public"));
	eventRef.setVenue(venueRef); // Important !

	HTMLMappingStrategy<Event> mappingStrategy = new EventMappingStrategy(
		eventSelectors, eventRef);

	Crawler<Event> parser = new Crawler<>(siteMap,
		mappingStrategy);

	parser.run();

	System.out.println("We're done here.");

    }

    public static void main(String[] args) throws Exception {
	testBikini();
    }

}
