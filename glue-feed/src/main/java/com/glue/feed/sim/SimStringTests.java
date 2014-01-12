package com.glue.feed.sim;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.DiceSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;
import uk.ac.shef.wit.simmetrics.similaritymetrics.NeedlemanWunch;

public class SimStringTests {

	// The string metrics to be tested
	private static AbstractStringMetric[] metrics = new AbstractStringMetric[] {
			new Levenshtein(), new NeedlemanWunch(), new CosineSimilarity(),
			new EuclideanDistance(), new MongeElkan(), new JaccardSimilarity(),
			new DiceSimilarity() };

	// The strings to be tested
	private static String[] strings = new String[] { "Michael Joseph Jackson",
			"Michael J. Jackson", "Michael Jackson", "Michel Jackson",
			"Michael Jackson, King of Pop", "Mickael Jackson",
			"Jackson, Michael", };

	public static void main(final String[] args) {

		for (String str1 : strings) {
			for (String str2 : strings) {
				System.out.println("Similarity between \"" + str1 + "\" and \""
						+ str2 + "\"");

				for (AbstractStringMetric metric : metrics) {
					float result = metric.getSimilarity(str1, str2);
					outputResult(result, metric, str1, str2);
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	/**
	 * outputs the result of the metric test.
	 * 
	 * @param result
	 *            the float result of the metric test
	 * @param metric
	 *            the metric itself to provide its description in the output
	 * @param str1
	 *            the first string with which to compare
	 * @param str2
	 *            the second string to compare with the first
	 */
	private static void outputResult(final float result,
			final AbstractStringMetric metric, final String str1,
			final String str2) {
		// System.out.println("Using Metric " +
		// metric.getShortDescriptionString()
		// + " on strings \"" + str1 + "\" & \"" + str2
		// + "\" gives a similarity score of " + result);

		System.out.println((int) (result * 100) + "% ["
				+ metric.getShortDescriptionString() + "]");

	}

}
