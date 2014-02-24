package com.glue.feed.opennlp.dates;

import java.util.List;

public class HolderFactory {

    public static DateHolder create(String type, List<String> value,
	    EventDateManager dateManager) {
	if (type.equals("day"))
	    return new DayHolder(value, dateManager);
	else if (type.equals("day_from"))
	    return new DayFromHolder(value, dateManager);
	else if (type.equals("month"))
	    return new MonthHolder(value, dateManager);
	else if (type.equals("year"))
	    return new YearHolder(value, dateManager);
	else if (type.equals("hour"))
	    return new TimeHolder(value, dateManager);
	return null;
    }
}
