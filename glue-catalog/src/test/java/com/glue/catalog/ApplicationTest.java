package com.glue.catalog;

import static com.glue.bot.domain.EventSelectorsBuilder.newEventSelectors;
import static com.glue.bot.domain.SiteMapBuilder.newSiteMap;
import static com.glue.bot.domain.VenueSelectorsBuilder.newVenueSelectors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.glue.bot.domain.EventSelectors;
import com.glue.bot.domain.SiteMap;
import com.glue.bot.domain.VenueSelectors;
import com.glue.catalog.domain.CatalogRepository;
import com.glue.catalog.domain.EventWebsite;
import com.glue.catalog.security.Manager;
import com.glue.catalog.security.ManagerRepository;
import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SpringBootApplication
public class ApplicationTest implements CommandLineRunner {

    @Autowired
    private CatalogRepository repository;

    @Autowired
    private ManagerRepository managers;

    /**
     * For JSON pretty printing.
     */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Manager manager;

    public static void main(String[] args) {
	SpringApplication.run(ApplicationTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

	final String username = "pasgille";
	manager = this.managers.save(new Manager(username, "secr3t",
		"ROLE_MANAGER"));

	SecurityContextHolder.getContext().setAuthentication(
		new UsernamePasswordAuthenticationToken(username,
			"doesn't matter", AuthorityUtils
				.createAuthorityList("ROLE_MANAGER")));

	repository.deleteAll();

	// save a couple of customers
	repository.save(getBikini());
	repository.save(getLAutreCanal());
	repository.save(getMandala());

	// fetch all customers
	System.out.println("Event websites found with findAll():");
	System.out.println("-------------------------------");
	for (EventWebsite eventWebsite : repository.findAll()) {
	    System.out.println(toJson(eventWebsite));
	}
	System.out.println();

	// fetch an individual event website
	System.out
		.println("Event website found with findOne('http://www.lebikini.com/'):");
	System.out.println("--------------------------------");
	System.out.println(toJson(repository
		.findOne("http://www.lebikini.com/")));

	SecurityContextHolder.clearContext();
    }

    private String toJson(EventWebsite eventWebsite) {
	String jsonObj = gson.toJson(eventWebsite);
	return jsonObj;
    }

    private EventWebsite getBikini() {
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

	EventWebsite retval = new EventWebsite("http://www.lebikini.com/");
	retval.setSiteMap(siteMap);
	retval.setEventSelectors(eventSelectors);
	retval.setEventTemplate(eventRef);

	retval.setManager(manager);

	return retval;
    }

    private EventWebsite getLAutreCanal() {
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
		.dates("#bloc_infos > div.date_ev").build();

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

	EventWebsite retval = new EventWebsite(
		"http://www.lautrecanalnancy.fr/");
	retval.setSiteMap(siteMap);
	retval.setEventSelectors(eventSelectors);
	retval.setEventTemplate(eventRef);

	retval.setManager(manager);

	return retval;
    }

    private EventWebsite getMandala() {
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

	EventWebsite retval = new EventWebsite("http://www.mandalabouge.com/");
	retval.setSiteMap(siteMap);
	retval.setEventSelectors(eventSelectors);
	retval.setEventTemplate(eventRef);

	retval.setManager(manager);

	return retval;
    }
}
