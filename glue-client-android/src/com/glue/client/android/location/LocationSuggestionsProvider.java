package com.glue.client.android.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.SearchManager;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;

public class LocationSuggestionsProvider extends
		SearchRecentSuggestionsProvider {
	private class DynamicGeocodingTask extends
			AsyncTask<String, Void, List<Address>> {

		private static final double HALF_SIDE = 0.5;
		private static final int MAX_RESULTS = 5;
		Context mContext;

		public DynamicGeocodingTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected List<Address> doInBackground(String... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			String locationName = params[0];
			List<Address> addresses = null;
			try {
				// Call the synchronous getFromLocationName() method by passing
				// in the entered location name.
				if (latitude != 0 && longitude != 0) {
					// If the user's current location is known, we specify a
					// bounding box centered on it to get search results nearby.

					addresses = geocoder.getFromLocationName(locationName,
							MAX_RESULTS, latitude - HALF_SIDE, longitude
									- HALF_SIDE, latitude + HALF_SIDE,
							longitude + HALF_SIDE);
				} else {
					addresses = geocoder.getFromLocationName(locationName,
							MAX_RESULTS);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> result) {
			suggestedAddresses = result;
		}

	}

	public final static String AUTHORITY = "com.glue.client.android.location.LocationSuggestionsProvider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;
	private static final int SUGGESTIONS_CODE = 5;

	private MatrixCursor cursor;

	private UriMatcher matcher;

	private double latitude;
	private double longitude;

	public void setMyLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	List<Address> suggestedAddresses = null;

	public LocationSuggestionsProvider() {
		matcher = new UriMatcher(UriMatcher.NO_MATCH);

		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,
				SUGGESTIONS_CODE);
		setupSuggestions(AUTHORITY, MODE);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor defaultCursor = super.query(uri, projection, selection,
				selectionArgs, sortOrder);

		int code = matcher.match(uri);
		switch (code) {
		case SUGGESTIONS_CODE:
			if (selectionArgs == null || selectionArgs.length == 0
					|| selectionArgs[0].trim().length() == 0) {
				return defaultCursor;
			} else {
				cursor = new MatrixCursor(defaultCursor.getColumnNames());
				if (suggestedAddresses != null) {
					for (Address address : suggestedAddresses) {

						String text1 = address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "";
						String text2 = address.getLocality();

						cursor.addRow(new Object[] { null, text1, text2,
								text1 + " " + text2, 0 });
					}
				}

				(new DynamicGeocodingTask(getContext())).execute(selectionArgs);
				return new MergeCursor(new Cursor[] { cursor, defaultCursor });
			}
		default:
			return defaultCursor;
		}
	}
}