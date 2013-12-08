package com.glue.feed.toulouse.open.data.venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.FeedMessageListener;
import com.glue.feed.GlueObjectBuilder;
import com.glue.feed.csv.CSVFeedParser;
import com.glue.feed.listener.VenueMessageListener;
import com.glue.struct.IVenue;

/**
 * Read from a CSV file the list of cultural equipement in Toulouse and
 * surroundings.
 * 
 * @author grdenis
 */
public class ToulouseEquipementsMain {

	static final Logger LOG = LoggerFactory.getLogger(ToulouseEquipementsMain.class);

	public static void main(String[] args) throws Exception {

		URL url = new URL(
				"http://data.grandtoulouse.fr/web/guest/les-donnees/-/opendata/card/23851-equipements-culturels/resource/document?p_p_state=exclusive&_5_WAR_opendataportlet_jspPage=%2Fsearch%2Fview_card_license.jsp");

		ZipInputStream zin = new ZipInputStream(url.openStream());
		ZipEntry ze = zin.getNextEntry();
		while (!ze.getName().endsWith(".csv")) {
			zin.closeEntry();
			ze = zin.getNextEntry();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(zin, Charset.forName("UTF-8")));

		CSVFeedParser<VenueBean> parser = new CSVFeedParser<>(reader, VenueBean.class);

		final FeedMessageListener<IVenue> delegate = new VenueMessageListener();
		final GlueObjectBuilder<VenueBean, IVenue> venueBuilder = new VenueBeanVenueBuilder();

		parser.setFeedMessageListener(new FeedMessageListener<VenueBean>() {

			@Override
			public void newMessage(VenueBean msg) throws Exception {
				IVenue venue = venueBuilder.build(msg);
				delegate.newMessage(venue);
			}

			@Override
			public void close() throws IOException {
				delegate.close();
			}
		});

		parser.read();
		parser.close();
		LOG.info("Done");
	}
}
