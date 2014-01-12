package com.glue.feed.youtube;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.DataSourceManager;
import com.glue.struct.IMedia;
import com.glue.struct.IStream;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.MediaDAO;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;

public class YoutubeMediaFeeder implements Job {

	static final Logger LOG = LoggerFactory.getLogger(YoutubeMediaFeeder.class);

	private DAOManager manager;
	private Calendar calendar;

	public YoutubeMediaFeeder() {
		DataSource ds = DataSourceManager.getInstance().getDataSource();
		manager = DAOManager.getInstance(ds);

		TimeZone tz = TimeZone.getTimeZone("UTC");
		calendar = Calendar.getInstance(tz);
		// reset hour, minutes, seconds and millis
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		long before = calendar.getTimeInMillis();
		calendar.add(Calendar.DATE, -7);
		long after = calendar.getTimeInMillis();

		StreamDAO streamDAO = null;
		MediaDAO mediaDAO = null;
		VenueDAO venueDAO = null;
		List<IStream> streams = new ArrayList<>();
		try {
			streamDAO = manager.getStreamDAO();
			mediaDAO = manager.getMediaDAO();
			venueDAO = manager.getVenueDAO();

			// Search for streams from "after" to "today"
			streams = streamDAO.searchBetween(after, before);

			// For each streams from "after" to "today"
			for (IStream stream : streams) {

				// Set venue
				stream.setVenue(venueDAO.search(stream.getVenue().getId()));

				LOG.info("Stream " + stream.getTitle());

				// Search for videos for that stream
				List<IMedia> videos = YoutubeRequester.getInstance().search(
						stream);

				// Persist videos
				for (IMedia video : videos) {

					// Url already in databases for that stream
					if (!mediaDAO.exist(video)) {
						mediaDAO.create(video);
						LOG.info("Creation of media " + video.getUrl());
					} else {
						LOG.info("Media " + video.getUrl() + " already stored");
					}
				}
			}

		} catch (SQLException e) {
			LOG.error("Sql Exception: " + e.getMessage());
			throw new JobExecutionException(e);
		} catch (IOException e) {
			LOG.error("There was an IO error: " + e.getMessage(), e);
			throw new JobExecutionException(e);
		}
	}
	
	public static void main(String[] args) throws JobExecutionException {
		YoutubeMediaFeeder job = new YoutubeMediaFeeder();
		job.execute(null);
	}
}
