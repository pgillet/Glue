package com.glue.feed.toulouse.open.data.so;

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
import com.glue.domain.Venue;
import com.glue.feed.GlueObjectBuilder;

public class EventBeanStreamBuilder implements
	GlueObjectBuilder<EventBean, Event> {

    private static final String DATE_PATTERN = "dd/MM/yy"; // ex: "14/05/77"

    private static final String DATA_SOURCE = "<a href=\"http://data.grandtoulouse.fr\" target=\"_blank\"><img alt=\"This material is Open Data\" border=\"0\" src=\"http://assets.okfn.org/images/ok_buttons/od_80x15_blue.png\" /></a>";

    static final Logger LOG = LoggerFactory
	    .getLogger(EventBeanStreamBuilder.class);

    private DateFormat format;

    private Map<String, String> catDico;

    public EventBeanStreamBuilder() {
	format = new SimpleDateFormat(DATE_PATTERN);
	TimeZone tz = TimeZone.getTimeZone("UTC");
	format.setTimeZone(tz);
	// Get dictionary
	catDico = getCategoryDictionnary();
    }

    @Override
    public Event build(EventBean bean) throws Exception {
	// Dates
	String strdate = bean.getDateDébut();
	String enddate = bean.getDateFin();
	Date sdate = null;
	Date edate = null;
	try {
	    sdate = format.parse(strdate);

	    // If endate empty, set endate to start_date
	    if ("".equals(enddate)) {
		edate = sdate;
	    } else {
		edate = format.parse(enddate);
	    }
	} catch (ParseException e) {
	    LOG.error("Format de date incorrect " + sdate + " " + edate);
	}

	// Description
	StringBuilder description = new StringBuilder()
		.append(StringUtils.defaultString(bean.getDescriptifCourt()))
		.append("\n")
		.append(StringUtils.defaultString(bean.getDescriptifLong()))
		.append("\n")
		.append(StringUtils.defaultString(bean.getHoraires()));

	// Venue address
	StringBuilder address = new StringBuilder()
		.append(StringUtils.defaultString(bean.getLieuAdresse1()))
		.append(" ")
		.append(StringUtils.defaultString(bean.getLieuAdresse2()))
		.append(" ")
		.append(StringUtils.defaultString(bean.getLieuAdresse3()))
		.append(" ")
		.append(StringUtils.defaultString(bean.getCodePostal()))
		.append(" ")
		.append(StringUtils.defaultString(bean.getCommune()));

	// Venue name
	String name = bean.getLieuNom();

	// Venue latitude
	String latitude = bean.getGooglemapLatitude();

	// Venue longitude
	String longitude = bean.getGooglemapLongitude();

	Event event = new Event();
	event.setTitle(bean.getNomDeLaManifestation());
	event.setDescription(description.toString().trim());
	event.setStartTime(sdate);
	event.setStopTime(edate);
	EventCategory cat = getCategory(bean.getTypeDeManifestation(),
		bean.getCatégorieDeLaManifestation(),
		bean.getThèmeDeLaManifestation());
	event.setCategory(cat);
	event.setPrice(bean.getTarifNormal());
	event.setSource(DATA_SOURCE);

	Venue venue = new Venue();
	venue.setName(name);

	if (StringUtils.isNotEmpty(latitude)
		&& StringUtils.isNotEmpty(longitude)) {
	    double dlatitude = Double.parseDouble(latitude);
	    double dlongitude = Double.parseDouble(longitude);
	    venue.setLatitude(dlatitude);
	    venue.setLongitude(dlongitude);
	    // Reverse Geoconding (only 2500 request a day)
	    /*
	     * geocoderRequest = new GeocoderRequestBuilder() .setLocation(new
	     * LatLng(BigDecimal.valueOf(dlatitude),
	     * BigDecimal.valueOf(dlongitude)))
	     * .setLanguage("fr").getGeocoderRequest(); geocoderResponse =
	     * geocoder.geocode(geocoderRequest); if
	     * (!geocoderResponse.getResults().isEmpty()) {
	     * System.out.println(geocoderResponse
	     * .getResults().get(0).getFormattedAddress());
	     * venue.setAddress(geocoderResponse
	     * .getResults().get(0).getFormattedAddress()); }
	     */
	}
	venue.setAddress(address.toString().trim());

	venue.setUrl(removeUrlExceptions(bean.getRéservationSiteInternet()));
	venue.setCity(StringUtils.defaultString(bean.getCommune()));
	event.setVenue(venue);

	return event;
    }

    // Get categories from a string like "CAT1, CAT2, CAT3"
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

    private String removeUrlExceptions(String url) {
	if (url != null && url.length() > 0) {
	    Set<String> exceptions = new HashSet<String>();
	    exceptions.add("www.fnac.com");
	    exceptions.add("www.box.fr");
	    for (String exception : exceptions) {
		url = url.replaceAll(exception, "");
	    }

	    // On ne prend que la première des URL
	    if (url.contains(" ")) {
		url = url.substring(0, url.indexOf(" "));
	    }
	}
	return url;
    }

    private EventCategory getCategory(String field1, String field2,
	    String field3) {

	// Get all possible Categories
	Set<String> categories = extractCategoriesFromField(field1);
	categories.addAll(extractCategoriesFromField(field2));
	categories.addAll(extractCategoriesFromField(field3));

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

    // Retrieve categories dictionnary from property file
    private Map<String, String> getCategoryDictionnary() {

	Map<String, String> dico = new HashMap<>();
	Properties properties = new Properties();
	InputStream in = EventBeanStreamBuilder.class
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

}
