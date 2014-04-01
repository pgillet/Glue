package com.glue.feed.img;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.content.ContentManager;
import com.glue.content.EventCAO;
import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;

public class ImageFetcher {

    static final Logger LOG = LoggerFactory.getLogger(ImageFetcher.class);

    ContentManager cm = new ContentManager();
    EventCAO eventCAO = cm.getEventCAO();

    public void fetchEventImage(Event event) throws IOException {

	List<Image> images = event.getImages();

	// List should not be empty here
	Image img = images.get(0);
	// Get the original rendition
	ImageItem item = img.getOriginal();
	URL imageUrl = new URL(item.getUrl());
	
	eventCAO.add(imageUrl, event);

    }
}
