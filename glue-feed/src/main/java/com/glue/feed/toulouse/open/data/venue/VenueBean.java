package com.glue.feed.toulouse.open.data.venue;

/**
 * Description des champs :
 * 
 * <ul>
 * <li>EQ_NOM_EQUIPEMENT Char (254) ;
 * <li>
 * <li>EQ_GESTIONNAIRE Char (100) ;
 * <li>
 * <li>NUMERO Char (5) ;
 * <li>
 * <li>LIB_OFF Char (40) ;
 * <li>
 * <li>ID_SECTEUR_POSTAL Char (5) ;
 * <li>
 * <li>EQ_VILLE Char (254) ;
 * <li>
 * <li>EQ_BIBLIOTHEQUE Char (20) ;
 * <li>
 * <li>EQ_MUSEE Char (20) ;
 * <li>
 * <li>EQ_CINEMA Char (20) ;
 * <li>
 * <li>EQ_GALERIE_LIEU_EXPOSITION Char (20) ;
 * <li>
 * <li>EQ_CONF_RECEPTION Char (20) ;
 * <li>
 * <li>EQ_NTA Char (20) ;
 * <li>
 * <li>EQ_ENSEIGNEMENT Char (20) ;
 * <li>
 * <li>EQ_SALLE_DE_SPECTACLE Char (20) ;
 * <li>
 * <li>EQ_CENTRE_CULTUREL Char (20) ;
 * <li>
 * <li>EQ_CA_MJC_MQ Char (20) ;
 * <li>
 * <li>EQ_CAFE_CONCERT Char (20) ;
 * <li>
 * <li>EQ_STUDIO_DANS_MUSIQUE Char (20) ;
 * <li>
 * <li>EQ_SECTEUR Char (80) ;
 * <li>
 * <li>EQ_QUARTIER Char (80) ;
 * <li>
 * <li>X_CC43 Decimal (11, 3) ;
 * <li>
 * <li>Y_CC43 Decimal (11, 3) ;
 * <li>
 * <li>X_WGS84 Decimal (11, 8) ;
 * <li>
 * <li>Y_WGS84 Decimal (11, 8) ;
 * <li>
 * </ul>
 * 
 * @author grdenis
 * 
 */

public class VenueBean {

	private String eqNomEquipement;
	private String eqGestionnaire;
	private String numero;
	private String libOff;
	private String idSecteurPostal;
	private String eqVille;
	private String eqBibliotheque;
	private String eqMusee;
	private String eqCinema;
	private String eqGalerieLieuExposition;
	private String eqConfReception;
	private String eqNta;
	private String eqEnseignement;
	private String eqSalleDeSpectacle;
	private String eqCentreCulturel;
	private String eqCaMjcMq;
	private String eqCafeConcert;
	private String eqStudioDansMusique;
	private String eqSecteur;
	private String eqQuartier;
	private String xCc43;
	private String yCc43;
	private String xWgs84;
	private String yWgs84;

	public String getXCc43() {
		return xCc43;
	}

	public void setXCc43(String xCc43) {
		this.xCc43 = xCc43;
	}

	public String getYCc43() {
		return yCc43;
	}

	public void setYCc43(String yCc43) {
		this.yCc43 = yCc43;
	}

	public String getXWgs84() {
		return xWgs84;
	}

	public void setXWgs84(String xWgs84) {
		this.xWgs84 = xWgs84;
	}

	public String getYWgs84() {
		return yWgs84;
	}

	public void setYWgs84(String yWgs84) {
		this.yWgs84 = yWgs84;
	}

	public String getEqNomEquipement() {
		return eqNomEquipement;
	}

	public void setEqNomEquipement(String eqNomEquipement) {
		this.eqNomEquipement = eqNomEquipement;
	}

	public String getEqGestionnaire() {
		return eqGestionnaire;
	}

	public void setEqGestionnaire(String eqGestionnaire) {
		this.eqGestionnaire = eqGestionnaire;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLibOff() {
		return libOff;
	}

	public void setLibOff(String libOff) {
		this.libOff = libOff;
	}

	public String getIdSecteurPostal() {
		return idSecteurPostal;
	}

	public void setIdSecteurPostal(String idSecteurPostal) {
		this.idSecteurPostal = idSecteurPostal;
	}

	public String getEqVille() {
		return eqVille;
	}

	public void setEqVille(String eqVille) {
		this.eqVille = eqVille;
	}

	public String getEqBibliotheque() {
		return eqBibliotheque;
	}

	public void setEqBibliotheque(String eqBibliotheque) {
		this.eqBibliotheque = eqBibliotheque;
	}

	public String getEqMusee() {
		return eqMusee;
	}

	public void setEqMusee(String eqMusee) {
		this.eqMusee = eqMusee;
	}

	public String getEqCinema() {
		return eqCinema;
	}

	public void setEqCinema(String eqCinema) {
		this.eqCinema = eqCinema;
	}

	public String getEqGalerieLieuExposition() {
		return eqGalerieLieuExposition;
	}

	public void setEqGalerieLieuExposition(String eqGalerieLieuExposition) {
		this.eqGalerieLieuExposition = eqGalerieLieuExposition;
	}

	public String getEqConfReception() {
		return eqConfReception;
	}

	public void setEqConfReception(String eqConfReception) {
		this.eqConfReception = eqConfReception;
	}

	public String getEqNta() {
		return eqNta;
	}

	public void setEqNta(String eqNta) {
		this.eqNta = eqNta;
	}

	public String getEqEnseignement() {
		return eqEnseignement;
	}

	public void setEqEnseignement(String eqEnseignement) {
		this.eqEnseignement = eqEnseignement;
	}

	public String getEqSalleDeSpectacle() {
		return eqSalleDeSpectacle;
	}

	public void setEqSalleDeSpectacle(String eqSalleDeSpectacle) {
		this.eqSalleDeSpectacle = eqSalleDeSpectacle;
	}

	public String getEqCentreCulturel() {
		return eqCentreCulturel;
	}

	public void setEqCentreCulturel(String eqCentreCulturel) {
		this.eqCentreCulturel = eqCentreCulturel;
	}

	public String getEqCaMjcMq() {
		return eqCaMjcMq;
	}

	public void setEqCaMjcMq(String eqCaMjcMq) {
		this.eqCaMjcMq = eqCaMjcMq;
	}

	public String getEqCafeConcert() {
		return eqCafeConcert;
	}

	public void setEqCafeConcert(String eqCafeConcert) {
		this.eqCafeConcert = eqCafeConcert;
	}

	public String getEqStudioDansMusique() {
		return eqStudioDansMusique;
	}

	public void setEqStudioDansMusique(String eqStudioDansMusique) {
		this.eqStudioDansMusique = eqStudioDansMusique;
	}

	public String getEqSecteur() {
		return eqSecteur;
	}

	public void setEqSecteur(String eqSecteur) {
		this.eqSecteur = eqSecteur;
	}

	public String getEqQuartier() {
		return eqQuartier;
	}

	public void setEqQuartier(String eqQuartier) {
		this.eqQuartier = eqQuartier;
	}

	@Override
	public String toString() {
		return "VenueBean [eqNomEquipement=" + eqNomEquipement + ", eqGestionnaire=" + eqGestionnaire + ", numero="
				+ numero + ", libOff=" + libOff + ", idSecteurPostal=" + idSecteurPostal + ", eqVille=" + eqVille
				+ ", eqBibliotheque=" + eqBibliotheque + ", eqMusee=" + eqMusee + ", eqCinema=" + eqCinema
				+ ", eqGalerieLieuExposition=" + eqGalerieLieuExposition + ", eqConfReception=" + eqConfReception
				+ ", eqNta=" + eqNta + ", eqEnseignement=" + eqEnseignement + ", eqSalleDeSpectacle="
				+ eqSalleDeSpectacle + ", eqCentreCulturel=" + eqCentreCulturel + ", eqCaMjcMq=" + eqCaMjcMq
				+ ", eqCafeConcert=" + eqCafeConcert + ", eqStudioDansMusique=" + eqStudioDansMusique + ", eqSecteur="
				+ eqSecteur + ", eqQuartier=" + eqQuartier + ", xCc43=" + xCc43 + ", yCc43=" + yCc43 + ", xWgs84="
				+ xWgs84 + ", yWgs84=" + yWgs84 + "]";
	}

}
