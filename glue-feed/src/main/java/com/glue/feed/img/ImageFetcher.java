package com.glue.feed.img;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.content.ContentManager;
import com.glue.content.EventCAO;
import com.glue.content.ImageRendition;
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
	String url = item.getUrl();
	
	eventCAO.addImage(url, event);
	
	// Create additional thumbnail image
	// First, we retrieve the original image we just stored as an input
	// stream
	String basename = FilenameUtils.getBaseName(url);
	String extension = FilenameUtils.getExtension(url);
	InputStream in = eventCAO.getImage(basename, event);
	
	final int width = 170;
	// We create the thumbnail in memory
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	Thumbnails.of(in).size(width, width).toOutputStream(out);
	in.close();

	LOG.info("Created thumbnail image of " + out.size()
		+ "bytes from url = "
		+ url);

	in = new ByteArrayInputStream(out.toByteArray());

	// Finally, we store the thumbnail image along the original image
	String filename = basename + "."
		+ ImageRendition.THUMBNAIL.name().toLowerCase() + "."
		+ extension;
	eventCAO.add(filename, "application/octet-stream", in, event);

	in.close();
    }
}
