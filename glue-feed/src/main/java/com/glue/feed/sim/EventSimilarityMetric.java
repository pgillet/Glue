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

	// Check for wrong weight distribution
	assert (nameWeight == 1.0f);

	AbstractStringMetric stringMetric = new JaroWinkler();

	float nameSim = stringMetric.getSimilarity(e1.getTitle().toUpperCase(),
		e2.getTitle().toUpperCase());

	float overallSim = nameWeight * nameSim;

	return overallSim;
    }

}
