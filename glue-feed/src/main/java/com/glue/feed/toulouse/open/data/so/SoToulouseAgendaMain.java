package com.glue.feed.toulouse.open.data.so;

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
import com.glue.feed.listener.StreamMessageListener;
import com.glue.struct.IStream;

/**
 * Read from a CSV file the list of cultural events in Toulouse and
 * surroundings.
 * 
 * @author grdenis
 */
public class SoToulouseAgendaMain {

	static final Logger LOG = LoggerFactory
			.getLogger(SoToulouseAgendaMain.class);

	public static void main(String[] args) throws Exception {

		URL url = new URL(
				"http://data.grandtoulouse.fr/web/guest/les-donnees/-/opendata/card/21905-agenda-des-manifestations-culturelles/resource/document?p_p_state=exclusive&_5_WAR_opendataportlet_jspPage=%2Fsearch%2Fview_card_license.jsp");

		ZipInputStream zin = new ZipInputStream(url.openStream());
		ZipEntry ze = zin.getNextEntry();
		while (!ze.getName().endsWith(".csv")) {
			zin.closeEntry();
			ze = zin.getNextEntry();
		}

		// CSV files encoding = "Windows-1252"
		BufferedReader reader = new BufferedReader(new InputStreamReader(zin,
				Charset.forName("Windows-1252")));

		CSVFeedParser<EventBean> parser = new CSVFeedParser<>(reader,
				EventBean.class);

		final FeedMessageListener<IStream> delegate = new StreamMessageListener();
		final GlueObjectBuilder<EventBean, IStream> streamBuilder = new EventBeanStreamBuilder();

		parser.setFeedMessageListener(new FeedMessageListener<EventBean>() {

			@Override
			public void newMessage(EventBean msg) throws Exception {
				IStream stream = streamBuilder.build(msg);
				delegate.newMessage(stream);
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
