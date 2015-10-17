package com.glue.persistence;

import java.util.List;

import com.glue.domain.City;

public class CityDAO extends AbstractDAO<City> {

    public CityDAO() {
	super();
    }

    public List<City> find(String query, int maxCount) {

	return em
		.createQuery(
			"SELECT c FROM City c WHERE c.name LIKE :query ORDER BY c.population DESC",
			type).setParameter("query", "%" + query + "%")
		.setMaxResults(maxCount).getResultList();
    }
}
