package com.glue.feed.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import com.glue.struct.IMedia;
import com.glue.struct.IStream;
import com.glue.struct.impl.Media;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YoutubeRequester {

	/** Global instance properties filename. */
	private static String PROPERTIES_FILENAME = "youtube.properties";

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**
	 * Global instance of the max number of videos we want returned (50 = upper
	 * limit per page).
	 */
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	/** Global instance of Youtube object to make all API requests. */
	private static YouTube youtube;

	/** Properties */
	private Properties properties;

	private DateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

	// Current research
	private YouTube.Search.List search;

	// Videos details
	private YouTube.Videos.List searchForVideos;

	private YoutubeRequester() {
		initialize();
	}

	/** Singleton Holder */
	private static class YoutubeRequesterHolder {
		/** Singleton */
		private final static YoutubeRequester instance = new YoutubeRequester();
	}

	/** Get Singleton */
	public static YoutubeRequester getInstance() {
		return YoutubeRequesterHolder.instance;
	}

	private void initialize() {

		// youtube.properties
		properties = new Properties();
		try {
			InputStream in = YoutubeRequester.class.getResourceAsStream("./" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : "
					+ e.getMessage());
			System.exit(1);
		}

		try {
			/*
			 * The YouTube object is used to make all API requests. The last
			 * argument is required, but because we don't need anything
			 * initialized when the HttpRequest is initialized, we override the
			 * interface and provide a no-op function.
			 */
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("Glue").build();

		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}

		calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	}

	public List<IMedia> search(IStream stream) throws IOException {

		// Resulted medias
		List<IMedia> medias = new ArrayList<>();

		// Create a youtube search list
		search = youtube.search().list("id,snippet");
		String apiKey = properties.getProperty("youtube.apikey");
		search.setKey(apiKey);
		search.set("safeSearch", "moderate");
		search.set("videoEmbeddable", "true");
		search.setType("video");
		search.setFields("items(id/videoId,snippet/title)");
		search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

		// Set calendar to enddate
		calendar.setTime(new Date(stream.getEndDate()));

		// Query stream title + stream venue + day + month (january = 0)
		search.setQ(stream.getTitle() + " " + stream.getVenue().getName());

		// Published after stream end_date
		search.set("publishedAfter", formater.format(calendar.getTime()));

		// Published before enddate +30
		calendar.add(Calendar.DATE, +30);
		search.set("publishedBefore", formater.format(calendar.getTime()));

		// Search
		SearchListResponse searchResponse = search.execute();

		// If videos found
		if (!searchResponse.getItems().isEmpty()) {

			// Create a string a videos ids separated by comma
			List<String> videosIds = new ArrayList<>();
			for (SearchResult video : searchResponse.getItems()) {
				videosIds.add(video.getId().getVideoId());
			}
			String ids = "";
			for (String vId : videosIds) {
				ids += vId + ",";
			}

			// Remove last comma
			ids = ids.substring(0, ids.lastIndexOf(","));

			// Search for videos details
			searchForVideos = youtube.videos().list(ids, "snippet");
			searchForVideos.setKey(properties.getProperty("youtube.apikey"));
			search.setFields("items(snippet/title,snippet/description)");
			VideoListResponse videosResponse = searchForVideos.execute();

			// for each videos, check similarity and create media
			for (Video video : videosResponse.getItems()) {
				if (checkSimilarity(stream, video)) {
					System.out.println(video.getSnippet().getTitle());
					medias.add(toMedia(video, stream));
				}
			}
		}
		return medias;
	}

	// Convert youtube Video to Media
	private IMedia toMedia(Video video, IStream stream) {
		IMedia media = new Media();
		media.setStream(stream);
		media.setMimeType("Video");
		media.setUrl("http://www.youtube.com/embed/" + video.getId());
		media.setExternal(true);
		return media;
	}

	// Check similarity between youtube video and stream
	private boolean checkSimilarity(IStream stream, Video video) {

		// Get title and description from video
		String vTitle = video.getSnippet().getTitle().toLowerCase().trim();
		String vDescription = video.getSnippet().getDescription().toLowerCase().trim();

		// Stream title
		String sTitle = stream.getTitle().toLowerCase();

		// Stream description, remove "le" "la"
		String venue = stream.getVenue().getName().toLowerCase();
		if (venue.startsWith("le ")) {
			venue = venue.substring(3);
		} else if (venue.startsWith("la ")) {
			venue = venue.substring(3);
		}

		// Video title must contain stream title
		boolean filterOne = vTitle.matches(".*\\b" + sTitle + "\\b.*");

		// Title or description must contain venue name
		boolean filterTwo = (vTitle.contains(venue)) || (vDescription.contains(venue));

		return filterOne && filterTwo;
	}
}