package com.glue.feed.opennlp.dates;

import java.util.List;

public class MonthHolder extends DateHolder {

    public MonthHolder(List<String> value, EventDateManager manager) {
	super(value, manager);
    }

    @Override
    public boolean checkValidity() {
	return true;
    }

    @Override
    void process() {
	manager.addMonth(value.toString());
    }

}
