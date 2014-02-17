package com.glue.feed.html.demo;

import com.glue.feed.html.PaginatedListStrategy;
import com.glue.feed.html.SiteMap;
import com.glue.feed.html.SiteMapBuilder;
import com.glue.feed.html.VisitorStrategy;

public class VisitorDemo {

    public static void main(String[] args) throws Exception {

	// See the query syntax documentation in
	// http://jsoup.org/apidocs/org/jsoup/select/Selector.html

	// Clutch Mag
	SiteMap siteMap = new SiteMapBuilder(
		"http://www.clutchmag.fr/agenda?date=ce_mois&type_evenement=tous")
		.list("table.tab-evenements tr h3")
		.next("div.pagination li.next")
		// End of data condition
		// li element with class "next" and also "disabled"
		// div.pagination li.next[class*=disabled]
		// or direct a with href # after li element with class next
		.last("div.pagination li.next > a[href=#]").build();

	// Le Bikini
	siteMap = new SiteMapBuilder(
		"http://www.lebikini.com/programmation/index/date/new").list(
		"tr.lignespectacle h2").build();

	// Le Connexion
	siteMap = new SiteMapBuilder(
		"http://www.connexion-cafe.com/programmation/").list(
		"div.entry a").build();

	// L'autre Canal Nancy
	siteMap = new SiteMapBuilder("http://www.lautrecanalnancy.fr/-Agenda-")
		.list("table#liste_agenda td").build();

	// Théâtre du Capitole
	siteMap = new SiteMapBuilder(
		"http://www.theatreducapitole.fr/1/saison-2013-2014/calendrier-392/")
		.list("table.calendar td.date > p:not([class])").build();

	// Théâtre du Grand Rond
	siteMap = new SiteMapBuilder(
		"http://www.grand-rond.org/index.php/grandrond/programmation?cat_id=1")
		.list("td a.savoir_plus:containsOwn(En savoir plus)").build();

	// Le Bijou
	siteMap = new SiteMapBuilder(
		"http://www.le-bijou.net/programmation.html").list(
		"div.evenement > a:not(:containsOwn(RÉSERVER))").build();

	// Orchestre National du Capitole de Toulouse
	siteMap = new SiteMapBuilder("http://onct.toulouse.fr/agenda").list(
		"div.event").build();

	// Le Mandala
	siteMap = new SiteMapBuilder(
		"http://www.lemandala.com/wp/category/tout-le-programme/")
		.list("div#listing a.dateInfo").next("div#nextpage")
		// Empty div with class "nextpage"
		.last("div#nextpage:not(:has(a))").build();

	// Cultures Toulouse
	siteMap = new SiteMapBuilder(
		"http://culture.toulouse.fr/web/guest/agenda")
		.list("div.info-event a.link-event")
		// finds sibling <a> element immediately preceded by sibling
		// <strong> element designing the current page
		.next("strong.journal-article-page-number + a.journal-article-page-number")
		.last("div#nextpage:not(:has(a))").build();

	// Peniche Didascalie
	// Tout public
	siteMap = new SiteMapBuilder(
		"http://www.penichedidascalie.com/Tout-Public").list(
		"div.nav_spectacles > div.date").build();

	VisitorStrategy strategy = new PaginatedListStrategy(siteMap);
	strategy.visit();

	System.out.println("All right");

    }
}
