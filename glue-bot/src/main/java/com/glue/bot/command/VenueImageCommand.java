package com.glue.bot.command;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.HtmlTags;
import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Venue;

public class VenueImageCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(VenueImageCommand.class);

    private String imageSelectorKey = SelectorKeys.IMAGE_SELECTOR_KEY;

    public String getImageSelectorKey() {
	return imageSelectorKey;
    }

    public void setImageSelectorKey(String imageSelectorKey) {
	this.imageSelectorKey = imageSelectorKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Venue venue = (Venue) context.get(getVenueKey());
	String imageSelector = (String) context.get(getImageSelectorKey());

	if (imageSelector != null) {
	    Elements elems = elem.select(imageSelector);
	    // Get media
	    elems = elems.select(HtmlTags.IMAGE);

	    for (Element imgElement : elems) {

		String imageUrl = imgElement.attr("abs:src");

		ImageItem item = new ImageItem();
		item.setUrl(imageUrl);

		Image image = new Image();
		image.setOriginal(item);
		image.setUrl(imageUrl);
		image.setSource(venue.getUrl());
		image.setSticky(true);

		venue.getImages().add(image);
	    }
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
