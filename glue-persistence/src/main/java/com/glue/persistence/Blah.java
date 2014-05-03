package com.glue.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Blah<T> {

    EntityManager em;
    CriteriaBuilder cb;
    Root<T> root;
    Predicate whereClause;
    Class<T> domainClass;
    CriteriaQuery<T> cq;

    // ... Methods to create where clause ...

    public Blah(EntityManager em, Class<T> domainClass) {
        this.em = em;
        this.domainClass = domainClass;
	cb = this.em.getCriteriaBuilder();
	whereClause = cb.equal(cb.literal(1), 1); // Dummy
	cq = cb.createQuery(domainClass);
	root = cq.from(domainClass);
    }

    public CriteriaQuery<T> getQuery() {
	cq.select(root);
	cq.where(whereClause);
	return cq;
    }

    public CriteriaQuery<Long> getQueryForCount() {
	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	cq.select(cb.count(root));
	cq.where(whereClause);
	return cq;
    }

    public List<T> list() {
	TypedQuery<T> q = this.em.createQuery(this.getQuery());
        return q.getResultList();
    }

    public Long count() {
        TypedQuery<Long> q = this.em.createQuery(this.getQueryForCount());
        return q.getSingleResult();
    }
}
