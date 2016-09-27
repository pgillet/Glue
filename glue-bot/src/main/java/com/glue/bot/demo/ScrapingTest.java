package com.glue.bot.demo;

import static com.glue.bot.domain.EventSelectorsBuilder.newEventSelectors;
import static com.glue.bot.domain.SiteMapBuilder.newSiteMap;
import static com.glue.bot.domain.VenueSelectorsBuilder.newVenueSelectors;

import java.util.ArrayList;
import java.util.List;

import com.glue.bot.Crawler;
import com.glue.bot.EventMapper;
import com.glue.bot.HtmlMapper;
import com.glue.bot.domain.EventSelectors;
import com.glue.bot.domain.SiteMap;
import com.glue.bot.domain.VenueSelectors;
import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Tag;
import com.glue.domain.Venue;

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
public class ScrapingTest {

    public static void testBikini() throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.lebikini.com/programmation/index/date/new").li(
		"a[href^=/programmation/concert/]").build();

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

	HtmlMapper<Event> mappingStrategy = new EventMapper(eventSelectors,
		eventRef);

	Crawler<Event> parser = new Crawler<>(siteMap, mappingStrategy);

	parser.run();

	System.out.println("We're done here.");
    }

    public static void testLAutreCanal() throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap("http://www.lautrecanalnancy.fr/-Agenda-")
		.li("#liste_agenda > tbody > tr > td > div > a").build();

	// 2nd step: describe the structure of an event details page
	// Note: The selectors are the same as the ones defined in the
	// BikiniEvent class
	EventSelectors eventSelectors = newEventSelectors()
		.rootBlock("#evenement").title("h1.titre")
		.description("#texte_art")
		.eventType("#evenement > div.mots_event")
		.thumbnail("#illustration").price("#tarifs")
		.dates("#bloc_infos > div.date_ev")
		// .withDatePattern("E dd MMM yyyy 'à' HH:mm")
		// Ex: vendredi 21 février 2014 à 20:30
		// .withLocale(Locale.FRENCH)
		.build();

	// Template
	Venue venueRef = new Venue();
	venueRef.setName("L'Autre Canal");
	venueRef.setAddress("45 Boulevard d'Austrasie");
	venueRef.setCity("Nancy");
	venueRef.setCountry("France");
	venueRef.setPostalCode("54000");

	Event eventRef = new Event();
	eventRef.setCategory(EventCategory.MUSIC);
	eventRef.setVenue(venueRef); // Important !

	HtmlMapper<Event> mappingStrategy = new EventMapper(eventSelectors,
		eventRef);

	Crawler<Event> parser = new Crawler<>(siteMap, mappingStrategy);

	parser.run();

	System.out.println("We're done here.");
    }

    public static void testMandala() throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.mandalabouge.com/wp/category/tout-le-programme/")
		.li("#listing > div > div.listContent > h2 > a")
		.next("#nextpage").build();

	// 2nd step: describe the structure of an event details page
	// Note: The selectors are the same as the ones defined in the
	// BikiniEvent class
	EventSelectors eventSelectors = newEventSelectors()
		.title("#postTitle:first-child")
		.description("#content div.entry > p")
		.thumbnail("#content div.entry")
		.dates("#metaStuff > li > div.smallMeta").build();

	// Template
	List<Tag> tags = new ArrayList<Tag>();
	tags.add(new Tag("Jazz"));
	tags.add(new Tag("Musique du monde"));
	tags.add(new Tag("Jam-sessions"));

	Venue venueRef = new Venue();
	venueRef.setName("Le Mandala");
	venueRef.setAddress("23 Rue des Amidonniers");
	venueRef.setCity("Toulouse");
	venueRef.setCountry("France");
	venueRef.setPostalCode("31000");
	venueRef.setTags(tags);

	Event eventRef = new Event();
	eventRef.setCategory(EventCategory.MUSIC);
	eventRef.setVenue(venueRef); // Important !

	HtmlMapper<Event> mappingStrategy = new EventMapper(eventSelectors,
		eventRef);

	Crawler<Event> parser = new Crawler<>(siteMap, mappingStrategy);

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
		.thumbnail("#show > div > a > img").build();

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

	HtmlMapper<Event> mappingStrategy = new EventMapper(eventSelectors,
		eventRef);

	Crawler<Event> parser = new Crawler<>(siteMap, mappingStrategy);

	parser.run();

	System.out.println("We're done here.");

    }

    public static void main(String[] args) throws Exception {
	testLAutreCanal();
    }

}
