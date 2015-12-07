package com.glue.bot;

import org.jsoup.nodes.Element;

public interface Extractor {
    
    void process(Element e) throws Exception;

}
