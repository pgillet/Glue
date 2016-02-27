package com.glue.chain;

/**
 * <p>
 * A {@link Contract} is a specialized {@link Command} that also expects the
 * {@link Chain} that is executing it to call the <code>require()</code> method
 * before the <code>execute()</code> method (precondition), and the
 * <code>ensure()</code> method after (postcondition).
 * </p>
 * 
 * <p>
 * This approach ensures that a <code>Command</code> invoked in a
 * <code>Chain</code> will meet the preconditions specified as required for that
 * operation, and guarantees certain properties on exit.
 * </p>
 */
public interface Contract extends Command {

    /**
     * Precondition: The condition to be verified by the chain prior to the
     * launch of the given treatment in the <code>execute()</code> method. This
     * condition must ensure that the execution of the treatment is possible
     * without error.
     * 
     * @param context
     *            The {@link Context} to be processed by this {@link Contract}
     * @throws Exception
     *             if the precondition did not held true.
     */
    void require(Context context) throws Exception;

    /**
     * Postcondition: The condition that must be guaranteed by this
     * <code>Contract</code> after the course of the given treatment in the
     * <code>execute()</code> method. This condition must ensure that the
     * processing has completed its work.
     * 
     * @param context
     *            The {@link Context} to be processed by this {@link Contract}
     * @throws Exception
     *             if the postcondition did not held true.
     */
    void ensure(Context context) throws Exception;

}
