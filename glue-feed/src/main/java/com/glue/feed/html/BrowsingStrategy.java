package com.glue.feed.html;


public interface BrowsingStrategy {

    void browse() throws Exception;

    void setExtractor(Extractor extractor);

}
