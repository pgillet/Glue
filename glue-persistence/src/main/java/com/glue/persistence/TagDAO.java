package com.glue.persistence;

import javax.persistence.Query;

import com.glue.domain.Tag;

public class TagDAO extends AbstractDAO<Tag> {

    public TagDAO() {
	super();
    }

    public Tag findDuplicate(Tag tag) {

	final String queryString = "SELECT t FROM Tag t WHERE t.title = :title";

	Query query = em.createQuery(queryString, type).setParameter("title",
		tag.getTitle());

	return PersistenceHelper.getSingleResultOrNull(query);

    }

}
