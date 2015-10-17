package com.glue.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** TODO mettre des index sur ça? */ 
//		 KEY `ville_departement` (`ville_departement`),
//		 KEY `ville_nom` (`ville_nom`),
//		 KEY `ville_nom_reel` (`ville_nom_reel`),
//		 KEY `ville_code_postal` (`ville_code_postal`),
//		 KEY `ville_longitude_latitude_deg` (`ville_longitude_deg`,`ville_latitude_deg`),
//		 KEY `ville_nom_soundex` (`ville_nom_soundex`),
//		 KEY `ville_nom_metaphone` (`ville_nom_metaphone`),
//		 KEY `ville_population_2010` (`ville_population_2010`),
//		 KEY `ville_nom_simple` (`ville_nom_simple`)

@Entity
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	@Column(nullable = false, length = 3)
	private String departement;

	@Column(nullable = false, length = 45, unique = true)
	private String slug;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(name = "simple_name", nullable = false, length = 45)
	private String simpleName;

	@Column(name = "real_name", nullable = false, length = 45)
	private String realName;

	@Column(nullable = false, length = 20)
	private String soundex;

	@Column(nullable = false, length = 22)
	private String metaphone;

	@Column(name = "postal_code", nullable = false)
	private String postalCode;

	private int population;

	@Column(nullable = false)
	private double longitude;

	@Column(nullable = false)
	private double latitude;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartement() {
		return departement;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSoundex() {
		return soundex;
	}

	public void setSoundex(String soundex) {
		this.soundex = soundex;
	}

	public String getMetaphone() {
		return metaphone;
	}

	public void setMetaphone(String metaphone) {
		this.metaphone = metaphone;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

}
