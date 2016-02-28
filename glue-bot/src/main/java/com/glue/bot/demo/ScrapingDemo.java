package com.glue.bot.demo;

import static com.glue.bot.SiteMapBuilder.newSiteMap;

import com.glue.bot.Crawler;
import com.glue.bot.SiteMap;

/**
 * Demonstration of the AnnotationMappingStrategy.
 * 
 * @see ScrapingTest
 * 
 * @author pgillet
 * 
 */
public class ScrapingDemo {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.penichedidascalie.com/Tout-Public").li(
		"div.nav_spectacles > div.date").build();

	// 2nd step: define your java bean. The HTML page for an event will be
	// mapped to this bean.
	// See DidascalieEvent class
	Crawler<DidascalieEvent> parser = new Crawler<>(
		DidascalieEvent.class, siteMap);

	// 3rd step: Define how to transform a DidascalieEvent object to a Event object
	// Here, we use the DefaultFeedMessageListener defined in the parser
	// that simply prints the bean on
	// the console

	// final FeedMessageListener delegate = new StreamMessageListener();
	// final GlueObjectBuilder<DidascalieEvent, Event> streamBuilder =
	// ...;
	//
	// parser.setFeedMessageListener(new
	// FeedMessageListener<DidascalieEvent>() {
	//
	// @Override
	// public void newMessage(DidascalieEvent msg) throws Exception {
	// Event stream = streamBuilder.build(msg);
	// delegate.newMessage(stream);
	// }
	//
	// @Override
	// public void close() throws IOException {
	// delegate.close();
	// }
	// });

	parser.run();

	System.out.println("We're done here.");
    }

}
