package com.glue.feed.sim;

/**
 * 
 * @author pgillet
 *
 * @param <T>
 * @see SimilarityMetric
 */
public interface Similarable<T> {

	/**
	 * Computes the similarity measure for this object with the specified
	 * object. Returns a float value between 0 and 1 inclusive as this object is
	 * not similar or similar to the specified object.
	 * 
	 * <p>
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>x.similarTo(y) ==
	 * y.similarTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies
	 * that <tt>x.similarTo(y)</tt> must throw an exception iff
	 * <tt>y.similarTo(x)</tt> throws an exception.)
	 * <p>
	 * 
	 * @param o
	 *            the object to be compared.
	 * @return a value 0-1 of similarity. 1 = similar, 0 = not similar.
	 * @throws NullPointerException
	 *             if the specified object is null
	 * @throws ClassCastException
	 *             if the specified object's type prevents it from being
	 *             compared to this object.
	 */
	float similarTo(T o);

}
