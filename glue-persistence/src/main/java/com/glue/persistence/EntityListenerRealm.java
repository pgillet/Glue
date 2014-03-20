package com.glue.persistence;

import java.io.Flushable;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * An object that keeps track of all entity listeners. Entity listeners are
 * responsible for registering themselves by calling {@link #add(Object)}.
 * 
 * @author pgillet
 * 
 */
public class EntityListenerRealm {

    private static Set<Object> listeners = Collections
	    .newSetFromMap(new WeakHashMap<Object, Boolean>());

    /**
     * Returns a collection of all registered entity listeners.
     */
    protected static Set<Object> getListeners() {
	return listeners;
    }

    /**
     * Registers a new entity listener.
     * 
     * @param listener
     */
    public static void add(Object listener) {
	listeners.add(listener);
    }

    /**
     * Flushes all registered listeners if they are flushable.
     * 
     * @throws IOException
     *             If an I/O error occurs
     */
    protected static void flush() throws IOException {
	for (Object listener : listeners) {
	    if (listener instanceof Flushable) {
		((Flushable) listener).flush();
	    }
	}
    }

}
