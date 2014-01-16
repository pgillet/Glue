package com.glue.webapp.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class Hours implements Serializable {

	private Map<Date, String> dateMap;

	@PostConstruct
	public void init() {
		dateMap = new LinkedHashMap<Date, String>();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0); // Epoch

		final int amount = 30;
		final String key = "time_format";
		String timeFormat = FacesUtil.getString(key);

		DateFormat df = new SimpleDateFormat(timeFormat);
		TimeZone tz = TimeZone.getTimeZone("UTC");
		df.setTimeZone(tz);

		final int n = 24 * 60 / amount;

		for (int i = 0; i < n; i++) {
			Date d = cal.getTime();
			dateMap.put(d, df.format(d));
			cal.add(Calendar.MINUTE, amount);
		}
	}

	public Map<Date, String> getDateMap() {
		init();
		return dateMap;
	}

	public void setDateMap(Map<Date, String> dateMap) {
		this.dateMap = dateMap;
	}

}
