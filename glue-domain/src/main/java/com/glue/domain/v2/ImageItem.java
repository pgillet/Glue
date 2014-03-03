package com.glue.domain.v2;

/**
 * Each image contains several image items. These are specific image
 * characteristics.
 */

public class ImageItem {

    private String url;
    private int width;
    private int height;

    /**
     * @return the height
     */
    public int getHeight() {
	return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(int height) {
	this.height = height;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @return the width
     */
    public int getWidth() {
	return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(int width) {
	this.width = width;
    }

}
