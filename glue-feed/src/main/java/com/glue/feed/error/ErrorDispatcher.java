package com.glue.feed.error;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that holds a list of error event listeners. A single instance can be
 * used to hold all error listeners for the instance using the list. This
 * class provides methods which dispatch event notification methods to the
 * error listeners in the list according to the passed error level.
 * 
 * <p>
 * A class that has interest for handling error listeners should implement the
 * <code>ErrorHandler</code> interface on top of an underlying error
 * dispatcher:
 * <pre>
 *  Class Sample implements ErrorHandler {
 *  private ErrorDispatcher dispatcher = new ErrorDispatcher();
 *  public ErrorListener[] getErrorListeners() {
 *  return dispatcher.getErrorListeners();
 *  }
 *  public void addErrorListener(ErrorListener l) {
 *  dispatcher.addErrorListener(l);
 *  }
 *  public void removeErrorListener(ErrorListener l) {
 *  dispatcher.removeErrorListener(l);
 *  }
 *  public void foo(){
 *  ...
 *  dispatcher.fireErrorEvent("error in foo method", Level.ERROR);
 *  ...
 *  }
 *  }
 *  </pre>
 * </p>
 *
 * @author pgillet
 */
public class ErrorDispatcher implements ErrorHandler, ErrorManager {

    /** The list of error event listeners. */
    private List listeners;

    /**
     * Tells whether or not the list of error listeners is in its initial
     * state.
     */
    private boolean _removeDefault = true;

    /**
     * Creates a new instance of ErrorDispatcher.
     */
    public ErrorDispatcher() {
        listeners = new LinkedList();
        listeners.add(new DefaultErrorListener());
    }

    /**
     * {@inheritDoc}
     */
    public ErrorListener[] getErrorListeners() {
        return (ErrorListener[]) listeners.toArray(new ErrorListener[listeners
                .size()]);
    }

    /**
     * {@inheritDoc}
     */
    public void addErrorListener(ErrorListener l) {
        if (listeners.add(l) && _removeDefault) {
            listeners.remove(0);
            _removeDefault = false;
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     *
     * @param code an error code
     * @param params an array of objects to be formatted and substituted in
     *        the error message associated with the passed error code. May be
     *        <code>null</code>
     * @param lvl The error level
     * @param source The name of the source where the error occured. May
     *        be<code>null</code> if none is available.
     * @param id the identifier of the source where the error occured. May
     *        be<code>null</code> if none is available.
     * @param pos the offset from the beginning of the file, in bytes, at
     *        which the  error event occurred. A negative number if undefined.
     *
     * @see ErrorLevel
     */
    public void fireErrorEvent(ErrorCode code, Object[] params,
        ErrorLevel lvl, String source, String id, long pos) {
        if (lvl != ErrorLevel.OFF) {

            ErrorListener[] listeners = getErrorListeners();
            SourceLocator locator = null;
            if (source != null) {
                locator = new SourceLocator(source, id, pos);
            }

            ErrorEvent ex = new ErrorEvent(code, params, locator);

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 1; i >= 0; i--) {
                if (lvl == ErrorLevel.INFO) {
                    listeners[i].info(ex);
                } else if (lvl == ErrorLevel.WARN) {
                    listeners[i].warning(ex);
                } else if (lvl == ErrorLevel.ERROR) {
                    listeners[i].error(ex);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeErrorListener(ErrorListener l) {
        listeners.remove(l);
        if (listeners.isEmpty()) {
            _removeDefault = listeners.add(new DefaultErrorListener());
        }
    }
}
