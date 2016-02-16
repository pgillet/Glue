package com.glue.bot.command;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.chain.Command;
import com.glue.chain.Context;

public class CacheVisitedCommand extends BaseCommand implements Command {

    Set<String> visited = new LinkedHashSet<String>();

    public static final Logger LOG = LoggerFactory
	    .getLogger(CacheVisitedCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());

	String uri = elem.baseUri();
	LOG.info("Processing event webpage = " + uri);

	boolean processingComplete = !visited.add(uri);

	if (processingComplete) {
	    LOG.warn("Event webpage has already been processed. Processing complete.");
	} else {
	    LOG.info("Event webpage has not been visited yet. Continue processing.");
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return processingComplete;
    }

    /**
     * Empty the cache.
     */
    public void clear() {
	visited.clear();
    }

}
