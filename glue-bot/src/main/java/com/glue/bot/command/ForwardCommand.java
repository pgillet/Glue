package com.glue.bot.command;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.HtmlFetcher;
import com.glue.chain.Command;
import com.glue.chain.Context;

public class ForwardCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(ForwardCommand.class);

    private HtmlFetcher hf = new HtmlFetcher();

    private String locationKey = "location";

    public String getLocationKey() {
	return locationKey;
    }

    public void setLocationKey(String locationKey) {
	this.locationKey = locationKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());

	if ("a".equals(elem.tagName())) {
	    String location = elem.attr("abs:href");
	    elem = hf.fetch(location);

	    context.put(getLocationKey(), location);
	    context.put(getElementKey(), elem);
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
