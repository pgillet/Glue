package com.glue.feed.html.demo.dates;

import static com.glue.feed.html.EventDetailsPageBuilder.newDetails;
import static com.glue.feed.html.SiteMapBuilder.newSiteMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.FeedMessageListener;
import com.glue.feed.html.EventDetailsPage;
import com.glue.feed.html.EventWebsite;
import com.glue.feed.html.HTMLFeedParser;
import com.glue.feed.html.HTMLMappingStrategy;
import com.glue.feed.html.SiteMap;

public class ExtractDates {

    static final Logger LOG = LoggerFactory.getLogger(ExtractDates.class);

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {

	ExtractDates ed = new ExtractDates();
	List<EventWebsite> websites = ed.getWebsites();

	final String path = "/home/pgillet/Downloads/Glue/dates.txt";

	// Append mode
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path),
		true));

	for (EventWebsite ws : websites) {

	    HTMLMappingStrategy<String> mappingStrategy = new DateExtractor(
		    ws.getDetails());
	    FeedMessageListener<String> listener = new DateListener(bw);

	    HTMLFeedParser<String> parser = new HTMLFeedParser<>(
		    ws.getSiteMap(), mappingStrategy);
	    parser.setFeedMessageListener(listener);
	    try {
		parser.read();
	    } catch (Exception e) {
		LOG.warn(e.getMessage(), e);
	    }
	}

	bw.close();

	System.out.println("We're done here.");
    }

    public List<EventWebsite> getWebsites() {
	List<EventWebsite> websites = new ArrayList<>();

	// Le Bikini
	SiteMap siteMap = newSiteMap(
		"http://www.lebikini.com/programmation/index/date/new").url(
		"http://www.lebikini.com/programmation/concert/").build();
	// Date mapping
	EventDetailsPage details = newDetails()
		.withRootBlock("div#encartDetailSpectacle")
		.withStartDate("div#blocContenu > div#date").build();
	EventWebsite ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Clutch Mag
	// siteMap = newSiteMap(
	// "http://www.clutchmag.fr/agenda?date=ce_mois&type_evenement=tous")
	// // .list("table.tab-evenements tr h3")
	// .url("http://www.clutchmag.fr/evenement/")
	// .next("div.pagination li.next")
	// // End of data condition
	// // li element with class "next" and also "disabled"
	// // div.pagination li.next[class*=disabled]
	// // or direct a with href # after li element with class next
	// .last("div.pagination li.next > a[href=#]").build();
	// // Date mapping
	// details = newDetails().withStartDate("div.page-header > h1 > small")
	// .build();
	// ws = new EventWebsite(siteMap, details);
	// websites.add(ws);

	// Le Connexion
	siteMap = newSiteMap("http://www.connexion-cafe.com/programmation/")
		.list("div.entry a").build();
	// Date mapping
	details = newDetails().withRootBlock("div#contentwrap")
		.withStartDate("div.entry > p:first-child").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// L'autre Canal Nancy
	siteMap = newSiteMap("http://www.lautrecanalnancy.fr/-Agenda-").list(
		"table#liste_agenda td").build();
	// Date mapping
	details = newDetails().withStartDate("div.date_ev > p").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Théâtre du Capitole
	siteMap = newSiteMap(
		"http://www.theatreducapitole.fr/1/saison-2013-2014/calendrier-392/")
		.list("table.calendar td.date > p:not([class])").build();
	// Date mapping
	details = newDetails().withStartDate("p.horaires").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Le Bijou
	siteMap = newSiteMap("http://www.le-bijou.net/programmation.html")
	// .list("div.evenement > a:not(:containsOwn(RÉSERVER))")
		.url("http://www.le-bijou.net/spectacle/").build();
	// Date mapping
	details = newDetails().withStartDate("p.rdv").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Orchestre National du Capitole de Toulouse
	// siteMap = newSiteMap("http://onct.toulouse.fr/agenda")
	// // .list("div.event")
	// .url("http://onct.toulouse.fr/spectacles/-/spectacle/").build();
	// // Date mapping
	// details = newDetails().withStartDate("span.date > strong").build();
	// ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Le Mandala
	siteMap = newSiteMap(
		"http://www.lemandala.com/wp/category/tout-le-programme/")
		.list("div#listing a.dateInfo").next("div#nextpage")
		// Empty div with class "nextpage"
		.last("div#nextpage:not(:has(a))").build();
	// Date mapping
	details = newDetails().withStartDate("div.smallMeta").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Cultures Toulouse
	siteMap = newSiteMap("http://culture.toulouse.fr/web/guest/agenda")
		// .list("div.info-event a.link-event")
		.url("http://culture.toulouse.fr/web/guest/agenda/-/agenda/event/")
		// finds sibling <a> element immediately preceded by sibling
		// <strong> element designing the current page
		.next("strong.journal-article-page-number + a.journal-article-page-number")
		.last("div#nextpage:not(:has(a))").build();
	// Date mapping
	details = newDetails()
		.withStartDate("h1.event-title + div.block-child").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	// Peniche Didascalie
	// Tout public
	siteMap = newSiteMap("http://www.penichedidascalie.com/Tout-Public")
		.list("div.nav_spectacles > div.date").build();
	details = newDetails().withStartDate("p.dates").build();
	ws = new EventWebsite(siteMap, details);
	websites.add(ws);

	return websites;
    }

}
