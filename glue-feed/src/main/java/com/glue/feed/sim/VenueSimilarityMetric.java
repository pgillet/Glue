package com.glue.feed.sim;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

import com.glue.domain.Venue;

public class VenueSimilarityMetric implements SimilarityMetric<Venue> {

	@Override
	public float getSimilarity(Venue v1, Venue v2) {

		float nameWeight = 0.45f;
		float cityWeight = 0.30f;
		float addressWeight = 0.05f;
		float latLongWeight = 0.20f;

		// Check for wrong weight distribution
		assert (nameWeight + cityWeight + addressWeight + latLongWeight == 1.0f);

		AbstractStringMetric stringMetric = new Levenshtein();

		float nameSim = stringMetric.getSimilarity(v1.getName(), v2.getName());
		float citySim = stringMetric.getSimilarity(v1.getCity(), v2.getCity());
		float addressSim = stringMetric.getSimilarity(v1.getAddress(),
				v2.getAddress());

		// TODO: for now, we consider that lat/long coordinates are not
		// discriminating
		float latLongSim = 1.0f;

		float overallSim = (nameWeight * nameSim) + (cityWeight * citySim)
				+ (addressWeight * addressSim) + (latLongWeight * latLongSim);

		return overallSim;
	}

	// Example
	public static void main(String[] args) {
		Venue v1 = new Venue();
		v1.setName("Bikini");
		v1.setCity("Ramonville-Saint-Agne");
		v1.setAddress("rue Théodore Monod, 31520 Ramonville-Saint-Agne");

		Venue v2 = new Venue();
		v2.setName("Le Bikini");
		v2.setCity("Ramonville St-Agne");
		v2.setAddress("rue Théodore Monod, 31520 Ramonville-St-Agne");

		Venue v3 = new Venue();
		v3.setName("Le Bikini");
		v3.setCity("Ramonville");
		v3.setAddress("Le Bikini, Rue Giotto, Ramonville, Ramonville-Saint-Agne, Toulouse, Haute-Garonne, Midi-Pyrénées, 31520, France métropolitaine");

		Venue v4 = new Venue();
		v4.setName("Le Bikini");
		v4.setCity("Toulouse");
		v4.setAddress("31000 Toulouse");

		Venue v5 = new Venue();
		v5.setName("La Dynamo");
		v5.setCity("Toulouse");
		v5.setAddress("Rue Amélie 31000 Toulouse");

		Venue[] venues = new Venue[] { v1, v2, v3, v4, v5 };

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
		return "Venue [name=" + venue.getName() + ", city=" + venue.getCity()
				+ ", address=" + venue.getAddress()
				/*
				 * + ", latitude=" + venue.getLatitude() + ", longitude=" +
				 * venue.getLongitude()
				 */+ "]";
	}
}
