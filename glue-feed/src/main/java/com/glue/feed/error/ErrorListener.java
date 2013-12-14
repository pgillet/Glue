package com.glue.feed.error;

import java.io.Flushable;
import java.util.EventListener;

/**
 * To provide customized error handling, implement this interface and use the
 * <code>addErrorListener</code> method to register an instance of the
 * implementation on the appropriate error source. The said source then
 * reports all errors and warnings through this interface.
 * 
 * <p>
 * For parsing errors, objects representing ARINC files must use this interface
 * instead of throwing an <code>Exception</code>: it is up to the application
 * to decide whether to throw an <code>Exception</code> for different types of
 * errors and warnings.
 * </p>
 *
 * @author pgillet
 */
public interface ErrorListener extends EventListener, Flushable {

    /**
     * Invoked when an error (recoverable or not) has been fired.
     *
     * @param e The error information encapsulated in an ErrorEvent object
     */
    void error(ErrorEvent e);

    /**
     * Invoked when an informational message has been fired.
     *
     * @param e The error information encapsulated in an ErrorEvent object
     */
    void info(ErrorEvent e);

    /**
     * Invoked when an warning notification has been fired.
     *
     * @param e The error information encapsulated in an ErrorEvent object
     */
    void warning(ErrorEvent e);
}
