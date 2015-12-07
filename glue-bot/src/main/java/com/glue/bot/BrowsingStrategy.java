package com.glue.bot;


public interface BrowsingStrategy {

    void browse() throws Exception;

    void setExtractor(Extractor extractor);

}
