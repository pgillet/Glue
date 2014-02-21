package com.glue.feed.html.demo;

import static com.glue.feed.html.SiteMapBuilder.newSiteMap;

import com.glue.feed.html.PaginatedListStrategy;
import com.glue.feed.html.SiteMap;
import com.glue.feed.html.VisitorStrategy;

public class VisitorDemo {

    public static void main(String[] args) throws Exception {

	// See the query syntax documentation in
	// http://jsoup.org/apidocs/org/jsoup/select/Selector.html

	// Clutch Mag
	SiteMap siteMap = newSiteMap(
		"http://www.clutchmag.fr/agenda?date=ce_mois&type_evenement=tous")
		// .list("table.tab-evenements tr h3")
		.url("http://www.clutchmag.fr/evenement/")
		.next("div.pagination li.next")
		// End of data condition
		// li element with class "next" and also "disabled"
		// div.pagination li.next[class*=disabled]
		// or direct a with href # after li element with class next
		.last("div.pagination li.next > a[href=#]").build();

	// Le Bikini
	siteMap = newSiteMap(
		"http://www.lebikini.com/programmation/index/date/new")
	// .list("tr.lignespectacle h2")
		.url("http://www.lebikini.com/programmation/concert/").build();

	// Le Connexion
	siteMap = newSiteMap(
		"http://www.connexion-cafe.com/programmation/").list(
		"div.entry a").build();

	// L'autre Canal Nancy
	siteMap = newSiteMap("http://www.lautrecanalnancy.fr/-Agenda-")
		.list("table#liste_agenda td").build();

	// Théâtre du Capitole
	siteMap = newSiteMap(
		"http://www.theatreducapitole.fr/1/saison-2013-2014/calendrier-392/")
		.list("table.calendar td.date > p:not([class])").build();

	// Théâtre du Grand Rond
	siteMap = newSiteMap(
		"http://www.grand-rond.org/index.php/grandrond/programmation")
		// .list("td a.savoir_plus:containsOwn(En savoir plus)")
		.url("http://www.grand-rond.org/index.php/grandrond/spectacle?")
		.build();

	// Le Bijou
	siteMap = newSiteMap(
		"http://www.le-bijou.net/programmation.html")
	// .list("div.evenement > a:not(:containsOwn(RÉSERVER))")
		.url("http://www.le-bijou.net/spectacle/").build();

	// Orchestre National du Capitole de Toulouse
	siteMap = newSiteMap("http://onct.toulouse.fr/agenda")
	// .list("div.event")
		.url("http://onct.toulouse.fr/spectacles/-/spectacle/").build();

	// Le Mandala
	siteMap = newSiteMap(
		"http://www.lemandala.com/wp/category/tout-le-programme/")
		.list("div#listing a.dateInfo").next("div#nextpage")
		// Empty div with class "nextpage"
		.last("div#nextpage:not(:has(a))").build();

	// Cultures Toulouse
	siteMap = newSiteMap(
		"http://culture.toulouse.fr/web/guest/agenda")
		// .list("div.info-event a.link-event")
		.url("http://culture.toulouse.fr/web/guest/agenda/-/agenda/event/")
		// finds sibling <a> element immediately preceded by sibling
		// <strong> element designing the current page
		.next("strong.journal-article-page-number + a.journal-article-page-number")
		.last("div#nextpage:not(:has(a))").build();

	// Peniche Didascalie
	// Tout public
	siteMap = newSiteMap(
		"http://www.penichedidascalie.com/Tout-Public").list(
		"div.nav_spectacles > div.date").build();

	VisitorStrategy strategy = new PaginatedListStrategy(siteMap);
	strategy.visit();

	System.out.println("All right");

    }
}
