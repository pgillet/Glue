package com.glue.feed.toulouse.bikini;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.xml.StreamBuilder;
import com.glue.struct.Category;
import com.glue.struct.IStream;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.Venue;

public class ItemStreamBuilder implements StreamBuilder<Item> {

	static final Logger LOG = LoggerFactory.getLogger(ItemStreamBuilder.class);

	private static final String DATE_PATTERN = "'le' E dd MMM yyyy";
	private DateFormat format;
	private File root;

	public ItemStreamBuilder() {
		format = new SimpleDateFormat(DATE_PATTERN, Locale.FRENCH);
		TimeZone tz = TimeZone.getTimeZone("UTC");
		format.setTimeZone(tz);
		root = new File(System.getProperty("java.io.tmpdir"));
	}

	@Override
	public IStream buildStream(Item msg) throws Exception {
		// Title
		int index = msg.title.lastIndexOf(":");
		String title = msg.title.substring(0, index).trim();

		// Begin date
		String strdate = msg.title.substring(index + 1).trim();
		Date date = format.parse(strdate);

		// Link
		String url = msg.link;

		// Description
		index = msg.description.lastIndexOf("<br>");
		String description = msg.description.substring(0, index);

		// Venue address
		index = msg.description.lastIndexOf(":");
		String address = msg.description.substring(index + 1).trim();

		// Venue name
		index = address.lastIndexOf("(");
		String name = address.substring(0, index).trim();

		// Get stream image
		// TODO: waiting for glue-content
		Document doc = Jsoup.connect(url).get();
		Elements images = doc.select("#blocImage a img");
		String imgUrl = images.attr("src");

		URL imageUrl = new URL(imgUrl);
		File imageFile = new File(imageUrl.getPath());
		imageFile = new File(root, imageFile.getName());

		System.out.println("Copying " + imageUrl + " to " + imageFile);
		InputStream in = new BufferedInputStream(imageUrl.openStream());
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				imageFile));
		copy(in, out);
		out.close();
		in.close();

		IStream stream = new Stream();
		stream.setTitle(title);
		stream.setDescription(description);
		stream.setUrl(url);
		stream.setStartDate(date.getTime());
		stream.setEndDate(date.getTime());
		stream.setThumbPath(imageFile.getPath());
		stream.setCategory(Category.MUSIC);

		IVenue venue = new Venue();
		venue.setName(name);
		venue.setAddress(address);
		stream.setVenue(venue);

		return stream;
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}

}
