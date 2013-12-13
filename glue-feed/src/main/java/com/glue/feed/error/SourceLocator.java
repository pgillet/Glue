package com.glue.feed.error;

/**
 * This interface is primarily for the purposes of reporting where an error
 * occurred in a binary source.
 *
 * @author pgillet
 */
public class SourceLocator {

    /** The source identifier. */
    private String id;

    /** The source name. */
    private String name;

    /**
     * the offset from the beginning of the file, in bytes, at which the error
     * event occurred.
     */
    private long position = -1L;

    /**
     * Creates a <code>sourceLocator</code> object immediately useful.
     *
     * @param name a string containing the source name. Should not
     *        be<code>null</code>
     * @param id A string identifier
     * @param position the offset from the beginning of the file, in bytes,
     *        at which the  error event occurred
     */
    public SourceLocator(String name, String id, long position) {
        this(name);
        this.setId(id);
        this.setPosition(position);
    }

    /**
     * Creates a new SourceLocator object with a single name.
     *
     * @param name a string containing the source name. Should not
     *        be<code>null</code>
     */
    public SourceLocator(String name) {
        this.setName(name);
    }

    /**
     * Sets the identifier of the current error event.
     *
     * @param id A string identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the current error event.
     *
     * @return A string containing the identifier, or <code>null</code> if none
     *         is available.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the name of this source.
     *
     * @param name a string containing the source name
     *
     * @throws NullPointerException if the given name is <code>null</code>
     * @throws IllegalArgumentException If the given name is an empty string.
     */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Empty string argument.");
        }
        this.name = name;
    }

    /**
     * Returns the name of this source.
     *
     * @return A string containing the source name, or <code>null</code> if
     *         none  is available.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the position where the error occurred.
     *
     * @param position the offset from the beginning of the file, in bytes,
     *        at which the error event occurred. A negative number has no
     *        effect.
     */
    public void setPosition(long position) {
        if (position > -1L) {
            this.position = position;
        }
    }

    /**
     * Returns the position where the error occurred. Returns <code>-1</code>
     * if the position has not been set.
     *
     * @return the offset from the beginning of the file, in bytes, at which
     *         the error event occurred, or <code>-1</code> if the position
     *         has not been set.
     */
    public long getPosition() {
        return this.position;
    }

    /**
     * Returns a string representation of this <code>SourceLocator</code>.
     *
     * @return a string representation of this <code>SourceLocator</code>.
     */
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer = buffer.append(name);
        if (id != null) {
            buffer = buffer.append(" (");
            buffer = buffer.append(id);
            buffer = buffer.append(")");
        }
        if (position > -1L) {
            buffer = buffer.append(": ");
            buffer = buffer.append(position);
        }
        return buffer.toString();
    }
}
