package com.glue.feed.chain;

import org.apache.commons.chain.Chain;

/**
 * <p>A {@link Command} encapsulates a unit of processing work to be
 * performed, whose purpose is to examine and/or modify the state of a
 * transaction that is represented by a {@link Context}.  Individual
 * {@link Command}s can be assembled into a {@link Chain}, which allows
 * them to either complete the required processing or delegate further
 * processing to the next {@link Command} in the {@link Chain}.</p>
 *
 * <p>{@link Command} implementations should be designed in a thread-safe
 * manner, suitable for inclusion in multiple {@link Chain}s that might be
 * processed by different threads simultaneously.  In general, this implies
 * that {@link Command} classes should not maintain state information in
 * instance variables.  Instead, state information should be maintained via
 * suitable modifications to the attributes of the {@link Context} that is
 * passed to the <code>execute()</code> command.</p>
 *
 * <p>{@link Command} implementations typically retrieve and store state
 * information in the {@link Context} instance that is passed as a parameter
 * to the <code>execute()</code> method, using particular keys into the
 * <code>Map</code> that can be acquired via
 * <code>Context.getAttributes()</code>.  To improve interoperability of
 * {@link Command} implementations, a useful design pattern is to expose the
 * key values used as JavaBeans properties of the {@link Command}
 * implementation class itself.  For example, a {@link Command} that requires
 * an input and an output key might implement the following properties:</p>
 *
 * <pre>
 *   private String inputKey = "input";
 *   public String getInputKey() {
 *     return (this.inputKey);
 *   }
 *   public void setInputKey(String inputKey) {
 *     this.inputKey = inputKey;
 *   }
 *
 *   private String outputKey = "output";
 *   public String getOutputKey() {
 *     return (this.outputKey);
 *   }
 *   public void setOutputKey(String outputKey) {
 *     this.outputKey = outputKey;
 *   }
 * </pre>
 *
 * <p>And the operation of accessing the "input" information in the context
 * would be executed by calling:</p>
 *
 * <pre>
 *   String input = (String) context.get(getInputKey());
 * </pre>
 *
 * <p>instead of hard coding the attribute name.  The use of the "Key"
 * suffix on such property names is a useful convention to identify properties
 * being used in this fashion, as opposed to JavaBeans properties that simply
 * configure the internal operation of this {@link Command}.</p>
 *
 */

public interface Command {

    /**
     * <p>
     * Execute a unit of processing work to be performed. This {@link Command}
     * completes its own processing, then delegates remaining processing to the
     * next {@link Command} in a {@link Chain} containing this {@link Command}.
     *
     * @param context
     *            The {@link Context} to be processed by this {@link Command}
     *
     * @exception Exception
     *                general purpose exception return to indicate abnormal
     *                termination
     * @exception IllegalArgumentException
     *                if <code>context</code> is <code>null</code>
     */
    void execute(Context context) throws Exception;


}

