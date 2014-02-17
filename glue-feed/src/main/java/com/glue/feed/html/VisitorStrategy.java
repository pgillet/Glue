package com.glue.feed.html;


public interface VisitorStrategy {

    void visit() throws Exception;

    void setVisitorListener(VisitorListener visitorListener);

}
