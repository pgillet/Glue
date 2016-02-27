package com.glue.bot.command;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.bot.Validate;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.chain.Contract;
import com.glue.domain.Event;
import com.glue.domain.Occurrence;
import com.glue.time.DateTimeProcessor;
import com.glue.time.DateTimeProcessor.Interval;

public class DateTimeCommand extends BaseCommand implements Contract {

    public static final Logger LOG = LoggerFactory
	    .getLogger(DateTimeCommand.class);

    private DateTimeProcessor dateTimeProcessor;

    private String datePatternKey = SelectorKeys.DATE_PATTERN_KEY;

    private String dateSelectorKey = SelectorKeys.DATE_SELECTOR_KEY;

    private String localeKey = SelectorKeys.LOCALE_KEY;

    public DateTimeCommand() throws IOException {
	dateTimeProcessor = new DateTimeProcessor();
    }

    public String getDatePatternKey() {
	return datePatternKey;
    }

    public void setDatePatternKey(String datePatternKey) {
	this.datePatternKey = datePatternKey;
    }

    public String getDateSelectorKey() {
	return dateSelectorKey;
    }

    public void setDateSelectorKey(String dateSelectorKey) {
	this.dateSelectorKey = dateSelectorKey;
    }

    public String getLocaleKey() {
	return localeKey;
    }

    public void setLocaleKey(String localeKey) {
	this.localeKey = localeKey;
    }

    @Override
    public void require(Context context) throws Exception {
	// No op
    }

    /**
     * <p>
     * Process the dates and times of the current event.
     * </p>
     *
     * @param context
     *            {@link Context} in which we are operating
     *
     * @throws Exception
     *             if an error occurs during execution.
     */
    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Element elem = (Element) context.get(getElementKey());
	Event event = (Event) context.get(getEventKey());

	String datePattern = (String) context.get(getDatePatternKey());
	String dateSelector = (String) context.get(getDateSelectorKey());
	Locale locale = (Locale) context.get(getLocaleKey());

	Elements elems = dateSelector != null ? elem.select(dateSelector)
		: elem.children();
	Validate.notEmpty(elems);

	String dates = elems.text(); // html() ?

	if (datePattern != null) {

	    DateFormat df = new SimpleDateFormat(datePattern, locale);
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    df.setTimeZone(tz);

	    Date startTime = df.parse(dates);
	    event.setStartTime(startTime);
	    event.setStopTime(startTime);
	} else {
	    boolean success = dateTimeProcessor.process(dates);

	    if (success) {

		Set<Interval> intervals = dateTimeProcessor.getIntervals();

		if (!intervals.isEmpty()) {
		    Interval it = dateTimeProcessor.getLastElement(intervals);

		    event.setStartTime(it.getStartTime());
		    event.setStopTime(it.getStopTime());
		} else {

		    Set<Date> datesCol = dateTimeProcessor.getDates();
		    if (datesCol.size() == 1) {
			Date d = datesCol.iterator().next();
			event.setStartTime(d);
			event.setStopTime(d);
		    } else {
			for (Date date : datesCol) {
			    Occurrence occur = new Occurrence();
			    occur.setStartTime(date);
			    occur.setStopTime(date);
			}
		    }
		}

	    } else {
		throw new ParseException("Dates could not be found", -1);
	    }
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;

    }

    @Override
    public void ensure(Context context) throws Exception {
	Event event = (Event) context.get(getEventKey());

	Date startTime = event.getStartTime();
	Date stopTime = event.getStopTime();

	if (startTime == null || stopTime == null) {
	    throw new IllegalArgumentException("Date is null");
	}

    }

}
