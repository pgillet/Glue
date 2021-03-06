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

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());

	LOG.info("Referring page = " + elem.baseUri());

	if ("a".equals(elem.tagName())) {
	    String location = elem.attr("abs:href");

	    LOG.info("Following link = " + location);
	    elem = hf.fetch(location);

	    context.put(getElementKey(), elem);
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
