package com.glue.domain;

public enum LinkType {
    OFFICIAL_SITE(1), PODCAST(2), WEBCAST(3), WEBSITE(4);

    private int typeId;

    private LinkType(int typeId) {
	this.typeId = typeId;
    }

    public int asInteger() {
	return typeId;
    }

}