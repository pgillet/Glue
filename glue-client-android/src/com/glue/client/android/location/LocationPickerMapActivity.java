package com.glue.client.android.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SearchRecentSuggestions;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.glue.client.android.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class LocationPickerMapActivity extends MapActivity {

	/**
	 * The basic component of SingleItemizedOverlay.
	 */
	private class AddressedOverlayItem extends OverlayItem {

		private Address address;

		public AddressedOverlayItem(GeoPoint point, String title, String snippet) {
			super(point, title, snippet);
		}

		/**
		 * @return the address
		 */
		public Address getAddress() {
			return address;
		}

		public Location getPointAsLocation() {

			GeoPoint p = getPoint();
			// Constructs a new Location with a null provider
			Location location = new Location((String) null);
			location.setLatitude(p.getLatitudeE6() / 1E6);
			location.setLongitude(p.getLongitudeE6() / 1E6);

			return location;

		}

		/**
		 * @param address
		 *            the address to set
		 */
		public void setAddress(Address address) {
			this.address = address;
		}

	}

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

				GeoPoint p = myLocation.getMyLocation();
				if (p != null) {
					// If the user's current location is known, we specify a
					// bounding box centered on it to get search results in
					// close proximity.
					double mylat = p.getLatitudeE6() / 1E6;
					double mylong = p.getLongitudeE6() / 1E6;

					addresses = geocoder.getFromLocationName(locationName,
							MAX_RESULTS, mylat - HALF_SIDE, mylong - HALF_SIDE,
							mylat + HALF_SIDE, mylong + HALF_SIDE);
				} else {
					addresses = geocoder.getFromLocationName(locationName,
							MAX_RESULTS);
				}
			} catch (IOException e) {
				e.printStackTrace();
				// Update UI field with the exception.
				// Message.obtain(getHandler(), ADDRESS_NOT_FOUND, e.toString())
				// .sendToTarget();

				// Message.obtain(mHandler, LocationConstants.ADDRESS_NOT_FOUND,
				// getString(R.string.network_not_available))
				// .sendToTarget();
			}

			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> result) {

			if (result != null) {
				// adapter.clear();
				adapter = new AddressAdapter(LocationPickerMapActivity.this,
						R.layout.address_entry);
				adapter.setNotifyOnChange(true);
				tv.setAdapter(adapter);

				for (Address address : result) {
					adapter.add(new AddressDecorator(address));
					adapter.notifyDataSetChanged();
				}
			}
		}

	}

	// AsyncTask encapsulating the geocoding API. Since the geocoder API
	// is blocked, we do not want to invoke it from the UI thread.
	private class GeocodingTask extends AsyncTask<String, Void, Void> {
		Context mContext;
		private ProgressDialog progressDialog;

		public GeocodingTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected Void doInBackground(String... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			String locationName = params[0];
			List<Address> addresses = null;
			try {
				// Call the synchronous getFromLocationName() method by passing
				// in the entered location name.
				addresses = geocoder.getFromLocationName(locationName, 1);
			} catch (IOException e) {
				e.printStackTrace();
				// Update UI field with the exception.
				// Message.obtain(getHandler(), ADDRESS_NOT_FOUND, e.toString())
				// .sendToTarget();

				Message.obtain(mHandler, LocationConstants.ADDRESS_NOT_FOUND,
						getString(R.string.network_not_available))
						.sendToTarget();
			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Update the UI via a message handler.
				Message.obtain(mHandler, LocationConstants.UPDATE_ADDRESS,
						address).sendToTarget();
			} else {
				Message.obtain(mHandler, LocationConstants.ADDRESS_NOT_FOUND,
						getString(R.string.no_result) + ": " + locationName)
						.sendToTarget();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			searching = false;
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			searching = true;
			progressDialog = ProgressDialog.show(
					LocationPickerMapActivity.this, null,
					getString(R.string.searching_for) + ": " + query, true);
		}
	}

	/**
	 * An overlay that maintains a single overlay item. The class draws a marker
	 * for the location chosen by the user, either by searching for an address
	 * or by taping on the map view.
	 * 
	 * For some reason, we get a NullPointerException when getting a MapView
	 * with an ItemizedOverlay with no OverlayItems (there is no initial overlay
	 * item and the user must pick a location on tap or search for an address).
	 * The workaround is to maintain a list of OverlayItems while we actually
	 * need a single instance, and call {@link ItemizedOverlay#populate()} after
	 * the call of the super constructor.
	 * 
	 * @see http://code.google.com/p/android/issues/detail?id=2035
	 * 
	 * @author pgillet
	 */
	private class SingleItemizedOverlay extends ItemizedOverlay {

		private boolean hasOverlay;
		private Context mContext;
		private Handler mHandler;
		private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public SingleItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			// See http://code.google.com/p/android/issues/detail?id=2035
			populate();

			// Handler that gets an Address from the location tapped by the user
			// on the map view with the given latitude and longitude, and
			// overrides the OverlayItem's snippet with it.
			mHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case LocationConstants.UPDATE_ADDRESS:
						Address address = (Address) msg.obj;

						AddressedOverlayItem item = (AddressedOverlayItem) mOverlays
								.get(0);
						item.setAddress(address);

						// TODO refresh the dialog if it is currently
						// showing

						break;
					}
				}
			};
		}

		public SingleItemizedOverlay(Drawable defaultMarker, Context context) {
			this(defaultMarker);
			mContext = context;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		private void doReverseGeocoding(Location location) {
			// Since the geocoding API is synchronous and may take a while. You
			// don't want to lock up the UI thread. Invoking reverse geocoding
			// in an AsyncTask.
			(new ReverseGeocodingTask(LocationPickerMapActivity.this, mHandler))
					.execute(new Location[] { location });
		}

		/**
		 * Tests if the overlay has been set.
		 * 
		 * @return
		 */
		public boolean hasOverlay() {
			return hasOverlay;
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			if (super.onTap(p, mapView)) {
				return true;
			}

			if (togglePickLocation.isChecked()) {

				AddressedOverlayItem item = new AddressedOverlayItem(p, null,
						"Lat: " + (p.getLatitudeE6() / 1E6) + ", Long: "
								+ (p.getLongitudeE6() / 1E6));

				setOverlay(item);

				doReverseGeocoding(item.getPointAsLocation());

				// Set enabled the OK button
				buttonOK.setEnabled(true);
			}
			return false;
		}

		@Override
		protected boolean onTap(int index) {
			AddressedOverlayItem item = (AddressedOverlayItem) mOverlays
					.get(index);
			if (item.getAddress() == null) {
				// Retry
				doReverseGeocoding(item.getPointAsLocation());
			}

			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			Address address = item.getAddress();
			dialog.setMessage(address != null ? formatAddress(address) : item
					.getSnippet());
			dialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							onClickOK(null);
						}
					}).setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});
			dialog.show();
			return true;
		}

		public void setOverlay(OverlayItem overlay) {
			// Only one overlay
			mOverlays.clear();
			mOverlays.add(overlay);
			hasOverlay = true;
			populate();
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

	}

	private static final int ZOOM_LEVEL = 16;

	private AddressAdapter adapter;

	private Button buttonOK;
	private SingleItemizedOverlay itemizedOverlay;
	private MapController mc;

	private Handler mHandler;
	private MyLocationOverlay myLocation = null;
	private boolean searching;

	private ToggleButton togglePickLocation;

	private AutoCompleteTextView tv;

	private String query;

	/**
	 * Add a marker on the map view at the given address.
	 * 
	 * @param address
	 */
	private void addMarker(Address address) {
		String addressText = formatAddress(address);

		GeoPoint point = new GeoPoint((int) (address.getLatitude() * 1E6),
				(int) (address.getLongitude() * 1E6));
		AddressedOverlayItem overlayitem = new AddressedOverlayItem(point,
				null, addressText);
		overlayitem.setAddress(address);

		itemizedOverlay.setOverlay(overlayitem);

		// Hide the virtual keyboard
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(tv.getWindowToken(), 0);

		// Center the map on the overlay
		mc.animateTo(point);
		mc.setZoom(ZOOM_LEVEL);

		// Reset the text view
		tv.setText(null);
		tv.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.holo_light_action_search, 0, 0, 0);

		// Set enabled the OK button
		buttonOK.setEnabled(true);
	}

	private void doGeocoding(String locationName) {
		// Since the geocoding API is synchronous and may take a while. You
		// don't want to lock up the UI thread. Invoking geocoding in an
		// AsyncTask.
		(new GeocodingTask(this)).execute(new String[] { locationName });
	}

	/**
	 * Format the first line of address (if available), city, and country name.
	 * 
	 * @param address
	 * @return
	 */
	private String formatAddress(Address address) {
		String addressText = String.format("%s, %s, %s", address
				.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
				address.getLocality(), address.getCountryName());
		return addressText;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private boolean isSearching() {
		return searching;
	}

	public void onClickOK(View v) {
		AddressedOverlayItem item = (AddressedOverlayItem) itemizedOverlay
				.getItem(0);

		Intent data = new Intent();
		Address address = item.getAddress();

		if (address != null) {
			data.putExtra(LocationConstants.ADDRESS, address);
		} else {
			data.putExtra(LocationConstants.LOCATION, item.getPointAsLocation());
		}

		setResult(RESULT_OK, data);
		finish();
	}

	public void onClickPickLocation(View v) {
		// TODO
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_picker_map);
		handleIntent(getIntent());

		tv = (AutoCompleteTextView) findViewById(R.id.editText1);
		adapter = new AddressAdapter(this, R.layout.address_entry);
		adapter.setNotifyOnChange(true);
		tv.setAdapter(adapter);

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSearchRequested();
			}
		});

		tv.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 1) {
					// adapter.clear();
					(new DynamicGeocodingTask(LocationPickerMapActivity.this))
							.execute(new String[] { tv.getText().toString() });
				}
			}
		});
		tv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Address address = (Address) parent.getAdapter().getItem(
						position);
				addMarker(address);
			}
		});

		buttonOK = (Button) findViewById(R.id.buttonOK);
		togglePickLocation = (ToggleButton) findViewById(R.id.togglePickLocation);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		final List<Overlay> mapOverlays = mapView.getOverlays();

		// Add an overlay for the location picked by the user
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.holo_light_location_place);
		itemizedOverlay = new SingleItemizedOverlay(drawable, this);
		mapOverlays.add(itemizedOverlay);

		// Add current location on the map
		myLocation = new MyLocationOverlay(getApplicationContext(), mapView);
		mapOverlays.add(myLocation);
		myLocation.enableMyLocation();

		mc = mapView.getController();
		myLocation.runOnFirstFix(new Runnable() {
			public void run() {
				if (!isSearching() && !itemizedOverlay.hasOverlay()) {
					mc.animateTo(myLocation.getMyLocation());
					mc.setZoom(ZOOM_LEVEL);
				}
			}
		});

		// Handler for adding an overlay item on the map with the address found
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case LocationConstants.UPDATE_ADDRESS:
					Address address = (Address) msg.obj;
					addMarker(address);

					// Save the address for later suggestion
					if (address.getMaxAddressLineIndex() > 0) {
						saveRecentQuery(address.getAddressLine(0),
								address.getLocality());
					} else if (address.getLocality() != null) {
						saveRecentQuery(address.getLocality(), null);
					}
					break;
				case LocationConstants.ADDRESS_NOT_FOUND:
					String text = (String) msg.obj;
					Toast.makeText(LocationPickerMapActivity.this, text,
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_location_picker_map, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Turn off updates when in the background.
		myLocation.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Enable user's current location
		myLocation.enableMyLocation();
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			doGeocoding(query);
		}
	}

	private void saveRecentQuery(String query, String line2) {
		Intent intent = getIntent();

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// String query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, LocationSuggestionsProvider.AUTHORITY,
					LocationSuggestionsProvider.MODE);
			suggestions.saveRecentQuery(query, line2);
		}
	}
}
