package com.glue.feed.error;

import java.io.Serializable;

/**
 * The <code>ErrorLevel</code> class defines a set of error levels that can be
 * used to control error handling.
 * 
 * <p>
 * The levels are:
 * 
 * <ul>
 * <li>
 * INFO
 * </li>
 * <li>
 * WARN
 * </li>
 * <li>
 * ERROR
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * In addition there is a level OFF that can be used to turn off error
 * handling.
 * </p>
 *
 * @author pgillet
 */
public final class ErrorLevel implements Serializable {

    /** The default serial version. */
    private static final long serialVersionUID = 1L;

    /** OFF is a special level that can be used to turn off error handling. */
    public static final ErrorLevel OFF = new ErrorLevel("OFF", -1);

    /** INFO is a level for informational messages. */
    public static final ErrorLevel INFO = new ErrorLevel("INFO", 0);

    /** The WARN level designates potentially harmful situations. */
    public static final ErrorLevel WARN = new ErrorLevel("WARN", 1);

    /**
     * The ERROR level designates error events that either will lead the
     * application to abort, or might still allow the application to continue
     * running, according to the behavior implemented in the method
     * <code>error</code> of the error listener.
     */
    public static final ErrorLevel ERROR = new ErrorLevel("ERROR", 2);

    /** The name of the level. */
    private final String name;

    /** The integer value of the level. */
    private final int value;

    /**
     * Private constructor to prevent instantiation.
     *
     * @param name the name of the error level
     * @param value an integer value for the error level
     */
    private ErrorLevel(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Return the string name of the error level.
     *
     * @return the string name of the error level
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the integer value for this level. This integer value can be used for
     * efficient ordering comparisons between Level objects.
     *
     * @return the integer value for this level.
     */
    public int intValue() {
        return this.value;
    }

    /**
     * Parses a level name string into a known <code>ErrorLevel</code>,
     * ignoring case consideration.
     *
     * @param name string to be parsed
     *
     * @return parsed <code>ErrorLevel</code> value
     *
     * @throws NullPointerException if the given name string is
     *         <code>null</code>
     * @throws IllegalArgumentException if the value is not a known name
     */
    public static ErrorLevel parse(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        ErrorLevel lvl = null;
        if (OFF.getName().equalsIgnoreCase(name)) {
            lvl = OFF;
        } else if (INFO.getName().equalsIgnoreCase(name)) {
            lvl = INFO;
        } else if (WARN.getName().equalsIgnoreCase(name)) {
            lvl = WARN;
        } else if (ERROR.getName().equalsIgnoreCase(name)) {
            lvl = ERROR;
        } else {
            throw new IllegalArgumentException("Bad level \"" + name + "\"");
        }
        return lvl;
    }
}
