package com.glue.webapp.beans;

enum DisplayType {

    LIST("display_list"), TABLE("display_table"), GRID("display_grid"), MAP(
	    "display_map");

    private final String labelKey;

    private DisplayType(String labelKey) {
	this.labelKey = labelKey;
    }

    /**
     * For EL access.
     * 
     * @return
     */
    public String getName() {
	return name();
    }

    /**
     * @return the labelKey
     */
    public String getLabelKey() {
	return labelKey;
    }

}