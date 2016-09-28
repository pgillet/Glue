package com.glue.catalog.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.glue.bot.Crawler;
import com.glue.bot.EventMapper;
import com.glue.bot.HtmlMapper;
import com.glue.domain.Event;

@Controller
public class GreetingController {
	
	private static final Logger LOG = LoggerFactory.getLogger(GreetingController.class);

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(ValidationRequest message) throws Exception {

		HtmlMapper<Event> mappingStrategy = new EventMapper(message.getEventWebsite().getEventSelectors(), new Event());

		Crawler<Event> parser = new Crawler<>(message.getEventWebsite().getSiteMap(), mappingStrategy);

		try {
			parser.run();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}

		return new Greeting("OK, well received " + message.getEventWebsite().getUri());
	}

}
