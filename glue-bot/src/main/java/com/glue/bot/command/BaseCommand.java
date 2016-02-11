package com.glue.bot.command;

import com.glue.bot.SelectorKeys;


public class BaseCommand {

    private String elementKey = SelectorKeys.ELEMENT_KEY;
    private String eventKey = SelectorKeys.EVENT_KEY;
    private String venueKey = SelectorKeys.VENUE_KEY;

    /**
     * <p>
     * Return the context attribute key for the element attribute.
     * </p>
     * 
     * @return The element attribute key.
     */
    public String getElementKey() {
        return elementKey;
    }

    /**
     * <p>
     * Set the context attribute key for the element attribute.
     * </p>
     *
     * @param fromKey
     *            The new key
     */
    public void setElementKey(String elementKey) {
        this.elementKey = elementKey;
    }

    /**
     * <p>
     * Return the context attribute key for the event attribute.
     * </p>
     * 
     * @return The element attribute key.
     */
    public String getEventKey() {
        return eventKey;
    }

    /**
     * <p>
     * Set the context attribute key for the event attribute.
     * </p>
     *
     * @param fromKey
     *            The new key
     */
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    /**
     * <p>
     * Return the context attribute key for the venue attribute.
     * </p>
     * 
     * @return The element attribute key.
     */
    public String getVenueKey() {
        return venueKey;
    }

    /**
     * <p>
     * Set the context attribute key for the venue attribute.
     * </p>
     *
     * @param fromKey
     *            The new key
     */
    public void setVenueKey(String venueKey) {
        this.venueKey = venueKey;
    }

}