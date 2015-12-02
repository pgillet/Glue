package com.glue.feed.html.demo;

import com.glue.feed.html.AttributeValue;
import com.glue.feed.html.HtmlValue;
import com.glue.feed.html.Selector;

@Selector(value = "div#encartDetailSpectacle")
public class BikiniEvent {

    @Selector(value = "div#blocImage > a")
    @AttributeValue(name = "href")
    public String thumbnail;

    @Selector(value = "div#blocContenu > div#date")
    public String date;

    @Selector(value = "div#blocContenu > h2")
    public String title;

    @Selector(value = "div#blocContenu > div#type")
    public String type;

    @Selector(value = "div#blocContenu > div#prix")
    public String price;

    @Selector(value = "div#infos > div#salle > h3")
    public String venueName;

    @Selector(value = "div#infos > div#salle > div#adresse")
    public String venueAddress;

    @Selector(value = "div#texte")
    @HtmlValue
    public String description;

    @Selector(value = "div#video > iframe")
    @AttributeValue(name = "src")
    public String video;

    public BikiniEvent() {
    }

    public String getThumbnail() {
	return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
	this.thumbnail = thumbnail;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getPrice() {
	return price;
    }

    public void setPrice(String price) {
	this.price = price;
    }

    public String getVenueName() {
	return venueName;
    }

    public void setVenueName(String venueName) {
	this.venueName = venueName;
    }

    public String getVenueAddress() {
	return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
	this.venueAddress = venueAddress;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getVideo() {
	return video;
    }

    public void setVideo(String video) {
	this.video = video;
    }

    @Override
    public String toString() {
	return "BikiniEvent [thumbnail=" + thumbnail + ", date=" + date
		+ ", title=" + title + ", type=" + type + ", price=" + price
		+ ", venueName=" + venueName + ", venueAddress=" + venueAddress
		+ ", description=" + description + ", video=" + video + "]";
    }



}
