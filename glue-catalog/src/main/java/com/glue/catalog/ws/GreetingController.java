package com.glue.catalog.ws;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.glue.bot.Crawler;
import com.glue.bot.EventMapper;
import com.glue.bot.HtmlMapper;
import com.glue.domain.Event;

@Controller
public class GreetingController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(ValidationRequest message) throws Exception {

		HtmlMapper<Event> mappingStrategy = new EventMapper(message.getEventWebsite().getEventSelectors(), new Event());

		Crawler<Event> parser = new Crawler<>(message.getEventWebsite().getSiteMap(), mappingStrategy);

		parser.run();

		return new Greeting("OK, well received " + message.getEventWebsite().getUri());
	}

}
