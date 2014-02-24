package com.glue.feed.opennlp.dates;

import java.util.List;

public class TimeHolder extends DateHolder {

    public TimeHolder(List<String> value, EventDateManager manager) {
	super(value, manager);
    }

    @Override
    public boolean checkValidity() {
	return true;
    }

    @Override
    void process() {
	manager.addTime(value.toString());
    }

}
