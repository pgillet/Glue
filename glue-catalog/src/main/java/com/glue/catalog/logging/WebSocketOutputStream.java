package com.glue.catalog.logging;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketOutputStream extends OutputStream {

	private SimpMessagingTemplate template;

	@Autowired
	public WebSocketOutputStream(SimpMessagingTemplate template) {
		this.template = template;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {

		String msg = new String(b, off, len);
		this.template.convertAndSend("/topic/greetings", msg);
	}

	@Override
	public void write(int b) throws IOException {
		// Does nothing
	}

}
