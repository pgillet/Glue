package com.glue.feed.html.demo;

import com.glue.feed.html.HtmlValue;
import com.glue.feed.html.Selector;

@Selector(value = "div.spectacle")
public class DidascalieEvent {

    @Selector(value = "p.dates")
    public String dates;

    @Selector(value = "div.bloc_content > h2")
    public String title;

    @Selector(value = "div.technique > p")
    public String author;

    @Selector(value = "div.texte > p:first-child")
    public String prix;

    @Selector(value = "div.chapo")
    @HtmlValue
    public String description1;

    @Selector(value = "div.texte")
    @HtmlValue
    public String description2;

    public String getDates() {
	return dates;
    }

    public void setDates(String dates) {
	this.dates = dates;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getPrix() {
	return prix;
    }

    public void setPrix(String prix) {
	this.prix = prix;
    }

    public String getDescription1() {
	return description1;
    }

    public void setDescription1(String description1) {
	this.description1 = description1;
    }

    public String getDescription2() {
	return description2;
    }

    public void setDescription2(String description2) {
	this.description2 = description2;
    }

    @Override
    public String toString() {
	return "DidascalieEvent [\ndates=" + dates + "\ntitle=" + title
		+ "\nauthor=" + author + "\nprix=" + prix + "\ndescription1="
		+ description1 + "\ndescription2=" + description2 + "]\n";
    }


}
