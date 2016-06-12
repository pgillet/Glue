package com.glue.catalog.ws;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(ValidationRequest message) throws Exception {
	return new Greeting("OK, well received "
		+ message.getEventWebsite().getUri());
    }

}
