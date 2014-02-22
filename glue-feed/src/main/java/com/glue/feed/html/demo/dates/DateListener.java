package com.glue.feed.html.demo.dates;

import java.io.BufferedWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.FeedMessageListener;

public class DateListener implements FeedMessageListener<String> {

    static final Logger LOG = LoggerFactory.getLogger(DateListener.class);

    public static final String REGEX = "(\\p{Punct}+)";
    public static final String REPLACEMENT = " $1";

    private BufferedWriter bw;

    public DateListener(BufferedWriter bw) {
	this.bw = bw;
    }

    @Override
    public void newMessage(String msg) throws Exception {
	LOG.info("String extracted = " + msg);
	if (msg != null) {
	    String str = msg.replaceAll(REGEX, REPLACEMENT);

	    // Append file
	    bw.write(str);
	    bw.newLine();
	}
    }

    @Override
    public void close() throws IOException {
	// bw.close();
    }

}