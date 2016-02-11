package com.glue.bot.command;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Event;

public class PriceListCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(PriceListCommand.class);

    private String priceSelectorKey = SelectorKeys.PRICE_SELECTOR_KEY;

    public String getPriceSelectorKey() {
	return priceSelectorKey;
    }

    public void setPriceSelectorKey(String priceSelectorKey) {
	this.priceSelectorKey = priceSelectorKey;
    }


    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	String priceSelector = (String) context.get(getPriceSelectorKey());
	Element elem = (Element) context.get(getElementKey());
	Event event = (Event) context.get(getEventKey());

	// Price
	if (priceSelector != null) {
	    String price = elem.select(priceSelector).text();
	    price = StringUtils.defaultIfBlank(price, event.getPrice());
	    event.setPrice(price);
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
