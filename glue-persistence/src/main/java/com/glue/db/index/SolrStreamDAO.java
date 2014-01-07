package com.glue.db.index;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.glue.struct.IStream;
import com.glue.webapp.db.StreamDAO;

public class SolrStreamDAO extends StreamDAO implements SolrDAO {

	public static final String FIELD_LONGITUDE = "latlng_1_coordinate";
	public static final String FIELD_LATITUDE = "latlng_0_coordinate";
	public static final String FIELD_CITY = "city";
	public static final String FIELD_VENUE = "venue";
	public static final String FIELD_END_DATE = "end_date";
	public static final String FIELD_START_DATE = "start_date";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_ID = "id";
	public SolrBaseDAO solrBaseDAO = new SolrBaseDAO();

	public SolrStreamDAO() {
		super();
	}

	@Override
	public IStream create(IStream obj) throws Exception {
		IStream stream = super.create(obj);

		SolrInputDocument doc = new SolrInputDocument();

		// See also SolrInputDocument#addField(String name, Object value,float
		// boost)
		doc.addField(FIELD_ID, stream.getId());
		doc.addField(FIELD_TITLE, stream.getTitle());
		doc.addField(FIELD_CATEGORY, stream.getCategory().getName());
		doc.addField(FIELD_DESCRIPTION, stream.getDescription());
		doc.addField(FIELD_START_DATE, stream.getStartDate());
		doc.addField(FIELD_END_DATE, stream.getEndDate());
		doc.addField(FIELD_VENUE, stream.getVenue().getName());
		doc.addField(FIELD_CITY, stream.getVenue().getCity());
		doc.addField(FIELD_LATITUDE, stream.getVenue().getLatitude());
		doc.addField(FIELD_LONGITUDE, stream.getVenue().getLongitude());

		addDoc(doc);

		return stream;
	}

	@Override
	public void delete(long id) throws Exception {
		super.delete(id);
		getSolrServer().deleteById(Long.toString(id));
		getSolrServer().commit();
	}

	public void update(IStream aStream) throws Exception {
		super.update(aStream);
		// TODO ...
	}

	/**
	 * @return the solrServer
	 */
	public SolrServer getSolrServer() {
		return solrBaseDAO.getSolrServer();
	}

	/**
	 * @param solrServer
	 *            the solrServer to set
	 */
	public void setSolrServer(SolrServer solrServer) {
		solrBaseDAO.setSolrServer(solrServer);
	}

	public void addDoc(SolrInputDocument doc) throws SolrServerException, IOException {
		solrBaseDAO.addDoc(doc);
	}

	@Override
	public void flush() throws IOException {
		solrBaseDAO.flush();
	}

}
