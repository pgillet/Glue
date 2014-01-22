package com.glue.feed.toulouse.open.data.so;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.glue.domain.IStream;
import com.glue.domain.IVenue;
import com.glue.domain.impl.Stream;
import com.glue.domain.impl.Venue;
import com.glue.feed.DataSourceManager;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;

/**
 * Read from a CSV file the list of cultural events and event producers in
 * France and abroad organizations in 2011.
 * 
 * @author pgillet
 * @see http://www.data.gouv.fr/DataSet/30382214
 */
public class ReadCVS {

	private DAOManager manager;
	private StreamDAO streamDAO;
	private VenueDAO venueDAO;

	public static void main(String[] args) throws IOException, SQLException {

		ReadCVS obj = new ReadCVS();
		obj.run();

	}

	public void run() throws IOException, SQLException {

		String line = null;
		final String delim = ";";
		int count = 0;

		DateFormat format = new SimpleDateFormat("dd/MM/yy"); // ex: "14/05/77"

		DataSource ds = DataSourceManager.getInstance().getDataSource();
		manager = DAOManager.getInstance(ds);
		streamDAO = manager.getStreamDAO();
		venueDAO = manager.getVenueDAO();

		final Geocoder geocoder = new Geocoder();
		GeocoderRequestBuilder geocoderRequestBuilder = new GeocoderRequestBuilder();
		geocoderRequestBuilder.setLanguage("fr"); // Not sure about that

		URL url = new URL("http://www.data.gouv.fr/var/download/258a71ec1cd4dc1d6264304de7ba5317.csv");

		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

			while ((line = br.readLine()) != null) {

				count++;
				// use semi-colon as separator
				String[] fields = line.split(delim);

				// 0: libelleOrg
				// 1: idMemo
				// 2: typeOrganisme
				// 3: ancienNom
				// 4: paysOrg
				// 5: adresseComplAdresseOrg
				// 6: adresseVoieNumeroOrg
				// 7: adresseVoieExtOuIndRepOrg
				// 8: adresseVoieTypeVoieOrg
				// 9: adresseVoieNomVoieOrg
				// 10: adresseMentionsDistribOrg
				// 11: adresseAcheminPostalCedexOrg
				// 12: adresseAcheminPostalLibelleCedexOrg
				// 13: adresseCodePostalOrg
				// 14: adresseCommuneOrg
				// 15: adresseCodeInseeOrg
				// 16: interneMUMuseeDeFrance
				// 17: natureOrganisme
				// 18: libelleOffre
				// 19: typeOffre
				// 20: themes
				// 21: horaire
				// 22: tarifs
				// 23: conditions
				// 24: premiereOuverture
				// 25: ouvertureExceptionnelle
				// 26: hebdoEstOuvertLundi
				// 27: hebdoEstOuvertMardi
				// 28: hebdoEstOuvertMercredi
				// 29: hebdoEstOuvertJeudi
				// 30: hebdoEstOuvertVendredi
				// 31: hebdoEstOuvertSamedi
				// 32: hebdoEstOuvertDimanche
				// 33: periodeDateDebut
				// 34: periodeDateFin
				// 35: temporaire
				// 36: joursExceptionnels
				// 37: gratuit
				// 38: libelleLieu
				// 39: typesLieu
				// 40: batiment
				// 41: accesHandicapes
				// 42: adresseComplAdresseLieu
				// 43: adresseVoieNumeroLieu
				// 44: adresseVoieExtOuIndRepLieu
				// 45: adresseVoieTypeVoieLieu
				// 46: adresseVoieNomVoieLieu
				// 47: adresseMentionsDistribLieu
				// 48: adresseAcheminPostalCedexLieu
				// 49: adresseAcheminPostalLibelleCedexLieu
				// 50: adresseCodePostalLieu
				// 51: adresseCommuneLieu
				// 52: adresseCodeInseeLieu
				// 53: interneClasse
				// 54: interneDateClasse
				// 55: interneInscrit
				// 56: interneDateInscrit
				// 57: interneElementClasseOuInscrit
				// 58: interneEspaceProtege
				// 59: interneEtendueProtection
				// 60: paysLieu

				try {
					boolean temp = "1".equals(fields[35]) ? true : false;

					if (temp) {

						IVenue venue = new Venue();
						venue.setName(fields[0]);

						StringBuilder sb = new StringBuilder();
						// sb.append(fields[0]).append(", ");
						sb.append(fields[5]).append(" ").append(fields[6]).append(" ").append(fields[7]).append(" ")
								.append(fields[8]).append(" ").append(fields[9]).append(" ").append(fields[10])
								.append(" ").append(fields[11]).append(" ")
								/*
								 * .append(fields[12]) .append(" ")
								 */.append(fields[13]).append(" ").append(fields[14]).append(", ").append(fields[4]);

						String address = sb.toString().trim().replaceAll("\\s+", " ");

						venue.setAddress(address);

						// Search for an existing venue
						IVenue persistentVenue = venueDAO.searchForDuplicate(venue);
						if (persistentVenue == null) {
							// Geocoding: convert the address into geographic
							// coordinates (latitude and longitude)
							// GeocoderRequest geocoderRequest =
							// geocoderRequestBuilder
							// .setAddress(address).getGeocoderRequest();
							// GeocodeResponse geocoderResponse = geocoder
							// .geocode(geocoderRequest);
							//
							// List<GeocoderResult> geocoderResults =
							// geocoderResponse
							// .getResults();
							// if (geocoderResults.size() > 0) {
							// // Reverse geocoding
							// LatLng location = geocoderResults.get(0)
							// .getGeometry().getLocation();
							//
							// venue.setLatitude(location.getLat()
							// .doubleValue());
							// venue.setLongitude(location.getLng()
							// .doubleValue());
							// }

							System.out.println("Inserting " + venue);
							persistentVenue = venueDAO.create(venue);
						}

						IStream stream = new Stream();
						stream.setTitle(fields[18]);
						StringBuilder description = new StringBuilder();
						description.append("Nature de l'organisme: ").append(fields[17]).append("<br/>")
								.append("Type d'évènement: ").append(fields[19]).append("<br/>").append("Thèmes: ")
								.append(fields[20]).append("<br/>").append("Horaires: ").append(fields[21])
								.append("<br/>").append("Tarifs: ").append(fields[22]).append("<br/>")
								.append("Conditions: ").append(fields[23]);
						stream.setDescription(description.toString());

						Date startDate = format.parse(fields[33].trim());
						Date endDate = format.parse(fields[34].trim());

						stream.setStartDate(startDate.getTime());
						stream.setEndDate(endDate.getTime());

						stream.setPublicc(true);
						stream.setOpen(true);

						stream.setVenue(persistentVenue);

						System.out.println("Inserting " + stream);
						streamDAO.create(stream);
					}
				} catch (Exception e) {
					System.out.println("Line " + count);
					e.printStackTrace();
				}

			}

		}

		System.out.println("Done");
	}

}
