package com.glue.content;

/**
 * Image renditions.
 * 
 * @author pgillet
 * 
 */
public enum ImageRendition {

    THUMBNAIL(0), SMALL(1), MEDIUM(2), LARGE(3), BLOCK(4), ORIGINAL(5);

    private final int level;

    private ImageRendition(int level) {
	this.level = level;
    }

    public int getLevel() {
	return level;
    }

    /**
     * Returns the rendition with the upper level, or null if this rendition is
     * the top level rendition.
     * 
     * @return
     */
    ImageRendition upper() {
	// The natural order is the order in which the constants are declared.
	ImageRendition[] renditions = values();

	if (level < renditions.length - 1) {
	    return renditions[level + 1];
	}

	return null;
    }

}
