package com.glue.feed.opennlp.dates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import opennlp.tools.util.Span;

public class EventDateManager {

    List<Span> spans;
    List<String> tokens;
    List<EventDate> eventDates = new LinkedList<>();
    List<EventDate> lastEventDatesClosed = new ArrayList<>();
    EventDate lastEventDate = null;
    Span currentSpan, previousSpan = null;

    public EventDateManager(List<String> tokens, List<Span> spans) {
	this.spans = spans;
	this.tokens = tokens;
    }

    public List<EventDate> process() {
	for (Span sp : spans) {

	    currentSpan = sp;

	    DateHolder holder = HolderFactory.create(sp.getType(),
		    tokens.subList(sp.getStart(), sp.getEnd()), this);

	    // Is it a valid holder?
	    if (holder.checkValidity()) {
		holder.process();
	    }

	    previousSpan = sp;
	}
	return eventDates;
    }

    // Add a new day "from" that event
    public void addDayFrom(String day) {
	EventDate event = new EventDate();
	event.setFrom(true);
	event.addNewDay(day);
	addEvent(event);
    }

    // Add a new day for that event
    public void addDay(String day) {

	// case where previous day was a "day from" (du ... au ...)
	if (isFromLastEventDate()) {

	    // Update end date
	    lastEventDate.getEnd().setDay(day);
	}

	// Otherwise create a new event date for that event
	else {
	    EventDate event = new EventDate();
	    event.addNewDay(day);
	    addEvent(event);
	}

    }

    // Check if the last event is OPEN and is a "from" event date
    private boolean isFromLastEventDate() {
	return (lastEventDate != null
		&& lastEventDate.getStatus() == Status.OPEN && lastEventDate
		    .isFrom());
    }

    public void addMonth(String month) {

	// Get all previous event date still "OPEN" and
	List<EventDate> opens = getOpenEventDates();

	// and add month
	for (EventDate event : opens) {
	    event.setMonth(month);
	}
    }

    public void addYear(String year) {

	// Get all previous event date still "OPEN" and
	List<EventDate> opens = getOpenEventDates();

	// and add year
	for (EventDate event : opens) {
	    event.setYear(year);
	}
    }

    public void addTime(String time) {

	// Case1 : previous span was "hour", so we have to add a new time for
	// all
	// event date previously closed
	// samedi 15 et dimanche 17 à 17H30 et 18H15
	if (previousSpan.getType().equals("hour")) {

	    for (EventDate eDate : lastEventDatesClosed) {
		EventDate newEventDate = EventDate.duplicate(eDate);
		newEventDate.setTime(time);
		eventDates.add(newEventDate);
	    }

	}

	else {

	    // Get all previous event date still "OPEN" and
	    List<EventDate> opens = getOpenEventDates();

	    // and add hour
	    lastEventDatesClosed.clear();
	    for (EventDate event : opens) {
		event.setTime(time);

		// Close the event date ...
		event.setStatus(Status.CLOSE);
		lastEventDatesClosed.add(event);
	    }

	}

    }

    private List<EventDate> getOpenEventDates() {

	List<EventDate> opens = new ArrayList<>();
	ListIterator<EventDate> it = eventDates.listIterator(eventDates.size());
	boolean ok = true;

	while (it.hasPrevious() && ok) {
	    EventDate event = it.previous();
	    ok = event.getStatus() == Status.OPEN;
	    if (ok) {
		opens.add(event);
	    }
	}

	return opens;
    }

    private void addEvent(EventDate event) {
	eventDates.add(event);
	lastEventDate = event;
    }
}
