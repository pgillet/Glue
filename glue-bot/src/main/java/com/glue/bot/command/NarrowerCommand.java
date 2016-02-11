package com.glue.bot.command;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.bot.Validate;
import com.glue.chain.Command;
import com.glue.chain.Context;

public class NarrowerCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(NarrowerCommand.class);

    private String rootBlockSelectorKey = SelectorKeys.ROOT_BLOCK_SELECTOR_KEY;

    public String getRootBlockSelectorKey() {
	return rootBlockSelectorKey;
    }

    public void setRootBlockSelectorKey(String rootBlockSelectorKey) {
	this.rootBlockSelectorKey = rootBlockSelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	String rootBlockSelector = (String) context
		.get(getRootBlockSelectorKey());

	if (rootBlockSelector != null) {
	    Elements tmp = elem.select(rootBlockSelector);
	    Validate.single(tmp);
	    elem = tmp.get(0);
	    context.put(getElementKey(), elem);
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
