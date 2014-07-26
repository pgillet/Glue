package com.glue.webapp.beans;

import java.util.Date;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

@ManagedBean
@ApplicationScoped
public class DateUtilBean {

    /**
     * Tells whether or not the given date has time specified, i.e. the number
     * of minutes since the start of the day is greater than zero.
     */
    public boolean hasTime(Date date) {
	DateTime dt = new DateTime(date, DateTimeZone.UTC);
	return dt.getMinuteOfDay() > 0;
    }

}
