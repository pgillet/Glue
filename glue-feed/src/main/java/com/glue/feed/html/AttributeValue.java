package com.glue.feed.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retrieves the value from an attribute in the element. The name of the
 * attribute can be specified in the name parameter.
 * 
 * @see Selector
 * 
 * @author pgillet
 * 
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AttributeValue {

    /**
     * The name of the attribute.
     * 
     * @return
     */
    String name();
}
