package com.glue.feed.error;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * The <code>ErrorEvent</code> class is used to provide contextual information
 * about an error event to the handler processing the event.
 *
 * @author pgillet
 */
public class ErrorEvent {

    /** The suffix for error message keys. Defined as {@value}. */
    private static final String MSG_SUFFIX = ".MSG";

    /** The suffix for standard reference keys. Defined as {@value}. */
    private static final String REF_SUFFIX = ".REF";

    /** the base name of the resource bundle. */
    private static final String BASE_NAME = "errorMessages";

    /** Preference key for this package. */
    public static final String LOCALE = "fr";

    /** The locale for error messages. */
    private Locale _locale;

    /** The resource bundle. */
    private ResourceBundle resourceBundle;

    /** The source locator. */
    private SourceLocator locator = null;

    /** The error code. */
    private String code;

    /** The detail message. */
    private String msg;

    /** The standard reference. */
    private String stdRef;

    /**
     * Creates a new ErrorEvent with the specified error code.
     *
     * @param code an error code
     * @param params an array of objects to be formatted and substituted in
     *        the error message associated with the passed error code. May be
     *        <code>null</code>
     */
    public ErrorEvent(ErrorCode code, Object[] params) {
        setPreferences();

        // Error code
        this.code = code.getLabel();

        // Detail message
        String pattern = resourceBundle.getString(code + MSG_SUFFIX);
        if (params != null) {
            pattern = MessageFormat.format(pattern, params);
        }
        msg = pattern;

        // Standard reference
        try {
            stdRef = resourceBundle.getString(code + REF_SUFFIX);
        } catch (MissingResourceException ex) {

            // Ignore as the standard reference is not mandatory !
        }
    }

    /**
     * Creates a new ErrorEvent with the specified message, locator and
     * standard reference.
     *
     * @param code an error code
     * @param params an array of objects to be formatted and substituted in
     *        the error message associated with the passed error code. May be
     *        <code>null</code>
     * @param locator The locator object for the error or warning. May be
     *        <code>null</code>.
     */
    public ErrorEvent(ErrorCode code, Object[] params,
        SourceLocator locator) {
        this(code, params);
        setLocator(locator);
    }

    /**
     * Returns the error code.
     *
     * @return the error code.
     */
    public String getErrorCode() {
        return code;
    }

    /**
     * Returns the detail message.
     *
     * @return the detail message.
     */
    public String getErrorMessage() {
        return this.msg;
    }

    /**
     * Sets the locale for error messages.
     *
     * @param locale the locale for error messages.
     */
    public void setLocale(Locale locale) {
        this._locale = locale;

        // Update the resource bundle
        resourceBundle = ResourceBundle.getBundle(BASE_NAME, locale);
    }

    /**
     * Returns the locale for error messages.
     *
     * @return the locale for error messages.
     */
    public Locale getLocale() {
        return this._locale;
    }

    /**
     * Sets an instance of a SourceLocator object that specifies where an error
     * occured.
     *
     * @param locator A SourceLocator object, or<code>null</code> to clear
     *        the location
     */
    public void setLocator(SourceLocator locator) {
        this.locator = locator;
    }

    /**
     * Retrieves an instance of a SourceLocator object that specifies where an
     * error occured.
     *
     * @return A SourceLocator object, or <code>null</code> if none was
     *         specified.
     */
    public SourceLocator getLocator() {
        return locator;
    }

    /**
     * Returns the standard reference.
     *
     * @return the standard reference, or <code>null</code> if none.
     */
    public String getStandardReference() {
        return stdRef;
    }

    /**
     * Returns a string representation of this error event.
     *
     * @return a string representation of this error event.
     */
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer = buffer.append("Error: ");
        buffer = buffer.append(code);
        if (getLocator() != null) {
            buffer = buffer.append("\r\n");
            buffer = buffer.append("Location: ");
            buffer = buffer.append(getLocator());
        }
        buffer = buffer.append("\r\n");
        buffer = buffer.append("Cause: ");
        buffer = buffer.append(getErrorMessage());

        String stdref = getStandardReference();
        if (stdref != null) {
            buffer = buffer.append("\r\n");
            buffer = buffer.append("Standard Reference: ");
            buffer = buffer.append(getStandardReference());
        }
        return buffer.toString();
    }

    /**
     * Sets the user preferences.
     */
    private void setPreferences() {

        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String value = prefs.get(LOCALE, "en,US");
        int index = value.indexOf(",");
        String language = value.substring(0, index).trim();
        String country = value.substring(index + 1).trim();
        setLocale(new Locale(language, country));
        this.resourceBundle = ResourceBundle.getBundle(BASE_NAME, getLocale());
    }
}
