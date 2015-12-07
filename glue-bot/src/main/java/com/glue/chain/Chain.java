package com.glue.chain;

/**
 * <p>
 * A {@link Chain} represents a configured list of {@link Command}s that will be
 * executed in order to perform processing on a specified {@link Context}. Each
 * included {@link Command} will be executed in turn, unless one of the executed
 * {@link Command}s throws an exception before the end of the chain has been
 * reached. The {@link Chain} itself may rethrow the thrown exception.
 * </p>
 *
 * <p>
 * Note that {@link Chain} extends {@link Command}, so that the two can be used
 * interchangeably when a {@link Command} is expected. This makes it easy to
 * assemble workflows in a hierarchical manner by combining subchains into an
 * overall processing chain.
 * </p>
 *
 * <p>
 * To protect applications from evolution of this interface, specialized
 * implementations of {@link Chain} should generally be created by extending the
 * provided base class {@link com.glue.feed.impl.ChainBase}) rather than
 * directly implementing this interface.
 * </p>
 *
 * <p>
 * {@link Chain} implementations should be designed in a thread-safe manner,
 * suitable for execution on multiple threads simultaneously. In general, this
 * implies that the state information identifying which {@link Command} is
 * currently being executed should be maintained in a local variable inside the
 * <code>execute()</code> method, rather than in an instance variable. The
 * {@link Command}s in a {@link Chain} may be configured (via calls to
 * <code>addCommand()</code>) at any time before the <code>execute()</code>
 * method of the {@link Chain} is first called. After that, the configuration of
 * the {@link Chain} is frozen.
 * </p>
 */

public interface Chain extends Command {

    /**
     * <p>
     * Add a {@link Command} to the list of {@link Command}s that will be called
     * in turn when this {@link Chain}'s <code>execute()</code> method is
     * called. Once <code>execute()</code> has been called at least once, it is
     * no longer possible to add additional {@link Command}s; instead, an
     * exception will be thrown.
     * </p>
     *
     * @param command
     *            The {@link Command} to be added
     *
     * @exception IllegalArgumentException
     *                if <code>command</code> is <code>null</code>
     * @exception IllegalStateException
     *                if this {@link Chain} has already been executed at least
     *                once, so no further configuration is allowed
     */
    void addCommand(Command command);

    /**
     * <p>
     * Execute the processing represented by this {@link Chain} according to the
     * following algorithm.
     * </p>
     * <ul>
     * <li>If there are no configured {@link Command}s in the {@link Chain}, do
     * nothing.</li>
     * <li>Call the <code>execute()</code> method of each {@link Command}
     * configured on this chain, in the order they were added via calls to the
     * <code>addCommand()</code> method, until the end of the configured
     * {@link Command}s is encountered, or until one of the executed throws an
     * exception.</li>
     * <li>Walk backwards through the {@link Command}s whose
     * <code>execute()</code> methods, starting with the last one that was
     * executed. If this {@link Command} instance is also a {@link Filter}, call
     * its <code>postprocess()</code> method, discarding any exception that is
     * thrown.</li>
     * <li>If the last {@link Command} whose <code>execute()</code> method was
     * called threw an exception, rethrow that exception.</li>
     * </ul>
     *
     * @param context
     *            The {@link Context} to be processed by this {@link Chain}
     *
     * @exception Exception
     *                if thrown by one of the {@link Command}s in this
     *                {@link Chain} but not handled by a
     *                <code>postprocess()</code> method of a {@link Filter}
     * @exception IllegalArgumentException
     *                if <code>context</code> is <code>null</code>
     */
    void execute(Context context) throws Exception;

}