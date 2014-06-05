package com.glue.persistence;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.openjpa.event.TransactionListener;

/**
 * An object that keeps track of all transaction listeners. Transaction
 * listeners are responsible for registering themselves by calling
 * {@link #add(TransactionListener)}.
 * 
 * @author pgillet
 * 
 */
public class TransactionListenerRealm {

    private static Set<TransactionListener> listeners = Collections
	    .newSetFromMap(new WeakHashMap<TransactionListener, Boolean>());

    /**
     * Returns a collection of all registered entity listeners.
     */
    protected static Set<TransactionListener> getListeners() {
	return listeners;
    }

    /**
     * Registers a new entity listener.
     * 
     * @param listener
     */
    public static void add(TransactionListener listener) {
	listeners.add(listener);
    }

}
