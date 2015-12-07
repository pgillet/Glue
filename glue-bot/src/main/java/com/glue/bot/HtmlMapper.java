package com.glue.bot;

import org.jsoup.nodes.Element;

public interface HtmlMapper<T> {

    T parse(Element e) throws Exception;

}
