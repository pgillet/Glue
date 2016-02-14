package com.glue.bot.command;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Event;

public class EventUrlCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(EventUrlCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {
	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Event event = (Event) context.get(getEventKey());

	event.setUrl(elem.baseUri());

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
