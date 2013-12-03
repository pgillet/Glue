package com.glue.feed.toulouse.open.data.biblio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Titre du jeu de données: Agenda des manifestations de la bibliothèque de
 * Toulouse
 * 
 * Accès au flux XML: http://www.bibliotheque.toulouse.fr/fluxEvents.xml
 * 
 * Description: Flux XML qui contient tous les événements et animations en place
 * au sein de la Bibliothèque de Toulouse (Médiathèque José Cabanis,
 * Bibliothèque d'Etude et du Patrimoine de Périgord, et les 22 bibliothèques de
 * quartier). Les événements sont soit ponctuels avec un créneau horaire, soit
 * durables sur plusieurs jours consécutifs (exposition, semaine thématique,
 * etc), soit cycliques sur des créneaux identifiés (cycle de conférences,
 * ateliers multimédias, etc). Sont précisés les jours, heures, lieux de la
 * manifestation, ainsi qu'une catégorisation plus fine selon le thème de
 * l'événement. On peut retrouver toutes ces manifestations : sur le site web de
 * la bibliothèque, et dans le Manifesta, programme imprimé bimestriel de
 * l'ensemble de ces animations.
 * 
 * Fréquence de mise à jour: Quotidienne
 * 
 * Description des éléments:
 * <ul>
 * <li>record webevent: identifiant unique</li>
 * <li>language :langue (fr=français, par défaut)
 * <li>
 * <li>title: titre de la manifestation
 * <li>
 * <li>where: lieu de la manifestation
 * <li>
 * <li>startTime: heure de début. Format :  « jj/mm/aaaa 14h15 »
 * <li>
 * <li>endTime: heure de fin. Format :  « jj/mm/aaaa 14h15 »
 * <li>
 * <li>summary: description de la manifestation (512 caractères max)
 * <li>
 * <li>link: url de la fiche complète de la manifestation sur le site de la
 * bibliothèque de Toulouse :  http://www.bibliotheque.toulouse.fr
 * <li>
 * <li>status: champ donnant un statut particulier à certaines animations. Pas
 * utilisé.
 * <li>
 * <li>illustration: nom du fichier image associé à la manifestation. L'url de
 * l'image est du type :  http://www.bibliotheque.toulouse.fr/nom_image.jpg
 * <li>
 * <li>genre : genre de la manifestation : Exposition, conférence, rencontre,
 * atelier, lecture, club de lecture, projection, conte, concert, visite,
 * spectacle
 * <li>
 * <li>public: Public visé préférentiellement par la manifestation : Tout
 * public, professionnel, jeune, adulte</li>
 * <li>theme: thème de la manifestation : Cinéma, Musique, arts, L'oeil et la
 * lettre, Langues et Littératures, Patrimoine, Sciences et loisirs, Société,
 * Histoire, Actualité, Jeunesse, Intermezzo
 * <li>
 * <li><published />: etat de la fiche descriptive de la manifestation. Celles
 * du flux sont toutes dans l'état : <published />
 * <li>
 * </ul>
 * 
 * 
 * Informations légales
 * 
 * Propriétaire des données: Mairie de Toulouse Licence: Odbl Contact:
 * webmestre@bibliothequedetoulouse.fr
 * 
 * @author pgillet
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Record {

	@XmlAttribute(name = "webevent")
	int id;

	String language;

	String title;

	String where;

	String startTime;

	String endTime;

	String summary;

	String link;

	String status;

	String illustration;

	String genre;

	@XmlElement(name = "public")
	String audience;

	String theme;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Record [id=" + id + ", language=" + language + ", title="
				+ title + ", where=" + where + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", summary=" + summary + ", link="
				+ link + ", status=" + status + ", illustration="
				+ illustration + ", genre=" + genre + ", audience=" + audience
				+ ", theme=" + theme + "]";
	}
	
	
}
