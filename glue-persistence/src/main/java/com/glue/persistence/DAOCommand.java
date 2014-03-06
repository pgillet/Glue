package com.glue.persistence;


public interface DAOCommand<T> {

    public T execute(PersistenceService service) throws Exception;

}
