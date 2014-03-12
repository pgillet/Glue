package com.glue.client.android.stream;

import com.glue.domain.Event;

public class EventData extends Event {

    private static EventData instance = new EventData();

    private EventData() {
    }

    public static EventData getInstance() {
	return instance;
    }

}
