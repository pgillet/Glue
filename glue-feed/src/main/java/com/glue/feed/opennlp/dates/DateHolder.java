package com.glue.feed.opennlp.dates;

import java.util.List;

public abstract class DateHolder {

    EventDateManager manager;
    List<String> value;

    public DateHolder(List<String> value, EventDateManager manager) {
	this.manager = manager;
	this.value = value;
    }

    abstract void process();

    abstract boolean checkValidity();
}
