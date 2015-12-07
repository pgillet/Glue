package com.glue.bot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that stores the CSS selector for retrieving the HTML element whose
 * the value will be set using the annotated field or setter. The value
 * parameter should contain the CSS selector of the wanted HTML element.
 * 
 * <p>
 * This annotation can also be applied at the class level to designate a common
 * ancestor element. Otherwise, the whole HTML document is taken.
 * </p>
 * 
 * <p>
 * 
 * <code>@Selector</code> will need either of the following annotations to
 * determine how the value will be extracted from the selected element:
 * <ul>
 * <li>@TextValue – retrieve the text within the element (remove all HTML tags
 * within the element)</li>
 * <li>@HtmlValue – retrieve the HTML within the element</li>
 * <li>@AttributeValue – retrieve the value from an attribute in the element.
 * The name of the attribute can be specified in the name parameter.</li>
 * </ul>
 * 
 * If none is specified, @TextValue will be used by default.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> If this annotation is applied on a field, the field
 * must be public.
 * </p>
 * 
 * @see the query syntax documentation in <a
 *      href="http://jsoup.org/apidocs/org/jsoup/select/Selector.html"
 *      >Selector</a>.
 * @see TextValue
 * @see HtmlValue
 * @see AttributeValue
 * 
 * 
 * @author pgillet
 * 
 */

@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Selector {

    /**
     * A Selector CSS-like query.
     * 
     * @return a String.
     */
    String value();
}
