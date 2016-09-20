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
		// super.write(b, off, len);
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}

		this.template.convertAndSend("/topic/greetings", new String(b));
	}

	@Override
	public void write(int b) throws IOException {
		// Does nothing
	}

}
