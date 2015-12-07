package com.glue.bot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retrieves the text within the element (remove all HTML tags within the
 * element).
 * 
 * @see Selector
 * 
 * @author pgillet
 * 
 */

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TextValue {

}
