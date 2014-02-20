package com.glue.feed.html;

public interface HTMLMappingStrategy<T> {

    T parse(String url) throws Exception;

}
