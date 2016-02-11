package com.glue.chain;


import java.util.Map;

import org.apache.commons.chain.Chain;

/**
 * <p>
 * A {@link Context} represents the state information that is accessed and
 * manipulated by the execution of a {@link Command} or a {@link Chain}.
 * Specialized implementations of {@link Context} will typically add JavaBeans
 * properties that contain typesafe accessors to information that is relevant to
 * a particular use case for this context, and/or add operations that affect the
 * state information that is saved in the context.
 * </p>
 *
 * <p>
 * Implementations of {@link Context} must also implement all of the required
 * and optional contracts of the <code>java.util.Map</code> interface.
 * </p>
 *
 * <p>
 * It is strongly recommended, but not required, that JavaBeans properties added
 * to a particular {@link Context} implementation exhibit
 * <em>Attribute-Property Transparency</em>. In other words, a value stored via
 * a call to <code>setFoo(value)</code> should be visible by calling
 * <code>get("foo")</code>, and a value stored via a call to
 * <code>put("foo", value)</code> should be visible by calling
 * <code>getFoo()</code>. If your {@link Context} implementation class exhibits
 * this feature, it becomes easier to reuse the implementation in multiple
 * environments, without the need to cast to a particular implementation class
 * in order to access the property getter and setter methods.
 * </p>
 *
 * <p>
 * To protect applications from evolution of this interface, specialized
 * implementations of {@link Context} should generally be created by extending
 * the provided base class ({@link com.glue.feed.impl.ContextBase}) rather than
 * directly implementing this interface.
 * </p>
 *
 * <p>
 * Applications should <strong>NOT</strong> assume that {@link Context}
 * implementations, or the values stored in its attributes, may be accessed from
 * multiple threads simultaneously unless this is explicitly documented for a
 * particular implementation.
 * </p>
 */

public interface Context extends Map {

}
