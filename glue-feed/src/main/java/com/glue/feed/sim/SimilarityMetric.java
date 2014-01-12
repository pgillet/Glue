package com.glue.feed.sim;

/**
 * 
 * @author pgillet
 * 
 * @param <T>
 * @see Similarable
 */
public interface SimilarityMetric<T> {

	/**
	 * Computes the similarity measure for the two arguments. Returns a float
	 * value between 0 and 1 inclusive as the first argument is not similar or
	 * similar to the second.
	 * 
	 * <p>
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>x.similarTo(y) ==
	 * y.similarTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies
	 * that <tt>x.similarTo(y)</tt> must throw an exception iff
	 * <tt>y.similarTo(x)</tt> throws an exception.)
	 * <p>
	 * 
	 * @param o1
	 *            the first object to be compared.
	 * @param o2
	 *            the second object to be compared.
	 * @return a value 0-1 of similarity. 1 = similar, 0 = not similar.
	 */
	float getSimilarity(T o1, T o2);

}
