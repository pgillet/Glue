package com.glue.bot.command;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.HtmlUtils;
import com.glue.bot.SelectorKeys;
import com.glue.bot.Validate;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Event;

public class DescriptionCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(DescriptionCommand.class);

    private String descriptionSelectorKey = SelectorKeys.DESCRIPTION_SELECTOR_KEY;

    public String getDescriptionSelectorKey() {
	return descriptionSelectorKey;
    }

    public void setDescriptionSelectorKey(String descriptionSelectorKey) {
	this.descriptionSelectorKey = descriptionSelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	Element elem = (Element) context.get(getElementKey());
	Event event = (Event) context.get(getEventKey());

	String descriptionSelector = (String) context
		.get(getDescriptionSelectorKey());

	if (descriptionSelector != null) {
	    Elements elems = elem.select(descriptionSelector);
	    Validate.notEmpty(elems);

	    String description = elems.html();
	    description = HtmlUtils.cleanHtml(description);

	    event.setDescription(StringUtils.defaultIfBlank(description,
		    event.getDescription()));
	}

	return Command.CONTINUE_PROCESSING;
    }

}
