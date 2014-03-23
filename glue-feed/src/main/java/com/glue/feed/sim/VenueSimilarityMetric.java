package com.glue.feed.sim;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;

import com.glue.domain.Venue;

/**
 * A simple similarity metric for venues based on the Jaro measure between their
 * single names.
 * 
 * <p>
 * The use of this similarity metric should be the final step of the
 * reconciliation process, and the given venues should be pre-filtered based on
 * their geographical coordinates.
 * </p>
 * 
 * @author pgillet
 * 
 */
public class VenueSimilarityMetric implements SimilarityMetric<Venue> {

    @Override
    public float getSimilarity(Venue v1, Venue v2) {

	final float nameWeight = 1.0f;

	// Check for wrong weight distribution
	assert (nameWeight == 1.0f);

	AbstractStringMetric stringMetric = new Jaro();

	float nameSim = stringMetric.getSimilarity(v1.getName().toUpperCase(),
		v2.getName().toUpperCase());

	float overallSim = nameWeight * nameSim;

	return overallSim;
    }

    // Example
    public static void main(String[] args) {
	Venue v1 = new Venue();
	v1.setName("THEATRE LE RING");

	Venue v2 = new Venue();
	v2.setName("SALLE NOUGARO");

	Venue v3 = new Venue();
	v3.setName("RUE HOCHE");

	Venue v4 = new Venue();
	v4.setName("PETIT THEATRE SAINT EXUPERE ");

	Venue v5 = new Venue();
	v5.setName("JARDIN NOUGARO");

	Venue v6 = new Venue();
	v6.setName("CINEMA REX");

	Venue[] venues = new Venue[] { v1, v2, v3, v4, v5, v6 };

	for (int i = 0; i < venues.length; i++) {
	    System.out.println("v" + i + " = " + getDescription(venues[i]));
	}

	SimilarityMetric<Venue> metric = new VenueSimilarityMetric();

	for (int i = 0; i < venues.length; i++) {
	    for (int j = 0; j < venues.length; j++) {
		float sim = metric.getSimilarity(venues[i], venues[j]);
		System.out.println("Similarity between v" + i + " and v" + j
			+ " = " + (int) (sim * 100) + "%");
	    }
	    System.out.println();
	}

    }

    public static String getDescription(Venue venue) {
	return "Venue [name=" + venue.getName() + "]";
    }
}
