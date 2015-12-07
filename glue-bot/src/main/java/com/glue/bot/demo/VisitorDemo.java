package com.glue.bot.demo;

import static com.glue.bot.SiteMapBuilder.newSiteMap;

import com.glue.bot.BrowsingStrategy;
import com.glue.bot.PaginatedListStrategy;
import com.glue.bot.SiteMap;

public class VisitorDemo {

    public static void main(String[] args) throws Exception {

	// See the query syntax documentation in
	// http://jsoup.org/apidocs/org/jsoup/select/Selector.html

	// Le Bikini
	SiteMap siteMap = newSiteMap(
		"http://www.lebikini.com/programmation/index/date/new").li(
		"a[href^=/programmation/concert/]")
		.build();

	// Le Connexion
	siteMap = newSiteMap("http://www.connexionlive.fr/programmation/")
		.li("li.entry-content-title > h4 > a").next("div.next_link")
		.build();

	// L'autre Canal Nancy
	// siteMap = newSiteMap("http://www.lautrecanalnancy.fr/-Agenda-").li(
	// "table#liste_agenda td > div > a").build();

	// // Théâtre du Capitole
	// siteMap = newSiteMap(
	// "http://www.theatreducapitole.fr/1/saison-2013-2014/calendrier-392/")
	// .li("table.calendar td.date > p:not([class])").build();
	//
	// // Théâtre du Grand Rond
	// siteMap = newSiteMap(
	// "http://www.grand-rond.org/index.php/grandrond/programmation")
	// // .list("td a.savoir_plus:containsOwn(En savoir plus)")
	// .li("a[href^=http://www.grand-rond.org/index.php/grandrond/spectacle?]")
	// .build();
	//
	// // Le Bijou
	// siteMap = newSiteMap("http://www.le-bijou.net/programmation.html")
	// // .list("div.evenement > a:not(:containsOwn(RÉSERVER))")
	// .li("a[href^=http://www.le-bijou.net/spectacle/]").build();
	//
	// // Orchestre National du Capitole de Toulouse
	// siteMap = newSiteMap("http://onct.toulouse.fr/agenda")
	// // .list("div.event")
	// .li("a[href^=http://onct.toulouse.fr/spectacles/-/spectacle/]")
	// .build();
	//
	// Le Mandala
	// siteMap = newSiteMap(
	// "http://www.mandalabouge.com/wp/category/tout-le-programme/")
	// .li("div#listing a.dateInfo").next("div#nextpage")
	// // Empty div with class "nextpage"
	// .last("div#nextpage:not(:has(a))").build();
	//
	// // Cultures Toulouse
	// siteMap = newSiteMap("http://culture.toulouse.fr/web/guest/agenda")
	// // .list("div.info-event a.link-event")
	// .li("a[href^=http://culture.toulouse.fr/web/guest/agenda/-/agenda/event/]")
	// // finds sibling <a> element immediately preceded by sibling
	// // <strong> element designing the current page
	// .next("strong.journal-article-page-number + a.journal-article-page-number")
	// .last("div#nextpage:not(:has(a))").build();
	//
	// // Peniche Didascalie
	// // Tout public
	// siteMap = newSiteMap(
	// "http://www.penichedidascalie.com/Theatre-jeune-public").li(
	// "div.nav_spectacles > div.date").build(); // div.date:nth-child(1)
	// // > a:nth-child(1)

	// 3T
	siteMap = newSiteMap("http://www.3tcafetheatre.com/programme3T.php")
		.li("td:nth-child(2) > a[href^=spectacle2.php?idspectacle=")
		.build();

	BrowsingStrategy strategy = new PaginatedListStrategy(siteMap);
	strategy.browse();

	System.out.println("All right");

    }
}
