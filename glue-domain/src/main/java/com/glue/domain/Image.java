package com.glue.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Image object.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Image {

    /**
     * Id of the image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Image caption.
     */
    private String caption;

    /**
     * User who created the images.
     */
    private String creator;

    /**
     * Image source.
     */
    @Column(length = 2048)
    private String source;

    /**
     * Whether this is the default/sticky image or not
     */
    private boolean sticky;

    /**
     * Image URL.
     */
    @Column(length = 2048)
    private String url;

    /**
     * Image width in pixels
     */
    private int width;

    /**
     * Image height in pixels
     */
    private int height;

    /**
     * Small image
     */
    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private ImageItem small;

    /**
     * Medium image
     */
    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private ImageItem medium;

    /**
     * Large image
     */
    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private ImageItem large;

    /**
     * Block image
     */
    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private ImageItem block;

    /**
     * Thumb image
     */
    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private ImageItem thumb;

    /**
     * Original image
     */
    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private ImageItem original;

    /**
     * @return the block
     */
    public ImageItem getBlock() {
	return block;
    }

    /**
     * @param block
     *            the block to set
     */
    public void setBlock(ImageItem block) {
	this.block = block;
    }

    /**
     * @return the caption
     */
    public String getCaption() {
	return caption;
    }

    /**
     * @param caption
     *            the caption to set
     */
    public void setCaption(String caption) {
	this.caption = caption;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
	return creator;
    }

    /**
     * @param creator
     *            the creator to set
     */
    public void setCreator(String creator) {
	this.creator = creator;
    }

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
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the large
     */
    public ImageItem getLarge() {
	return large;
    }

    /**
     * @param large
     *            the large to set
     */
    public void setLarge(ImageItem large) {
	this.large = large;
    }

    /**
     * @return the medium
     */
    public ImageItem getMedium() {
	return medium;
    }

    /**
     * @param medium
     *            the medium to set
     */
    public void setMedium(ImageItem medium) {
	this.medium = medium;
    }

    /**
     * @return the original
     */
    public ImageItem getOriginal() {
	return original;
    }

    /**
     * @param original
     *            the original to set
     */
    public void setOriginal(ImageItem original) {
	this.original = original;
    }

    /**
     * @return the small
     */
    public ImageItem getSmall() {
	return small;
    }

    /**
     * @param small
     *            the small to set
     */
    public void setSmall(ImageItem small) {
	this.small = small;
    }

    /**
     * @return the source
     */
    public String getSource() {
	return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
	this.source = source;
    }

    /**
     * @return the sticky
     */
    public boolean isSticky() {
	return sticky;
    }

    /**
     * @param sticky
     *            the sticky to set
     */
    public void setSticky(boolean sticky) {
	this.sticky = sticky;
    }

    /**
     * @return the thumb
     */
    public ImageItem getThumb() {
	return thumb;
    }

    /**
     * @param thumb
     *            the thumb to set
     */
    public void setThumb(ImageItem thumb) {
	this.thumb = thumb;
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
