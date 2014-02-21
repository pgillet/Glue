package com.glue.feed.html.demo;

import static com.glue.feed.html.SiteMapBuilder.newSiteMap;

import com.glue.feed.html.HTMLFeedParser;
import com.glue.feed.html.SiteMap;

/**
 * Demonstration of the AnnotationMappingStrategy.
 * 
 * @see ScrapingDemo2
 * 
 * @author pgillet
 * 
 */
public class ScrapingDemo {

    public static void main(String[] args) throws Exception {

	// 1st step: describe the structure of your web site
	SiteMap siteMap = newSiteMap(
		"http://www.penichedidascalie.com/Tout-Public").list(
		"div.nav_spectacles > div.date").build();

	// 2nd step: define your java bean. The HTML page for an event will be
	// mapped to this bean.
	// See DidascalieEvent class
	HTMLFeedParser<DidascalieEvent> parser = new HTMLFeedParser<>(
		DidascalieEvent.class, siteMap);

	// 3rd step: Define how to transform a DidascalieEvent object to a IStream object
	// Here, we use the DefaultFeedMessageListener defined in the parser
	// that simply prints the bean on
	// the console

	// final FeedMessageListener delegate = new StreamMessageListener();
	// final GlueObjectBuilder<DidascalieEvent, IStream> streamBuilder =
	// ...;
	//
	// parser.setFeedMessageListener(new
	// FeedMessageListener<DidascalieEvent>() {
	//
	// @Override
	// public void newMessage(DidascalieEvent msg) throws Exception {
	// IStream stream = streamBuilder.build(msg);
	// delegate.newMessage(stream);
	// }
	//
	// @Override
	// public void close() throws IOException {
	// delegate.close();
	// }
	// });

	parser.read();
	parser.close();

	System.out.println("We're done here.");
    }

}
