package com.glue.feed.fnac.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Link;
import com.glue.domain.LinkType;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.feed.GlueObjectBuilder;

public class FnacProductBuilder implements GlueObjectBuilder<Product, Event> {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'00:00:00";
    private static final String DATE_PATTERN_2 = "dd/MM/yyy HH:mm";

    static final Logger LOG = LoggerFactory.getLogger(FnacProductBuilder.class);

    private DateFormat format1, format2;

    public FnacProductBuilder() {
	format1 = new SimpleDateFormat(DATE_PATTERN);
	format2 = new SimpleDateFormat(DATE_PATTERN_2);
	TimeZone tz = TimeZone.getTimeZone("UTC");
	format1.setTimeZone(tz);
	format2.setTimeZone(tz);
    }

    @Override
    public Event build(Product bean) throws Exception {

	String[] temp, temp2;

	Event event = new Event();

	// Get category
	// JAZ|Jazz ; 14J|Jazz/Blues/Gospel ; 1MC|Musique/Concerts
	EventCategory cat = null;
	temp = StringUtils.split(
		StringUtils.defaultString(bean.merchantCategory), " ; ");
	if (temp.length > 0) {
	    temp2 = StringUtils.split(temp[0], "|");
	    if (temp2 != null && temp2.length > 0) {
		cat = getCategory(temp2[0]);
	    }
	}

	// We don't want to manage that event
	if (cat == null) {
	    return null;
	}

	event.setCategory(cat);

	// Tags
	Set<Tag> tags = new HashSet<>();
	for (int i = 0; i < temp.length; i++) {
	    temp2 = StringUtils.split(temp[0], "|");
	    if (temp2.length > 1) {
		tags.addAll(getTags(temp2[1]));
	    }
	}
	event.setTags(tags);

	// Name
	event.setTitle(bean.name.trim());

	// Description
	temp = StringUtils.split(StringUtils.defaultString(bean.description),
		'|');
	if (temp.length == 3) {
	    event.setDescription(temp[2].trim());
	}

	// How many dates?
	temp = StringUtils.split(
		StringUtils.defaultString(bean.longDescription), '|');

	Date dateFrom = format1
		.parse(StringUtils.defaultString(bean.validFrom));
	Date dateTo = format1.parse(StringUtils.defaultString(bean.validTo));

	// more than 1 recurrence?
	if (temp.length > 1) {

	    // Manage recurrences
	} else {
	    if (temp.length > 0) {
		dateFrom = format2.parse(StringUtils.defaultString(temp[0]));
	    }
	}
	event.setStartTime(dateFrom);
	event.setStopTime(dateTo);

	// Price
	event.setPrice(StringUtils.defaultString(bean.price));

	// Performers

	// Source
	event.setSource("www.francebillet.com");

	// Ticket
	String link = StringUtils.defaultString(bean.deepLink);
	if (!link.isEmpty()) {
	    Link aLink = new Link();
	    aLink.setType(LinkType.TICKET);
	    aLink.setUrl(link);
	    event.getLinks().add(aLink);
	}

	// Images
	Image image = new Image();
	image.setSource("www.francebillet.com");
	image.setSticky(true);
	boolean find = false;
	if (StringUtils.isNotEmpty(bean.largeImage)) {
	    ImageItem item = new ImageItem();
	    item.setUrl(bean.largeImage);
	    image.setLarge(item);
	    image.setOriginal(item);
	    find = true;
	}
	if (StringUtils.isNotEmpty(bean.mediumImage)) {
	    ImageItem item = new ImageItem();
	    item.setUrl(bean.mediumImage);
	    image.setMedium(item);
	    if (image.getOriginal() == null) {
		image.setOriginal(item);
	    }
	    find = true;
	}
	if (StringUtils.isNotEmpty(bean.smallImage)) {
	    ImageItem item = new ImageItem();
	    item.setUrl(bean.smallImage);
	    image.setSmall(item);
	    if (image.getOriginal() == null) {
		image.setOriginal(item);
	    }
	    find = true;
	}

	if (find) {
	    event.getImages().add(image);
	}

	// Venue
	Venue venue = new Venue();

	// Name
	venue.setName(StringUtils.defaultString(bean.manufacturer));

	// City
	temp = StringUtils.split(StringUtils.defaultString(bean.extra2), '|');
	String city = temp[1].trim();
	venue.setCity(city);

	// Address
	String address = StringUtils.defaultString(bean.terms).trim();
	String postalCode = temp[0].trim();
	venue.setAddress((address + " " + postalCode + " " + city).trim());

	// Geo
	temp = StringUtils.split(
		StringUtils.defaultString(bean.shippingHandling), '|');
	venue.setLatitude(Double.parseDouble(temp[3]));
	venue.setLongitude(Double.parseDouble(temp[2]));

	event.setVenue(venue);

	return event;
    }

    private EventCategory getCategory(String category) {

	if (category == null || category.equals("CLU")
		|| category.equals("FEU") || category.equals("LEC")
		|| category.equals("LON") || category.equals("POE")
		|| category.equals("RHO") || category.equals("ROC")
		|| category.equals("SOI")) {
	    return EventCategory.OTHER;
	}

	if (category.equals("ARM") || category.equals("ASC")
		|| category.equals("ASP") || category.equals("ATH")
		|| category.equals("BAS") || category.equals("BOX")
		|| category.equals("CYC") || category.equals("EQU")
		|| category.equals("FOO") || category.equals("GOL")
		|| category.equals("HAN") || category.equals("NAT")
		|| category.equals("RUG") || category.equals("SPM")
		|| category.equals("TEN") || category.equals("VBA")) {
	    return EventCategory.SPORT;
	}

	if (category.equals("ASC") || category.equals("CAB")
		|| category.equals("CMU") || category.equals("COM")
		|| category.equals("CTE") || category.equals("CTH")
		|| category.equals("DCL") || category.equals("DCO")
		|| category.equals("DDM") || category.equals("DEF")
		|| category.equals("ASC") || category.equals("CAB")
		|| category.equals("CMU") || category.equals("COM")
		|| category.equals("CTE") || category.equals("CTH")
		|| category.equals("DCL") || category.equals("DCO")
		|| category.equals("DDM") || category.equals("DEF")
		|| category.equals("DTR") || category.equals("GLA")
		|| category.equals("GSP") || category.equals("HIP")
		|| category.equals("HUM") || category.equals("IMP")
		|| category.equals("LYR") || category.equals("MAG")
		|| category.equals("MIM") || category.equals("MUC")
		|| category.equals("ONE") || category.equals("OPT")
		|| category.equals("PER") || category.equals("SEL")
		|| category.equals("SES") || category.equals("SPE")
		|| category.equals("TCL") || category.equals("TCO")
		|| category.equals("TVI") || category.equals("VAU")) {
	    return EventCategory.PERFORMING_ART;
	}

	if (category.equals("BLU") || category.equals("CAT")
		|| category.equals("CHO") || category.equals("CIC")
		|| category.equals("GOL") || category.equals("HAR")
		|| category.equals("JAZZ") || category.equals("MAC")
		|| category.equals("MAF") || category.equals("MAI")
		|| category.equals("MBA") || category.equals("MCL")
		|| category.equals("MCO") || category.equals("MEL")
		|| category.equals("MSA") || category.equals("MTF")
		|| category.equals("MUS") || category.equals("POP")
		|| category.equals("RAI") || category.equals("RAP")
		|| category.equals("REG") || category.equals("SOU")
		|| category.equals("VAF") || category.equals("VAI")) {
	    return EventCategory.MUSIC;
	}

	if (category.equals("CON") || category.equals("DEB")
		|| category.equals("THR") || category.equals("SUC")) {
	    return EventCategory.CONFERENCE;
	}

	if (category.equals("CTR") || category.equals("DEN")
		|| category.equals("ETH") || category.equals("MAR")
		|| category.equals("MUE") || category.equals("NCI")) {
	    return EventCategory.YOUTH;
	}

	if (category.equals("EXP") || category.equals("MEX")) {
	    return EventCategory.EXHIBITION;
	}
	return null;
    }

    private Set<Tag> getTags(String tags) {
	Set<Tag> result = new HashSet<>();
	String[] tmp;

	if (tags == null)
	    return null;

	tmp = StringUtils.split(tags, "/");
	for (int i = 0; i < tmp.length; i++) {
	    result.add(new Tag(tmp[i].trim()));
	}

	return result;

    }
}
