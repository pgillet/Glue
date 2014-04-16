package com.glue.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

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
    public long countAll() {

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	cq.select(cb.count(cq.from(type)));

	return em.createQuery(cq).getSingleResult();
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