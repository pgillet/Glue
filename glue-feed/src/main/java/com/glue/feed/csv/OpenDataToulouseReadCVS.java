package com.glue.feed.csv;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.sql.DataSource;

import com.glue.feed.DataSourceManager;
import com.glue.struct.IStream;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.Venue;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;

/**
 * Read from a CSV file the list of cultural events in Toulouse and
 * surroundings.
 * 
 * @author grdenis
 */
public class OpenDataToulouseReadCVS {

	private DAOManager manager;
	private StreamDAO streamDAO;
	private VenueDAO venueDAO;

	public static void main(String[] args) throws IOException {

		URL url = new URL(
				" http://data.grandtoulouse.fr/web/guest/les-donnees/-/opendata/card/21905-agenda-des-manifestations-culturelles/resource/document?p_p_state=exclusive&_5_WAR_opendataportlet_jspPage=%2Fsearch%2Fview_card_license.jsp");

		ZipInputStream zin = new ZipInputStream(url.openStream());
		ZipEntry ze = zin.getNextEntry();
		while (!ze.getName().endsWith(".csv")) {
			zin.closeEntry();
			ze = zin.getNextEntry();
		}

		// Get the stream from zipentry
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int count;
		byte data[] = new byte[1024];
		while ((count = zin.read(data, 0, 1024)) > -1) {
			bout.write(data, 0, count);
		}
		bout.close();
		zin.close();

		// Create the reader
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		BufferedReader reader = new BufferedReader(new InputStreamReader(bin));

		OpenDataToulouseReadCVS obj = new OpenDataToulouseReadCVS();
		obj.run(reader);

	}

	public void run(BufferedReader reader) throws IOException {

		String line = null;
		int countCommitted = 0;
		int countTotal = 0;

		DataSource ds = DataSourceManager.getInstance().getDataSource();
		manager = DAOManager.getInstance(ds);

		try {
			streamDAO = manager.getStreamDAO();
			venueDAO = manager.getVenueDAO();

			String nextLine = "";
			String currentLine = null;
			// First line, commentary fields
			line = reader.readLine();
			line = reader.readLine();
			while (line != null) {
				currentLine = line;
				nextLine = reader.readLine();
				while ((nextLine != null) && !currentLine.endsWith(";\"\"")) {
					currentLine += nextLine.trim();
					nextLine = reader.readLine();
				}
				try {
					manageLine(currentLine);
					countCommitted++;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					countTotal++;
				}

				line = nextLine;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(countCommitted + "/" + countTotal + " have been successful imported.");
	}

	private void manageLine(String currentLine) throws SQLException {

		final String delim = "\";\"";

		// 0: Identifiant : identifiant unique de la manifestation (16
		// caractères)
		// 1: Nom générique : intitulé général qui regroupe plusieurs
		// manifestations (nom du festival)
		// 2: Nom de la manifestation : titre de la manifestation
		// 3: Descriptif court : courte description de la manifestation
		// (caractères limités à 300)
		// 4: Descriptif long : description détaillée de la
		// manifestation (caractères illimités)
		// 5: Date début : date de début de la manifestation
		// 6: Date fin : date de fin de la manifestation
		// 7: Horaires : horaires de la manifestation
		// 8: Dates affichage - horaires : champ texte dates et horaires
		// 9: Modification dernière minute : valeur annulé/reporté
		// 10: Lieu - nom : nom du lieu de la manifestation (ex : Halle
		// aux grains, Le Zénith, place du Capitole, Divers lieux, etc.)
		// 11: Lieu - adresse 1 : escalier, porte, bâtiment
		// 12: Lieu - adresse 2 : N° rue, avenue ou boulevard
		// 13: Lieu - adresse 3 : BP, lieu-dit
		// 14: Code postal : code bureau postal
		// 15: Commune : commune accueillant la manifestation
		// 16: Type de manifestation : Culturelle, Danse, Insolite,
		// Manifestation commerciale, Musique, Nature et détente,
		// Religieuse, Son et Lumière, Sports et loisirs, Traditions et
		// folklore
		// 17: Catégorie de la manifestation : Animations, Bal, Balade,
		// Brocante, Carnaval, Cinéma, Circuits culturels,
		// Commémoration, Compétition sportive, Concert, Concours,
		// Conférence, Congres, Défilé Cortège Parade, Exposition,
		// Festival, Foire ou salon, Marché, Meeting, Opéra, Pélerinage
		// et procession, Portes ouvertes, Rallye, Récital, Rencontres,
		// Soirée - Clubbing, Spectacle, Sport, Stage, Théâtre, Vide
		// greniers Braderie, Visites guidées
		// 18: Thème de la manifestation : Animaux, Antiquité, Art
		// contemporain, Art lyrique, Artisanat, Astronomie, Atelier,
		// Athlétisme gymnastique, Automobile, Bande dessinée, Bio,
		// Carte postale, Céramique, Cerf-volant, Chorale, Cinéma,
		// Cirque, Comique, Contes, Course à pied, Danse, Fanfares
		// bandas, Fête, Feux d'artifice, Fleurs Plantes, Gastronomie,
		// Historique, Jazz et blues, Jeune public, Littérature,
		// Marionnette, Médiéval, Mode, Modélisme, Montgolfières, Musée,
		// Musical, Musique classique, Musique contemporaine, Musique de
		// variété, Musique du monde, Musique folklorique (country),
		// Musique sacrée, Nocturne, Noêl, Opérette, Peinture,
		// Performance, Photographie, Plantes, Poésie, Pop musique
		// (rock...), Randonnée, Rap - reggae - soul - funk, Sculpture,
		// Sport cycliste, Sport équestre, Sports aériens, Sports
		// d'hiver, Sports mécaniques, Sports nautiques, Tauromachie,
		// Tennis, Théâtre de rue, Vidéo, Vin - oenologie
		// 19: Station métro/Tram à proximité : nom de la station de
		// métro ou de Tram à proximité de la manifestation (10 min à
		// pied maximum)
		// 20: GoogleMap latitude : Coordonnée Y (latitude)
		// 21: GoogleMap longitude : Coordonnée X (longitude)
		// 22: Réservation téléphone : numéro de téléphone du service
		// réservation et d'information
		// 23: Réservation email : adresse email du service réservation
		// et d'information
		// 24: Réservation site internet : site web information et
		// réservation en ligne
		// 25: Manifestation gratuite : "oui" pour manifestation
		// gratuite
		// 26: Tarif normal : tarif adulte, tarif entrée de la
		// manifestation
		// 27: Tarif enfant : tarif enfant
		// 28: Tranche d'âge enfant : indication des tranches d'âge
		// jeune public : 0 - 3 ans / 4 - 7 ans / 8 - 12 ans / + 12 ans

		DateFormat format = new SimpleDateFormat("dd/MM/yy"); // ex: "14/05/77"

		// use semi-colon as separator
		String[] fields = currentLine.split(delim);

		// Dates
		String strdate = cleanup(fields[5]);
		String enddate = cleanup(fields[6]);
		Date sdate = null;
		Date edate = null;
		try {
			sdate = format.parse(strdate);
			edate = format.parse(enddate);
		} catch (ParseException e) {
			System.out.println("Format de date incorrect " + sdate + " " + edate);
		}

		System.out.println("Date = " + sdate);
		// System.out.println("Date = " + edate);

		// Description
		String description = (cleanup(fields[3]) + "\n" + cleanup(fields[7])).trim();
		// System.out.println("Description = " + description);

		// Venue address
		String address = (cleanup(fields[11]) + " " + cleanup(fields[12]) + " " + cleanup(fields[13]) + " "
				+ cleanup(fields[14]) + " " + cleanup(fields[15])).trim();
		// System.out.println("Adresse = " + address.trim());

		// Venue name
		String name = cleanup(fields[10]).toUpperCase();
		// System.out.println("Name = " + name);

		// Venue latitude
		String latitude = cleanup(fields[20]);

		// Venue longitude
		String longitude = cleanup(fields[21]);

		IStream stream = new Stream();
		stream.setTitle(cleanup(fields[2]));
		stream.setDescription(description);
		stream.setPublicc(true);
		stream.setOpen(true);
		stream.setStartDate(sdate.getTime());
		if (edate.getTime() != sdate.getTime()) {
			stream.setEndDate(edate.getTime());
		}

		IVenue venue = new Venue();
		venue.setName(name);
		if (!"".equals(latitude) && !"".equals(longitude)) {
			double dlatitude = Double.parseDouble(latitude);
			double dlongitude = Double.parseDouble(longitude);
			venue.setLatitude(dlatitude);
			venue.setLongitude(dlongitude);
			// Reverse Geoconding (only 2500 request a day)
			/*
			 * geocoderRequest = new GeocoderRequestBuilder() .setLocation(new
			 * LatLng(BigDecimal.valueOf(dlatitude),
			 * BigDecimal.valueOf(dlongitude)))
			 * .setLanguage("fr").getGeocoderRequest(); geocoderResponse =
			 * geocoder.geocode(geocoderRequest); if
			 * (!geocoderResponse.getResults().isEmpty()) {
			 * System.out.println(geocoderResponse
			 * .getResults().get(0).getFormattedAddress());
			 * venue.setAddress(geocoderResponse
			 * .getResults().get(0).getFormattedAddress()); }
			 */
		}
		if (venue.getAddress() == null || "".equals(venue.getAddress())) {
			venue.setAddress(address.trim());
		}

		venue.setUrl(removeUrlExceptions(cleanup(fields[24])));

		// Search for an existing stream
		if (!streamDAO.exist(stream.getTitle(), stream.getStartDate())) {

			// Search for an existing venue
			IVenue persistentVenue = venueDAO.search(venue.getAddress());
			if (persistentVenue == null) {
				System.out.println("Inserting " + venue);
				persistentVenue = venueDAO.create(venue);
			}
			stream.setVenue(persistentVenue);

			System.out.println("Inserting " + stream);
			streamDAO.create(stream);
		} else {
			throw new SQLException("Duplicate Stream " + stream.getTitle() + "!");
		}
	}

	private String cleanup(String str) {
		if (str.startsWith("\"")) {
			str = str.substring(1, str.length());
		}
		if (str.endsWith("\"")) {
			str = str.substring(0, str.length() - 1);
		}
		str = str.replaceAll("\"\"", "\"");
		return str.trim();
	}

	private String removeUrlExceptions(String url) {
		Set<String> exceptions = new HashSet<String>();
		exceptions.add("www.fnac.com");
		exceptions.add("www.box.fr");
		for (String exception : exceptions) {
			url = url.replaceAll(exception, "");
		}

		// On ne prend que la première des URL
		if (url.contains(" ")) {
			url = url.substring(0, url.indexOf(" "));
		}
		return url;
	}

}
