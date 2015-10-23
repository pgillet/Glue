package com.glue.feed.dvr;

import java.util.List;

public interface EventIndexService {

    /**
     * Returns a list of event Ids that are withdrawn.
     * 
     * @param limit
     * @return
     */
    List<String> getWithdrawnEventIds();

    /**
     * Deletes from the index a list of documents by unique ID.
     * 
     * @param ids
     *            the list of document IDs to delete
     * @throws Exception
     *             If there is a low-level I/O error.
     */
    void deleteById(List<String> ids) throws Exception;

}
