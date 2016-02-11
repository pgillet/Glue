package com.glue.bot.command;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Event;

public class TitleCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(TitleCommand.class);

    private String titleSelectorKey = SelectorKeys.TITLE_SELECTOR_KEY;

    public String getTitleSelectorKey() {
	return titleSelectorKey;
    }

    public void setTitleSelectorKey(String titleSelectorKey) {
	this.titleSelectorKey = titleSelectorKey;
    }

    /**
     * <p>
     * Parses the title of the current event.
     * </p>
     *
     * @param context
     *            {@link Context} in which we are operating
     *
     * @return <code>false</code> so that processing will continue
     * @throws Exception
     *             in the if an error occurs during execution.
     */
    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Event event = (Event) context.get(getEventKey());

	String titleSelector = (String) context.get(getTitleSelectorKey());

	event.setTitle(StringUtils.defaultIfEmpty(elem.select(titleSelector)
		.text(), event.getTitle()));

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
