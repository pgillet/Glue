package com.glue.feed.sim;

import java.util.Collection;

public class MetricHandler<T> {

    private SimilarityMetric<T> sm;
    private float threshold;

    /**
     * Constructs a new metric handler.
     * 
     * @param obj
     *            The object to be compared
     * @param sm
     *            The metric to be used for comparison
     * @param threshold
     */
    public MetricHandler(SimilarityMetric<T> sm, float threshold) {
	this.sm = sm;
	this.threshold = threshold;
    }

    /**
     * Does a batch comparison of one object against a given set of objects and
     * returns the first one whose the score is greater or equal to this
     * handler's threshold, or null otherwise.
     * 
     * @param candidates
     *            a collection of objects to be compared.
     * @return The first object in the given collection with a score greater or
     *         equal to this handler's threshold, or null otherwise.
     */
    public T getFirstMatchOver(T obj, Collection<T> candidates) {

	for (T candidate : candidates) {
	    float m = sm.getSimilarity(obj, candidate);
	    if (m >= threshold) {
		return candidate;
	    }
	}

	return null;
    }

    /**
     * Does a batch comparison of one object against a given set of objects and
     * returns the one with the highest score.
     * 
     * @param candidates
     *            a collection of objects to be compared.
     * @return The object from the given collection with the highest score.
     */
    public T getBestMatch(T obj, Collection<T> candidates) {
	return getBestMatchOver(obj, candidates, 0.0f);
    }

    /**
     * Does a batch comparison of one object against a given set of objects and
     * returns the one with the highest score and greater or equal to this
     * handler's threshold, or null otherwise.
     * 
     * @param candidates
     *            a collection of objects to be compared.
     * @return The object from the given collection with the highest score that
     *         is also greater or equal to this handler's threshold.
     */
    public T getBestMatchOver(T obj, Collection<T> candidates) {
	return getBestMatchOver(obj, candidates, this.threshold);
    }

    /**
     * Does a batch comparison of one object against a given set of objects and
     * returns the one with the highest score and greater or equal to the
     * threshold argument, or null otherwise.
     * 
     * @param candidates
     *            a collection of objects to be compared.
     * @return The object from the given collection with the highest score that
     *         is also greater or equal to the given threshold.
     */
    protected T getBestMatchOver(T obj, Collection<T> candidates,
	    float threshold) {

	T best = null;
	float max = threshold;

	for (T candidate : candidates) {
	    float m = sm.getSimilarity(obj, candidate);
	    if (m >= max) {
		max = m;
		best = candidate;
	    }
	}

	return best;
    }

}
