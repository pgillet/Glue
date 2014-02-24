package com.glue.feed.opennlp.dates;

public class EventDate {

    private Date start = new Date();
    private Date end = new Date();
    private boolean from = false;
    private Status status = Status.OPEN;

    public Date getStart() {
	return start;
    }

    public void setStart(Date start) {
	this.start = start;
    }

    public Date getEnd() {
	return end;
    }

    public void setEnd(Date end) {
	this.end = end;
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    @Override
    public String toString() {
	return "Event [from = " + from + " start=" + start + ", end=" + end
		+ "]";
    }

    public boolean isFrom() {
	return from;
    }

    public void setFrom(boolean from) {
	this.from = from;
    }

    public void addNewDay(String day) {
	start.setDay(day);
	end.setDay(day);
    }

    public void setMonth(String month) {
	if (!start.hasMonth()) {
	    start.setMonth(month);
	}
	if (!end.hasMonth()) {
	    end.setMonth(month);
	}
    }

    public void setYear(String year) {
	if (!start.hasYear()) {
	    start.setYear(year);
	}
	if (!end.hasYear()) {
	    end.setYear(year);
	}
    }

    public void setTime(String time) {
	start.setTime(time);
    }

    public static EventDate duplicate(EventDate e) {
	EventDate eNew = new EventDate();
	eNew.setFrom(e.isFrom());
	eNew.setStart(Date.duplicate(e.getStart()));
	eNew.setEnd(Date.duplicate(e.getEnd()));
	eNew.setStatus(e.getStatus());
	return eNew;
    }
}
