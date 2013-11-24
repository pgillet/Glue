package com.glue.feed.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.sql.DataSource;

import com.glue.feed.DataSourceManager;
import com.glue.struct.Category;
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
	private Map<String, String> catDico;
	private DateFormat format;

	public static void main(String[] args) throws IOException {

		URL url = new URL(
				" http://data.grandtoulouse.fr/web/guest/les-donnees/-/opendata/card/21905-agenda-des-manifestations-culturelles/resource/document?p_p_state=exclusive&_5_WAR_opendataportlet_jspPage=%2Fsearch%2Fview_card_license.jsp");

		ZipInputStream zin = new ZipInputStream(url.openStream());
		ZipEntry ze = zin.getNextEntry();
		while (!ze.getName().endsWith(".csv")) {
			zin.closeEntry();
			ze = zin.getNextEntry();
		}

		// CSV files encoding = "Windows-1252"
		BufferedReader reader = new BufferedReader(new InputStreamReader(zin, Charset.forName("Windows-1252")));

		OpenDataToulouseReadCVS obj = new OpenDataToulouseReadCVS();
		obj.run(reader);

		reader.close();

	}

	public void run(BufferedReader reader) throws IOException {

		String line = null;
		int countCommitted = 0;
		int countTotal = 0;

		DataSource ds = DataSourceManager.getInstance().getDataSource();
		manager = DAOManager.getInstance(ds);
		format = new SimpleDateFormat("dd/MM/yy"); // ex: "14/05/77"
		TimeZone tz = TimeZone.getTimeZone("UTC");
		format.setTimeZone(tz);

		try {
			streamDAO = manager.getStreamDAO();
			venueDAO = manager.getVenueDAO();

			// Get dictionary
			catDico = getCategoryDictionnary();

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
		} finally {
			manager.closeConnectionQuietly();
		}

		System.out.println(countCommitted + "/" + countTotal + " have been successful imported.");
	}

	private void manageLine(String currentLine) throws SQLException {

		final String delim = "\";\"";

		// 0: Identifiant : identifiant unique de la manifestation (16
		// caractï¿½res)
		// 1: Nom gï¿½nï¿½rique : intitulï¿½ gï¿½nï¿½ral qui regroupe plusieurs
		// manifestations (nom du festival)
		// 2: Nom de la manifestation : titre de la manifestation
		// 3: Descriptif court : courte description de la manifestation
		// (caractï¿½res limitï¿½s ï¿½ 300)
		// 4: Descriptif long : description dï¿½taillï¿½e de la
		// manifestation (caractï¿½res illimitï¿½s)
		// 5: Date dï¿½but : date de dï¿½but de la manifestation
		// 6: Date fin : date de fin de la manifestation
		// 7: Horaires : horaires de la manifestation
		// 8: Dates affichage - horaires : champ texte dates et horaires
		// 9: Modification derniï¿½re minute : valeur annulï¿½/reportï¿½
		// 10: Lieu - nom : nom du lieu de la manifestation (ex : Halle
		// aux grains, Le Zï¿½nith, place du Capitole, Divers lieux, etc.)
		// 11: Lieu - adresse 1 : escalier, porte, bï¿½timent
		// 12: Lieu - adresse 2 : Nï¿½ rue, avenue ou boulevard
		// 13: Lieu - adresse 3 : BP, lieu-dit
		// 14: Code postal : code bureau postal
		// 15: Commune : commune accueillant la manifestation
		// 16: Type de manifestation : Culturelle, Danse, Insolite,
		// Manifestation commerciale, Musique, Nature et dï¿½tente,
		// Religieuse, Son et Lumiï¿½re, Sports et loisirs, Traditions et
		// folklore
		// 17: Catï¿½gorie de la manifestation : Animations, Bal, Balade,
		// Brocante, Carnaval, Cinï¿½ma, Circuits culturels,
		// Commï¿½moration, Compï¿½tition sportive, Concert, Concours,
		// Confï¿½rence, Congres, Dï¿½filï¿½ Cortï¿½ge Parade, Exposition,
		// Festival, Foire ou salon, Marchï¿½, Meeting, Opï¿½ra, Pï¿½lerinage
		// et procession, Portes ouvertes, Rallye, Rï¿½cital, Rencontres,
		// Soirï¿½e - Clubbing, Spectacle, Sport, Stage, Thï¿½ï¿½tre, Vide
		// greniers Braderie, Visites guidï¿½es
		// 18: Thï¿½me de la manifestation : Animaux, Antiquitï¿½, Art
		// contemporain, Art lyrique, Artisanat, Astronomie, Atelier,
		// Athlï¿½tisme gymnastique, Automobile, Bande dessinï¿½e, Bio,
		// Carte postale, Cï¿½ramique, Cerf-volant, Chorale, Cinï¿½ma,
		// Cirque, Comique, Contes, Course ï¿½ pied, Danse, Fanfares
		// bandas, Fï¿½te, Feux d'artifice, Fleurs Plantes, Gastronomie,
		// Historique, Jazz et blues, Jeune public, Littï¿½rature,
		// Marionnette, Mï¿½diï¿½val, Mode, Modï¿½lisme, Montgolfiï¿½res,
		// Musï¿½e,
		// Musical, Musique classique, Musique contemporaine, Musique de
		// variï¿½tï¿½, Musique du monde, Musique folklorique (country),
		// Musique sacrï¿½e, Nocturne, Noï¿½l, Opï¿½rette, Peinture,
		// Performance, Photographie, Plantes, Poï¿½sie, Pop musique
		// (rock...), Randonnï¿½e, Rap - reggae - soul - funk, Sculpture,
		// Sport cycliste, Sport ï¿½questre, Sports aï¿½riens, Sports
		// d'hiver, Sports mï¿½caniques, Sports nautiques, Tauromachie,
		// Tennis, Thï¿½ï¿½tre de rue, Vidï¿½o, Vin - oenologie
		// 19: Station mï¿½tro/Tram ï¿½ proximitï¿½ : nom de la station de
		// mï¿½tro ou de Tram ï¿½ proximitï¿½ de la manifestation (10 min ï¿½
		// pied maximum)
		// 20: GoogleMap latitude : Coordonnï¿½e Y (latitude)
		// 21: GoogleMap longitude : Coordonnï¿½e X (longitude)
		// 22: Rï¿½servation tï¿½lï¿½phone : numï¿½ro de tï¿½lï¿½phone du
		// service
		// rï¿½servation et d'information
		// 23: Rï¿½servation email : adresse email du service rï¿½servation
		// et d'information
		// 24: Rï¿½servation site internet : site web information et
		// rï¿½servation en ligne
		// 25: Manifestation gratuite : "oui" pour manifestation
		// gratuite
		// 26: Tarif normal : tarif adulte, tarif entrï¿½e de la
		// manifestation
		// 27: Tarif enfant : tarif enfant
		// 28: Tranche d'ï¿½ge enfant : indication des tranches d'ï¿½ge
		// jeune public : 0 - 3 ans / 4 - 7 ans / 8 - 12 ans / + 12 ans

		// use semi-colon as separator
		String[] fields = currentLine.split(delim);

		// Dates
		String strdate = cleanup(fields[5]);
		String enddate = cleanup(fields[6]);
		Date sdate = null;
		Date edate = null;
		try {
			sdate = format.parse(strdate);

			// If endate empty, set endate to start_date
			if ("".equals(enddate)) {
				edate = sdate;
			} else {
				edate = format.parse(enddate);
			}
		} catch (ParseException e) {
			System.out.println("Format de date incorrect " + sdate + " " + edate);
		}

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
		stream.setEndDate(edate.getTime());
		Category cat = getCategory(fields[16], fields[17], fields[18]);
		stream.setCategory(cat);
		stream.setPrice(cleanup(fields[26]));

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

	// Get categories from a string like "CAT1, CAT2, CAT3"
	private Set<String> extractCategoriesFromField(String field) {

		Set<String> result = new HashSet<String>();
		if (!"".equals(field)) {
			String[] values = field.split(",");
			for (int i = 0; i < values.length; i++) {
				result.add(values[i].toLowerCase().trim());
			}
		}
		return result;
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

		// On ne prend que la premiï¿½re des URL
		if (url.contains(" ")) {
			url = url.substring(0, url.indexOf(" "));
		}
		return url;
	}

	private Category getCategory(String field1, String field2, String field3) {

		// Get all possible Categories
		Set<String> categories = extractCategoriesFromField(cleanup(field1));
		categories.addAll(extractCategoriesFromField(cleanup(field2)));
		categories.addAll(extractCategoriesFromField(cleanup(field3)));

		// Some main rules concert > spectacle
		if (categories.contains("concert")) {
			return Category.MUSIC;
		}

		if (categories.contains("conference") || categories.contains("conférence")) {
			return Category.CONFERENCE;
		}

		if (categories.contains("spectacle")) {
			return Category.PERFORMING_ART;
		}

		if (categories.contains("exposition")) {
			return Category.EXHIBITION;
		}

		if (categories.contains("photographie")) {
			return Category.EXHIBITION;
		}

		// Return the first found
		for (String catStr : categories) {

			// Try to find mapping from dico
			String category = catDico.get(catStr.toLowerCase());
			if (category != null && !"".equals(category)) {
				return Category.valueOf(category.toUpperCase());
			}
		}

		return Category.OTHER;
	}

	// Retrieve categories dictionnary from property file
	private Map<String, String> getCategoryDictionnary() {

		Map<String, String> dico = new HashMap<>();
		Properties properties = new Properties();
		InputStream in = OpenDataToulouseReadCVS.class.getResourceAsStream("../dico.properties");
		Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		try {
			properties.load(reader);
			for (Entry<Object, Object> entry : properties.entrySet()) {

				// Retrieve cat_name from key (key = glue.category.cat_name)
				String value = (String) entry.getKey();

				if (value.startsWith("glue.category")) {
					value = value.substring(value.lastIndexOf(".") + 1, value.length());

					// Split values (value = cat1#cat2# ...)
					String[] keys = ((String) entry.getValue()).split("#");
					for (int i = 0; i < keys.length; i++) {
						dico.put(keys[i], value);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dico;
	}
}
