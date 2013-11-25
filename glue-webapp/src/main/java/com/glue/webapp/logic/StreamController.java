package com.glue.webapp.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.struct.IVenue;
import com.glue.webapp.db.DAOCommand;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;
import com.glue.webapp.repository.RepositoryManager;
import com.glue.webapp.search.PageIterator;
import com.glue.webapp.search.SearchEngine;

public class StreamController implements PageIterator<List<IStream>> {

	static final Logger LOG = LoggerFactory.getLogger(StreamController.class);

	@Inject
	private SearchEngine<IStream> engine;

	private int start;

	private int rowsPerPage = PageIterator.DEFAULT_ROWS;

	private long totalRows;

	private String queryString;

	private List<String> categories = new ArrayList<>();

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString
	 *            the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<IStream> search() throws InternalServerException {

		// The underlying search engine returns only partial streams (i.e. only
		// the properties that are actually indexed)
		engine.setQueryString(queryString);
		engine.setCategories(categories.toArray(new String[categories.size()]));
		engine.setStartDate(null);
		engine.setEndDate(null);
		engine.setStart(start);
		engine.setRows(rowsPerPage);

		List<IStream> temp = engine.search();

		// Stores the total number of found results
		totalRows = engine.getNumFound();

		final List<Long> ids = new ArrayList<Long>();

		for (IStream stream : temp) {
			ids.add(stream.getId());
		}

		try {
			DAOManager manager = DAOManager.getInstance();
			List<IStream> streams = manager.transaction(new DAOCommand<List<IStream>>() {

				@Override
				public List<IStream> execute(DAOManager manager) throws Exception {

					// Store venues to avoid repetitive SQL requests
					Map<Long, IVenue> m = new HashMap<Long, IVenue>();

					StreamDAO streamDAO = manager.getStreamDAO();
					VenueDAO venueDAO = manager.getVenueDAO();

					List<IStream> items = streamDAO.searchInList(ids.toArray(new Long[ids.size()]));

					for (IStream stream : items) {

						IVenue persistentVenue = m.get(stream.getVenue().getId());
						if (persistentVenue == null) { // Not stored yet
							persistentVenue = venueDAO.search(stream.getVenue().getId());

							// Store the persistent venue into the map
							m.put(persistentVenue.getId(), persistentVenue);
						}

						// Replace the dummy venue with the
						// persistent one
						stream.setVenue(persistentVenue);
					}

					return items;
				}

			});

			return streams;
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		}
	}

	/**
	 * This method should be moved in another controller dedicated to stream
	 * creation.
	 * 
	 * @param stream
	 * @param venue
	 * @param admin
	 * @throws InternalServerException
	 */
	public void createStream(final IStream stream, final IVenue venue, final IUser admin)
			throws InternalServerException {
		try {
			DAOManager manager = DAOManager.getInstance();

			manager.transaction(new DAOCommand<Void>() {

				@Override
				public Void execute(DAOManager manager) throws Exception {
					StreamDAO streamDAO = manager.getStreamDAO();
					VenueDAO venueDAO = manager.getVenueDAO();

					// Search for an existing venue
					IVenue persistentVenue = venueDAO.search(venue.getAddress());
					if (persistentVenue == null) {
						persistentVenue = venueDAO.create(venue);
					}

					stream.setVenue(persistentVenue);
					streamDAO.create(stream);

					// Set user as administrator
					streamDAO.joinAsAdmin(stream.getId(), admin.getId());

					// Create associated directory
					if (!RepositoryManager.createStream(stream.getId())) {
						throw new IOException("Cannot create stream directory");
					}

					return null;
				}
			});

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		}
	}
	
	public IStream search(final long id) throws InternalServerException {
		try {
			DAOManager manager = DAOManager.getInstance();
			IStream stream = manager.transaction(new DAOCommand<IStream>() {

				@Override
				public IStream execute(DAOManager manager) throws Exception {

					StreamDAO streamDAO = manager.getStreamDAO();
					VenueDAO venueDAO = manager.getVenueDAO();

					IStream stream = streamDAO.search(id);
					IVenue venue = venueDAO.search(stream.getVenue().getId());
					stream.setVenue(venue);
					
					// TODO: retrieve media

					return stream;
				}

			});

			return stream;
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		}
	}

	@Override
	public boolean hasNext() {
		return getPageIndex() < (getTotalPages() - 1);
	}

	@Override
	public List<IStream> next() throws InternalServerException, NoSuchElementException {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		start += rowsPerPage;
		return search();
	}

	@Override
	public boolean hasPrevious() {
		return getPageIndex() > 0;
	}

	@Override
	public List<IStream> previous() throws InternalServerException, NoSuchElementException {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}

		start -= rowsPerPage;
		return search();
	}

	@Override
	public List<IStream> first() throws InternalServerException {
		start = 0;
		return search();
	}

	@Override
	public List<IStream> last() throws InternalServerException {
		start = (getTotalPages() - 1) * rowsPerPage; // last page index
		return search();
	}

	@Override
	public List<IStream> get(int pageNumber) throws NoSuchElementException {
		// TODO Not yet implemented!
		return null;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	@Override
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	@Override
	public long getTotalRows() {
		return totalRows;
	}

	/**
	 * For pagination control from request to request.
	 */
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	@Override
	public int getPageIndex() {
		return start / rowsPerPage;
	}

	@Override
	public int getTotalPages() {
		int num = (int) totalRows / rowsPerPage;
		if (totalRows % rowsPerPage > 0) {
			num++;
		}

		return num;
	}
}
