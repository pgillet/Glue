package com.glue.webapp.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public String printDateInterval(Date startTime, Date stopTime) {
	StringBuilder sb = new StringBuilder();

	String datePattern = FacesUtil.getString("date_format_long");
	DateFormat df = new SimpleDateFormat(datePattern);
	
	String timePattern = FacesUtil.getString("time_format");
	DateFormat tf = new SimpleDateFormat(timePattern);

	if (stopTime != null && startTime.before(stopTime)) {
	    sb.append(FacesUtil.getString("stream_date_from")).append(" ")
		    .append(df.format(startTime)).append(" ")
		    .append(FacesUtil.getString("stream_date_to")).append(" ")
		    .append(df.format(stopTime));
	} else {
	    sb.append(df.format(startTime));
	    if(hasTime(startTime)){
		sb = sb.append(" ").append(tf.format(startTime));
	    }
	}

	return sb.toString();
    }

}
