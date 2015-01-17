package com.glue.feed.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.types.heideltime.Timex3Interval;

public class DateTimeProcessor {

    static final Logger LOG = LoggerFactory.getLogger(DateTimeProcessor.class);

    private static final String configProps = "/com/glue/feed/heideltime/config.props";

    private Date startTime;

    public Date getStartTime() {
	return startTime;
    }

    public Date getStopTime() {
	return stopTime;
    }

    private Date stopTime;

    /**
     * 
     * @param document
     * @return true if the given document has been successfully processed, i.e.
     *         a date interval or a single date has been found.
     * @throws DocumentCreationTimeMissingException
     * @throws ParseException
     */
    public boolean process(String document)
	    throws DocumentCreationTimeMissingException, ParseException {

	boolean success = false;
	String configPath = DateTimeProcessor.class.getResource(configProps)
		.getFile();

	HeidelTimeStandalone heidel = new HeidelTimeStandalone(Language.FRENCH,
		DocumentType.NEWS, OutputType.TIMEML, configPath,
		POSTagger.STANFORDPOSTAGGER, true);

	Date documentCreationTime = Calendar.getInstance().getTime();

	LOG.info("Document = " + document);
	
	JavaTimeResultFormatter rf = new JavaTimeResultFormatter();

	String result = heidel
		.process(document + ".", documentCreationTime,
		rf);
	
	LOG.info(result);

	// -- Intervals --

	Map<Integer, Timex3Interval> intervals = rf.getIntervals();
	Map<Integer, Timex3> timexes = rf.getTimexes();

	if (!intervals.isEmpty()) {
	    Timex3Interval interval = getLastElement(intervals.values());
	    Timex3IntervalAsDateDecorator decorated = new Timex3IntervalAsDateDecorator(
		    interval);

	    startTime = decorated.getEarliestBegin();
	    stopTime = decorated.getLatestEnd();
	    success = true;
	} else if (!timexes.isEmpty()) {
	    Timex3 timex = getLastElement(timexes.values());
	    Timex3AsDateDecorator decorated = new Timex3AsDateDecorator(timex);

	    startTime = decorated.getValueAsDate();
	    success = true;
	}


	return success;
    }
    
    private <T> T getLastElement(final Collection<T> c) {
	final Iterator<T> itr = c.iterator();
	T lastElement = itr.next();
	while (itr.hasNext()) {
	    lastElement = itr.next();
	}
	return lastElement;
    }

    public static void main(String[] args)
	    throws DocumentCreationTimeMissingException, ParseException {
	String document = "Du 21 au 30 janvier 2015.";
	// document = "Tous les samedis et vendredis de 20h à 22h00.";
	// document = "mer 28 jan 15, 20:00.";
	
	// document = "sam 3 jan.";
	//
	// document = "Du 11 novembre 2014 au 15 janvier 2015";
	//
	// document = "Le 16 janvier 2015";
	//
	document = "SAMEDI 24 JANVIER 2015 tagada ... jeudi 12 février 2015";

	// document = "21.03";

	DateTimeProcessor dateTimeProcessor = new DateTimeProcessor();
	dateTimeProcessor.process(document);

	LOG.info("Start time = " + dateTimeProcessor.getStartTime());
	LOG.info("Stop time = " + dateTimeProcessor.getStopTime());
   
    }

}
