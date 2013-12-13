package com.glue.feed.error;

/**
 * Defines the requirements for an object that delegates error events to a
 * specified error handler.
 *
 * @author pgillet
 */
public interface ErrorNode extends ErrorHandler {

    /**
     * Sets the specified error handler to be responsible for handling the
     * error events sent from this object.
     *
     * @param parent the parent<code>ErrorHandler</code>
     */
    void setParent(ErrorHandler parent);
}
