package com.glue.feed.opennlp.dates;

import java.util.List;

public class DayHolder extends DateHolder {

    public DayHolder(List<String> value, EventDateManager manager) {
	super(value, manager);
    }

    @Override
    public boolean checkValidity() {
	return true;
    }

    @Override
    void process() {
	manager.addDay(value.toString());
    }
}
