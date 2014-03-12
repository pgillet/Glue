package com.glue.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    /**
     * If container-managed, the entity manager will be automatically injected.
     * If application-managed, the entity manager is set with
     * {@link #setEntityManager(EntityManager)}.
     */
    @PersistenceContext(unitName = GluePersistenceService.PERSISTENCE_UNIT)
    protected EntityManager em;

    protected Class<T> type;

    public AbstractDAO() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    protected EntityManager getEntityManager() {
	return em;
    }

    protected void setEntityManager(EntityManager em) {
	this.em = em;
    }

    @Override
    public long countAll(final Map<String, Object> params) {

        final StringBuffer queryString = new StringBuffer(
                "SELECT count(o) from ");

        queryString.append(type.getSimpleName()).append(" o ");
	// TODO
	// queryString.append(this.getQueryClauses(params, null));

        final Query query = this.em.createQuery(queryString.toString());

        return (Long) query.getSingleResult();
    }

    @Override
    public T create(final T t) {
        this.em.persist(t);
        return t;
    }

    @Override
    public void delete(final String id) {
        this.em.remove(this.em.getReference(type, id));
    }

    @Override
    public T find(final String id) {
        return (T) this.em.find(type, id);
    }

    @Override
    public T update(final T t) {
        return this.em.merge(t);    
    }
}