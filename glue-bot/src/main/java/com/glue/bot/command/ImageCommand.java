package com.glue.bot.command;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.HtmlTags;
import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;

public class ImageCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(ImageCommand.class);

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
	Event event = (Event) context.get(getEventKey());
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
		image.setSource(event.getUrl());

		event.getImages().add(image);
	    }

	    // Set the first image as sticky
	    List<Image> images = event.getImages();
	    if (!images.isEmpty()) {
		Image image = images.get(0);
		image.setSticky(true);
	    }

	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
