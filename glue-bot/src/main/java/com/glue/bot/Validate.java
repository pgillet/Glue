package com.glue.bot;

import java.util.Collection;

/**
 * Simple validation methods. Designed for error handling while scraping.
 * 
 * @author ubuntu
 *
 */
public final class Validate {

    private Validate() {
    }

    /**
     * Checks that the given collection is not empty.
     * 
     * @param elements
     * @throws SelectorParseException
     */
    public static void notEmpty(Collection<?> elements)
	    throws SelectorParseException {
	if (elements.isEmpty()) {
	    throw new SelectorParseException("No elements found");
	}
    }

    /**
     * Checks that the given collection has zero or one element.
     * 
     * @param elements
     * @throws SelectorParseException
     */
    public static void oneAtMost(Collection<?> elements)
	    throws SelectorParseException {
	if (elements.size() > 1) {
	    throw new SelectorParseException("More than one element found");
	}
    }

    /**
     * Checks that the given collection has exactly one element.
     * 
     * @param elements
     * @throws SelectorParseException
     */
    public static void single(Collection<?> elements)
	    throws SelectorParseException {
	notEmpty(elements);
	oneAtMost(elements);
    }

}
