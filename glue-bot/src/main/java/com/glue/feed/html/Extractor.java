package com.glue.feed.html;

import org.jsoup.nodes.Element;

public interface Extractor {
    
    void process(Element e) throws Exception;

}
