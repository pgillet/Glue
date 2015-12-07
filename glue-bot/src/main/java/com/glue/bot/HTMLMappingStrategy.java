package com.glue.bot;

import org.jsoup.nodes.Element;

public interface HTMLMappingStrategy<T> {

    T parse(Element e) throws Exception;

}
