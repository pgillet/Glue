package com.glue.feed.html;

import org.jsoup.nodes.Element;

public interface HTMLMappingStrategy<T> {

    T parse(Element e) throws Exception;

}
