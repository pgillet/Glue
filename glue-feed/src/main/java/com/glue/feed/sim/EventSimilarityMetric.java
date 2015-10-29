package com.glue.feed.sim;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

import com.glue.domain.Event;

/**
 * A simple similarity metric for events based on the Jaro measure between their
 * single titles.
 * 
 * <p>
 * The use of this similarity metric should be the final step of the
 * reconciliation process, and the given events should be pre-filtered based on
 * their dates and their venue.
 * </p>
 * 
 * @author pgillet
 * 
 */
public class EventSimilarityMetric implements SimilarityMetric<Event> {

    @Override
    public float getSimilarity(Event e1, Event e2) {

	final float nameWeight = 1.0f;
	final int minSize = 5;

	// Check for wrong weight distribution
	assert (nameWeight == 1.0f);

	AbstractStringMetric stringMetric = new JaroWinkler();

	// Title1 included in Title2 o Title2 included in Title1
	String title1 = e1.getTitle().toUpperCase();
	String title2 = e2.getTitle().toUpperCase();
	float nameSim = 0.0f;
	if ((title2.length() > minSize && title1.contains(title2))
		|| (title1.length() > minSize && title2.contains(title1))) {
	    nameSim = 1.0f;
	} else {
	    nameSim = stringMetric.getSimilarity(title1, title2);
	}

	return nameWeight * nameSim;
    }
}
