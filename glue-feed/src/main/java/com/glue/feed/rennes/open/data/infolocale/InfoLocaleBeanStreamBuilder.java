package com.glue.feed.rennes.open.data.infolocale;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Occurrence;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.feed.GlueObjectBuilder;

public class InfoLocaleBeanStreamBuilder implements
		GlueObjectBuilder<InfoLocaleBean, Event> {

	private static final String DATA_SOURCE = "<a href=\"http://www.infolocale.fr\" title=\"Pour annoncer vos évènements dans cette base rendez-vous sur www.infolocale.fr.\" target=\"_blank\">Infolocale</a>";

	private static final String DATE_PATTERN = "dd/MM/yyyy"; // ex:
	// "14/05/77"

	private static final String SOURCE = "http://data.infolocale.fr/";

	static final Logger LOG = LoggerFactory
			.getLogger(InfoLocaleBeanStreamBuilder.class);

	private DateFormat format;

	private Map<String, String> catDico;

	public InfoLocaleBeanStreamBuilder() {
		format = new SimpleDateFormat(DATE_PATTERN);
		TimeZone tz = TimeZone.getTimeZone("UTC");
		format.setTimeZone(tz);
		// Get dictionary
		catDico = getCategoryDictionnary();
	}

	@Override
	public Event build(InfoLocaleBean bean) throws Exception {

		Event event = new Event();

		Date sdate = null, edate = null;
		try {
			sdate = format.parse(bean.getJourMin());
			edate = format.parse(bean.getJourMax());
		} catch (ParseException e) {
			LOG.error("Format de date incorrect " + sdate + " " + edate);
		}

		// Description
		StringBuilder description = new StringBuilder()
				.append(StringUtils.defaultString(bean.getTexteDebut()))
				.append("\n")
				.append(StringUtils.defaultString(bean.getTexteMilieu()))
				.append("\n")
				.append(StringUtils.defaultString(bean.getTexteFin()));

		// Venue address
		StringBuilder address = new StringBuilder()
				.append(StringUtils.defaultString(bean.getCodePostal()))
				.append(" ")
				.append(StringUtils.defaultString(bean.getCommune()));

		// Venue name
		String name = bean.getOrganismeNom();

		String[] coords = StringUtils.defaultString(bean.getCoordonneesGps())
				.split(",");

		// Venue latitude
		String latitude = StringUtils.defaultString(coords[0]);

		// Venue longitude
		String longitude = StringUtils.defaultString(coords[1]);

		// Event
		event.setTitle(StringUtils.defaultString(bean.getTitre()).trim());
		event.setDescription(description.toString().trim());
		event.setStartTime(sdate);
		event.setStopTime(edate);
		event.setCategory(getCategory(bean.getRubrique()));
		event.setPrice(StringUtils.defaultString(bean.getTarifGeneral()).trim());
		event.setTags(getTags(bean.getRubrique(), bean.getGenre()));
		event.setSource(DATA_SOURCE);

		// Images
		if (StringUtils.isNotEmpty(bean.getPhoto1Path())) {
			Image image = new Image();
			ImageItem item = new ImageItem();
			item.setUrl(bean.getPhoto1Path());
			image.setOriginal(item);
			image.setUrl(bean.getPhoto1Path());
			image.setSource(SOURCE);
			image.setSticky(true);
			image.setCaption(bean.getPhoto1Legende());
			image.setCreator(bean.getPhoto1Credit());
			event.getImages().add(image);
		}
		if (StringUtils.isNotEmpty(bean.getPhoto2Path())) {
			Image image = new Image();
			ImageItem item = new ImageItem();
			item.setUrl(bean.getPhoto2Path());
			image.setOriginal(item);
			image.setUrl(bean.getPhoto2Path());
			image.setSource(SOURCE);
			image.setCaption(bean.getPhoto2Legende());
			image.setCreator(bean.getPhoto2Credit());
			event.getImages().add(image);
		}
		if (StringUtils.isNotEmpty(bean.getPhoto3Path())) {
			Image image = new Image();
			ImageItem item = new ImageItem();
			item.setUrl(bean.getPhoto3Path());
			image.setOriginal(item);
			image.setUrl(bean.getPhoto3Path());
			image.setSource(SOURCE);
			image.setCaption(bean.getPhoto3Legende());
			image.setCreator(bean.getPhoto3Credit());
			event.getImages().add(image);
		}

		Venue venue = new Venue();
		venue.setName(name);

		if (StringUtils.isNotEmpty(latitude)
				&& StringUtils.isNotEmpty(longitude)) {
			double dlatitude = Double.parseDouble(latitude);
			double dlongitude = Double.parseDouble(longitude);
			venue.setLatitude(dlatitude);
			venue.setLongitude(dlongitude);
		}
		venue.setAddress(address.toString().trim());
		venue.setCity(bean.getCommune().trim());
		event.setVenue(venue);

		// Occurrences management
		if (StringUtils.isNotEmpty(bean.getJour1())
				&& StringUtils.isNotEmpty(bean.getJour2())) {
			event.getOccurrences().add(
					createOccurrence(format.parse(bean.getJour1()), venue));
			event.getOccurrences().add(
					createOccurrence(format.parse(bean.getJour2()), venue));

			if (StringUtils.isNotEmpty(bean.getJour3())) {
				event.getOccurrences().add(
						createOccurrence(format.parse(bean.getJour3()), venue));
			}
		}

		return event;
	}

	private Occurrence createOccurrence(Date date, Venue venue) {
		Occurrence occ = new Occurrence();
		occ.setStartTime(date);
		occ.setStopTime(date);
		occ.setVenue(venue);
		return occ;
	}

	// Get categories
	private Set<String> extractCategoriesFromField(String field) {

		Set<String> result = new HashSet<String>();
		if (field != null && field.length() > 0) {
			String[] values = field.split(",");
			for (int i = 0; i < values.length; i++) {
				result.add(values[i].toLowerCase().trim());
			}
		}
		return result;
	}

	private EventCategory getCategory(String field) {

		// Get all possible Categories
		Set<String> categories = extractCategoriesFromField(field);

		// Some main rules concert > spectacle
		if (categories.contains("concert")) {
			return EventCategory.MUSIC;
		}

		if (categories.contains("conference")
				|| categories.contains("conférence")) {
			return EventCategory.CONFERENCE;
		}

		if (categories.contains("spectacle")) {
			return EventCategory.PERFORMING_ART;
		}

		if (categories.contains("exposition")) {
			return EventCategory.EXHIBITION;
		}

		if (categories.contains("photographie")) {
			return EventCategory.EXHIBITION;
		}

		// Return the first found
		for (String catStr : categories) {

			// Try to find mapping from dico
			String category = catDico.get(catStr.toLowerCase());
			if (category != null && !"".equals(category)) {
				return EventCategory.valueOf(category.toUpperCase());
			}
		}

		return EventCategory.OTHER;
	}

	// Retrieve categories dictionary from property file
	private Map<String, String> getCategoryDictionnary() {

		Map<String, String> dico = new HashMap<>();
		Properties properties = new Properties();
		InputStream in = InfoLocaleBeanStreamBuilder.class
				.getResourceAsStream("/com/glue/feed/dico.properties");
		Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		try {
			properties.load(reader);
			for (Entry<Object, Object> entry : properties.entrySet()) {

				// Retrieve cat_name from key (key = glue.category.cat_name)
				String value = (String) entry.getKey();

				if (value.startsWith("glue.category")) {
					value = value.substring(value.lastIndexOf(".") + 1,
							value.length());

					// Split values (value = cat1#cat2# ...)
					String[] keys = ((String) entry.getValue()).split("#");
					for (int i = 0; i < keys.length; i++) {
						dico.put(keys[i], value);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dico;
	}

	private Set<Tag> getTags(String... tags) {
		Set<Tag> result = new HashSet<>();

		for (String value : tags) {
			String[] tmp = StringUtils.split(StringUtils.defaultString(value),
					',');
			for (String tag : tmp) {
				result.add(new Tag(tag.trim()));
			}
		}

		return result;
	}
}
