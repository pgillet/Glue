package com.glue.feed.toulouse.bikini;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/*
 * Represents one RSS message
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	String title;
	String description;
	String link;
	String pubDate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item [title=" + title + ", description=" + description
				+ ", link=" + link + ", pubDate=" + pubDate + "]";
	}

}
