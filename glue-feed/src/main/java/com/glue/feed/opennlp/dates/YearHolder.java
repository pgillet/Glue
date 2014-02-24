package com.glue.feed.opennlp.dates;

import java.util.List;

public class YearHolder extends DateHolder {

    public YearHolder(List<String> value, EventDateManager manager) {
	super(value, manager);
    }

    @Override
    public boolean checkValidity() {
	return true;
    }

    @Override
    void process() {
	manager.addYear(value.toString());
    }

}
