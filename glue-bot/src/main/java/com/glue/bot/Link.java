package com.glue.bot;

import java.net.URL;

public class Link {

    private String url;

    private String name;

    public Link(String url) {
	this(url, null);
    }

    public Link(URL url) {
	this(url.toString(), null);
    }

    public Link(URL url, String name) {
	this(url.toString(), name);
    }

    public Link(String url, String name){
	this.url = url;
	this.name = name;
    }

    public String getUrl() {
	return url;
    }

    public String getName() {
	return name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Link other = (Link) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	return true;
    }

}
