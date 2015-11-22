package com.glue.feed.merge;

public interface Merger<T> {
    T merge(T obj1, T obj2);
}
