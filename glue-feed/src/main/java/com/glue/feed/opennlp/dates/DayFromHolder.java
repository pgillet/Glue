package com.glue.feed.opennlp.dates;

import java.util.List;

public class DayFromHolder extends DateHolder {

    public DayFromHolder(List<String> value, EventDateManager manager) {
	super(value, manager);
    }

    @Override
    public boolean checkValidity() {
	return true;
    }

    @Override
    void process() {
	manager.addDayFrom(value.toString());
    }
}
