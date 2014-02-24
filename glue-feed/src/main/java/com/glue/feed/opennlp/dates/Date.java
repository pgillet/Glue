package com.glue.feed.opennlp.dates;

public class Date {

    String day = null;
    String month = null;
    String year = null;
    String time = null;

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	this.time = time;
    }

    public String getDay() {
	return day;
    }

    public void setDay(String day) {
	this.day = day;
    }

    public String getMonth() {
	return month;
    }

    public void setMonth(String month) {
	this.month = month;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public boolean hasDay() {
	return day != null;
    }

    public boolean hasMonth() {
	return month != null;
    }

    public boolean hasYear() {
	return year != null;
    }

    public boolean hasTime() {
	return time != null;
    }

    @Override
    public String toString() {
	return "Date [day=" + day + ", month=" + month + ", year=" + year
		+ ", time=" + time + "]";
    }

    public static Date duplicate(Date date) {
	Date newDate = new Date();
	newDate.setDay(date.getDay());
	newDate.setMonth(date.getMonth());
	newDate.setYear(date.getYear());
	newDate.setTime(date.getTime());
	return newDate;
    }

}
