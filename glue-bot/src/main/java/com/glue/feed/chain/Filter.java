package com.glue.feed.chain;


/**
 * <p>
 * A {@link Filter} is a specialized {@link Command} that also expects the
 * {@link Chain} that is executing it to call the <code>postprocess()</code>
 * method if it called the <code>execute()</code> method. This promise must be
 * fulfilled in spite of any possible exceptions thrown by the
 * <code>execute()</code> method of this {@link Command}, or any subsequent
 * {@link Command} whose <code>execute()</code> method was called. The owning
 * {@link Chain} must call the <code>postprocess()</code> method of each
 * {@link Filter} in a {@link Chain} in reverse order of the invocation of their
 * <code>execute()</code> methods.
 * </p>
 *
 * <p>
 * The most common use case for a {@link Filter}, as opposed to a
 * {@link Command}, is where potentially expensive resources must be acquired
 * and held until the processing of a particular request has been completed,
 * even if execution is delegated to a subsequent {@link Command} via the
 * <code>execute()</code> returning <code>false</code>. A {@link Filter} can
 * reliably release such resources in the <code>postprocess()</code> method,
 * which is guaranteed to be called by the owning {@link Chain}.
 * </p>
 */

public interface Filter extends Command {

    /**
     * <p>
     * Execute any cleanup activities, such as releasing resources that were
     * acquired during the <code>execute()</code> method of this {@link Filter}
     * instance.
     * </p>
     *
     * @param context
     *            The {@link Context} to be processed by this {@link Filter}
     * @param exception
     *            The <code>Exception</code> (if any) that was thrown by the
     *            last {@link Command} that was executed; otherwise
     *            <code>null</code>
     *
     * @exception IllegalArgumentException
     *                if <code>context</code> is <code>null</code>
     *
     * @return If a non-null <code>exception</code> was "handled" by this method
     *         (and therefore need not be rethrown), return <code>true</code>;
     *         otherwise return <code>false</code>
     */
    boolean postprocess(Context context, Exception exception);

}

