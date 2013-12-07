package com.glue.feed.toulouse.open.data.so;

/**
 * Description des champs :
 * 
 * <ul>
 * <li>Identifiant : identifiant unique de la manifestation (16 caractères)</li>
 * <li>Nom générique : intitulé général qui regroupe plusieurs manifestations
 * (nom du festival)</li>
 * <li>Nom de la manifestation : titre de la manifestation</li>
 * <li>Descriptif court : courte description de la manifestation (caractères
 * limités à 300)</li>
 * <li>Descriptif long : description détaillée de la manifestation (caractères
 * illimités)</li>
 * <li>Date début : date de début de la manifestation</li>
 * <li>Date fin : date de fin de la manifestation</li>
 * <li>Horaires : horaires de la manifestation</li>
 * <li>Dates affichage - horaires : champ texte dates et horaires</li>
 * <li>Modification dernière minute : valeur annulé/reporté</li>
 * <li>Lieu - nom : nom du lieu de la manifestation (ex : Halle aux grains, Le
 * Zénith, place du Capitole, Divers lieux, etc.)</li>
 * <li>Lieu - adresse 1 : escalier, porte, bâtiment</li>
 * <li>Lieu - adresse 2 : N° rue, avenue ou boulevard</li>
 * <li>Lieu - adresse 3 : BP, lieu-dit</li>
 * <li>Code postal : code bureau postal</li>
 * <li>Commune : commune accueillant la manifestation</li>
 * <li>Type de manifestation : Culturelle, Danse, Insolite, Manifestation
 * commerciale, Musique, Nature et détente, Religieuse, Son et Lumière, Sports
 * et loisirs, Traditions et folklore</li>
 * <li>Catégorie de la manifestation : Animations, Bal, Balade, Brocante,
 * Carnaval, Cinéma, Circuits culturels, Commémoration, Compétition sportive,
 * Concert, Concours, Conférence, Congres, Défilé Cortège Parade, Exposition,
 * Festival, Foire ou salon, Marché, Meeting, Opéra, Pélerinage et procession,
 * Portes ouvertes, Rallye, Récital, Rencontres, Soirée - Clubbing, Spectacle,
 * Sport, Stage, Théâtre, Vide greniers Braderie, Visites guidées</li>
 * <li>Thème de la manifestation : Animaux, Antiquité, Art contemporain, Art
 * lyrique, Artisanat, Astronomie, Atelier, Athlétisme gymnastique, Automobile,
 * Bande dessinée, Bio, Carte postale, Céramique, Cerf-volant, Chorale, Cinéma,
 * Cirque, Comique, Contes, Course à pied, Danse, Fanfares bandas, Fête, Feux
 * d'artifice, Fleurs Plantes, Gastronomie, Historique, Jazz et blues, Jeune
 * public, Littérature, Marionnette, Médiéval, Mode, Modélisme, Montgolfières,
 * Musée, Musical, Musique classique, Musique contemporaine, Musique de variété,
 * Musique du monde, Musique folklorique (country), Musique sacrée, Nocturne,
 * Noêl, Opérette, Peinture, Performance, Photographie, Plantes, Poésie, Pop
 * musique (rock...), Randonnée, Rap - reggae - soul - funk, Sculpture, Sport
 * cycliste, Sport équestre, Sports aériens, Sports d'hiver, Sports mécaniques,
 * Sports nautiques, Tauromachie, Tennis, Théâtre de rue, Vidéo, Vin - oenologie
 * </li>
 * <li>Station métro/Tram à proximité : nom de la station de métro ou de Tram à
 * proximité de la manifestation (10 min à pied maximum)</li>
 * <li>GoogleMap latitude : Coordonnée Y (latitude)</li>
 * <li>GoogleMap longitude : Coordonnée X (longitude)</li>
 * <li>Réservation téléphone : numéro de téléphone du service réservation et
 * d'information</li>
 * <li>Réservation email : adresse email du service réservation et d'information
 * </li>
 * <li>Réservation site internet : site web information et réservation en ligne</li>
 * <li>Manifestation gratuite : "oui" pour manifestation gratuite</li>
 * <li>Tarif normal : tarif adulte, tarif entrée de la manifestation</li>
 * <li>Tarif enfant : tarif enfant</li>
 * <li>Tranche d'âge enfant : indication des tranches d'âge jeune public : 0 - 3
 * ans / 4 - 7 ans / 8 - 12 ans / + 12 ans</li>
 * </ul>
 * 
 * @author pgillet
 * 
 */

public class EventBean {
	
	private String identifiant; 
	private String nomGénérique;
	private String nomDeLaManifestation;
	private String descriptifCourt;
	private String descriptifLong;
	private String dateDébut; 
	private String dateFin; 
	private String horaires;
	private String datesAffichageHoraires;
	private String modificationDernièreMinute;
	private String lieuNom;
	private String lieuAdresse1;
	private String lieuAdresse2;
	private String lieuAdresse3;
	private String codePostal; 
	private String commune;
	private String typeDeManifestation;
	private String catégorieDeLaManifestation;
	private String thèmeDeLaManifestation;
	private String stationMétroTramÀProximité;
	private String googlemapLatitude;
	private String googlemapLongitude;
	private String réservationTéléphone;
	private String réservationEmail;
	private String réservationSiteInternet;
	private String manifestationGratuite; 
	private String tarifNormal;
	private String tarifEnfant;
	private String trancheÂgeEnfant;
	/**
	 * @return the identifiant
	 */
	public String getIdentifiant() {
		return identifiant;
	}
	/**
	 * @param identifiant the identifiant to set
	 */
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	/**
	 * @return the nomGénérique
	 */
	public String getNomGénérique() {
		return nomGénérique;
	}
	/**
	 * @param nomGénérique the nomGénérique to set
	 */
	public void setNomGénérique(String nomGénérique) {
		this.nomGénérique = nomGénérique;
	}
	/**
	 * @return the nomDeLaManifestation
	 */
	public String getNomDeLaManifestation() {
		return nomDeLaManifestation;
	}
	/**
	 * @param nomDeLaManifestation the nomDeLaManifestation to set
	 */
	public void setNomDeLaManifestation(String nomDeLaManifestation) {
		this.nomDeLaManifestation = nomDeLaManifestation;
	}
	/**
	 * @return the descriptifCourt
	 */
	public String getDescriptifCourt() {
		return descriptifCourt;
	}
	/**
	 * @param descriptifCourt the descriptifCourt to set
	 */
	public void setDescriptifCourt(String descriptifCourt) {
		this.descriptifCourt = descriptifCourt;
	}
	/**
	 * @return the descriptifLong
	 */
	public String getDescriptifLong() {
		return descriptifLong;
	}
	/**
	 * @param descriptifLong the descriptifLong to set
	 */
	public void setDescriptifLong(String descriptifLong) {
		this.descriptifLong = descriptifLong;
	}
	/**
	 * @return the dateDébut
	 */
	public String getDateDébut() {
		return dateDébut;
	}
	/**
	 * @param dateDébut the dateDébut to set
	 */
	public void setDateDébut(String dateDébut) {
		this.dateDébut = dateDébut;
	}
	/**
	 * @return the dateFin
	 */
	public String getDateFin() {
		return dateFin;
	}
	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	/**
	 * @return the horaires
	 */
	public String getHoraires() {
		return horaires;
	}
	/**
	 * @param horaires the horaires to set
	 */
	public void setHoraires(String horaires) {
		this.horaires = horaires;
	}
	/**
	 * @return the datesAffichageHoraires
	 */
	public String getDatesAffichageHoraires() {
		return datesAffichageHoraires;
	}
	/**
	 * @param datesAffichageHoraires the datesAffichageHoraires to set
	 */
	public void setDatesAffichageHoraires(String datesAffichageHoraires) {
		this.datesAffichageHoraires = datesAffichageHoraires;
	}
	/**
	 * @return the modificationDernièreMinute
	 */
	public String getModificationDernièreMinute() {
		return modificationDernièreMinute;
	}
	/**
	 * @param modificationDernièreMinute the modificationDernièreMinute to set
	 */
	public void setModificationDernièreMinute(String modificationDernièreMinute) {
		this.modificationDernièreMinute = modificationDernièreMinute;
	}
	/**
	 * @return the lieuNom
	 */
	public String getLieuNom() {
		return lieuNom;
	}
	/**
	 * @param lieuNom the lieuNom to set
	 */
	public void setLieuNom(String lieuNom) {
		this.lieuNom = lieuNom;
	}
	/**
	 * @return the lieuAdresse1
	 */
	public String getLieuAdresse1() {
		return lieuAdresse1;
	}
	/**
	 * @param lieuAdresse1 the lieuAdresse1 to set
	 */
	public void setLieuAdresse1(String lieuAdresse1) {
		this.lieuAdresse1 = lieuAdresse1;
	}
	/**
	 * @return the lieuAdresse2
	 */
	public String getLieuAdresse2() {
		return lieuAdresse2;
	}
	/**
	 * @param lieuAdresse2 the lieuAdresse2 to set
	 */
	public void setLieuAdresse2(String lieuAdresse2) {
		this.lieuAdresse2 = lieuAdresse2;
	}
	/**
	 * @return the lieuAdresse3
	 */
	public String getLieuAdresse3() {
		return lieuAdresse3;
	}
	/**
	 * @param lieuAdresse3 the lieuAdresse3 to set
	 */
	public void setLieuAdresse3(String lieuAdresse3) {
		this.lieuAdresse3 = lieuAdresse3;
	}
	/**
	 * @return the codePostal
	 */
	public String getCodePostal() {
		return codePostal;
	}
	/**
	 * @param codePostal the codePostal to set
	 */
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	/**
	 * @return the commune
	 */
	public String getCommune() {
		return commune;
	}
	/**
	 * @param commune the commune to set
	 */
	public void setCommune(String commune) {
		this.commune = commune;
	}
	/**
	 * @return the typeDeManifestation
	 */
	public String getTypeDeManifestation() {
		return typeDeManifestation;
	}
	/**
	 * @param typeDeManifestation the typeDeManifestation to set
	 */
	public void setTypeDeManifestation(String typeDeManifestation) {
		this.typeDeManifestation = typeDeManifestation;
	}
	/**
	 * @return the catégorieDeLaManifestation
	 */
	public String getCatégorieDeLaManifestation() {
		return catégorieDeLaManifestation;
	}
	/**
	 * @param catégorieDeLaManifestation the catégorieDeLaManifestation to set
	 */
	public void setCatégorieDeLaManifestation(String catégorieDeLaManifestation) {
		this.catégorieDeLaManifestation = catégorieDeLaManifestation;
	}
	/**
	 * @return the thèmeDeLaManifestation
	 */
	public String getThèmeDeLaManifestation() {
		return thèmeDeLaManifestation;
	}
	/**
	 * @param thèmeDeLaManifestation the thèmeDeLaManifestation to set
	 */
	public void setThèmeDeLaManifestation(String thèmeDeLaManifestation) {
		this.thèmeDeLaManifestation = thèmeDeLaManifestation;
	}
	/**
	 * @return the stationMétroTramÀProximité
	 */
	public String getStationMétroTramÀProximité() {
		return stationMétroTramÀProximité;
	}
	/**
	 * @param stationMétroTramÀProximité the stationMétroTramÀProximité to set
	 */
	public void setStationMétroTramÀProximité(String stationMétroTramÀProximité) {
		this.stationMétroTramÀProximité = stationMétroTramÀProximité;
	}
	/**
	 * @return the googlemapLatitude
	 */
	public String getGooglemapLatitude() {
		return googlemapLatitude;
	}
	/**
	 * @param googlemapLatitude the googlemapLatitude to set
	 */
	public void setGooglemapLatitude(String googlemapLatitude) {
		this.googlemapLatitude = googlemapLatitude;
	}
	/**
	 * @return the googlemapLongitude
	 */
	public String getGooglemapLongitude() {
		return googlemapLongitude;
	}
	/**
	 * @param googlemapLongitude the googlemapLongitude to set
	 */
	public void setGooglemapLongitude(String googlemapLongitude) {
		this.googlemapLongitude = googlemapLongitude;
	}
	/**
	 * @return the réservationTéléphone
	 */
	public String getRéservationTéléphone() {
		return réservationTéléphone;
	}
	/**
	 * @param réservationTéléphone the réservationTéléphone to set
	 */
	public void setRéservationTéléphone(String réservationTéléphone) {
		this.réservationTéléphone = réservationTéléphone;
	}
	/**
	 * @return the réservationEmail
	 */
	public String getRéservationEmail() {
		return réservationEmail;
	}
	/**
	 * @param réservationEmail the réservationEmail to set
	 */
	public void setRéservationEmail(String réservationEmail) {
		this.réservationEmail = réservationEmail;
	}
	/**
	 * @return the réservationSiteInternet
	 */
	public String getRéservationSiteInternet() {
		return réservationSiteInternet;
	}
	/**
	 * @param réservationSiteInternet the réservationSiteInternet to set
	 */
	public void setRéservationSiteInternet(String réservationSiteInternet) {
		this.réservationSiteInternet = réservationSiteInternet;
	}
	/**
	 * @return the manifestationGratuite
	 */
	public String getManifestationGratuite() {
		return manifestationGratuite;
	}
	/**
	 * @param manifestationGratuite the manifestationGratuite to set
	 */
	public void setManifestationGratuite(String manifestationGratuite) {
		this.manifestationGratuite = manifestationGratuite;
	}
	/**
	 * @return the tarifNormal
	 */
	public String getTarifNormal() {
		return tarifNormal;
	}
	/**
	 * @param tarifNormal the tarifNormal to set
	 */
	public void setTarifNormal(String tarifNormal) {
		this.tarifNormal = tarifNormal;
	}
	/**
	 * @return the tarifEnfant
	 */
	public String getTarifEnfant() {
		return tarifEnfant;
	}
	/**
	 * @param tarifEnfant the tarifEnfant to set
	 */
	public void setTarifEnfant(String tarifEnfant) {
		this.tarifEnfant = tarifEnfant;
	}
	/**
	 * @return the trancheÂgeEnfant
	 */
	public String getTrancheÂgeEnfant() {
		return trancheÂgeEnfant;
	}
	/**
	 * @param trancheÂgeEnfant the trancheÂgeEnfant to set
	 */
	public void setTrancheÂgeEnfant(String trancheÂgeEnfant) {
		this.trancheÂgeEnfant = trancheÂgeEnfant;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventBean [identifiant=" + identifiant + ", nomGénérique="
				+ nomGénérique + ", nomDeLaManifestation="
				+ nomDeLaManifestation + ", descriptifCourt=" + descriptifCourt
				+ ", descriptifLong=" + descriptifLong + ", dateDébut="
				+ dateDébut + ", dateFin=" + dateFin + ", horaires=" + horaires
				+ ", datesAffichageHoraires=" + datesAffichageHoraires
				+ ", modificationDernièreMinute=" + modificationDernièreMinute
				+ ", lieuNom=" + lieuNom + ", lieuAdresse1=" + lieuAdresse1
				+ ", lieuAdresse2=" + lieuAdresse2 + ", lieuAdresse3="
				+ lieuAdresse3 + ", codePostal=" + codePostal + ", commune="
				+ commune + ", typeDeManifestation=" + typeDeManifestation
				+ ", catégorieDeLaManifestation=" + catégorieDeLaManifestation
				+ ", thèmeDeLaManifestation=" + thèmeDeLaManifestation
				+ ", stationMétroTramÀProximité=" + stationMétroTramÀProximité
				+ ", googlemapLatitude=" + googlemapLatitude
				+ ", googlemapLongitude=" + googlemapLongitude
				+ ", réservationTéléphone=" + réservationTéléphone
				+ ", réservationEmail=" + réservationEmail
				+ ", réservationSiteInternet=" + réservationSiteInternet
				+ ", manifestationGratuite=" + manifestationGratuite
				+ ", tarifNormal=" + tarifNormal + ", tarifEnfant="
				+ tarifEnfant + ", trancheÂgeEnfant=" + trancheÂgeEnfant + "]";
	}
	
}
