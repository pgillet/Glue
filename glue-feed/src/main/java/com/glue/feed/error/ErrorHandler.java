package com.glue.feed.error;

/**
 * Defines the requirements for an object that can be used to send error events
 * to an arbitrary destination.
 *
 * @author pgillet
 */
public interface ErrorHandler {

    /**
     * Sends an error event with the specified level.
     *
     * @param code an error code
     * @param params an array of objects to be formatted and substituted in
     *        the error message
     * @param lvl the offset from the beginning of the file, in bytes, at
     *        which the  error event occurred. A negative number if undefined.
     * @param source The name of the source where the error occurred. May
     *        be<code>null</code> if none is available.
     * @param id the identifier of the source where the error occurred. May
     *        be<code>null</code> if none is available.
     * @param pos pos the offset from the beginning of the file, in bytes, at
     *        which the  error event occurred. A negative number if undefined.
     *
     * @see ErrorLevel
     */
    void fireErrorEvent(ErrorCode code, Object[] params, ErrorLevel lvl,
        String source, String id, long pos);
}
