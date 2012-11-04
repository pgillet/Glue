package com.glue.client.android.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
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

	// AsyncTask encapsulating the geocoding API. Since the geocoder API
	// is blocked, we do not want to invoke it from the UI thread.
	private class GeocodingTask extends AsyncTask<String, Void, Void> {
		private static final int MAX_RESULTS = 5;
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
				// in
				// the entered location name.
				addresses = geocoder.getFromLocationName(locationName,
						MAX_RESULTS);
			} catch (IOException e) {
				e.printStackTrace();
				// Update UI field with the exception.
				// Message.obtain(getHandler(), ADDRESS_NOT_FOUND, e.toString())
				// .sendToTarget();

				Message.obtain(mHandler, ADDRESS_NOT_FOUND,
						getString(R.string.network_not_available))
						.sendToTarget();
			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Update the UI via a message handler.
				Message.obtain(mHandler, UPDATE_ADDRESS, address)
						.sendToTarget();
			} else {
				Message.obtain(mHandler, ADDRESS_NOT_FOUND,
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
					getString(R.string.searching_for) + ": " + tv.getText(),
					true);
		}
	}
	private class PinItemizedOverlay extends ItemizedOverlay {

		private boolean hasOverlay;
		private Context mContext;
		private OverlayItem mOverlay;

		public PinItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			mOverlay = new OverlayItem(new GeoPoint(0, 0), null, null);
			populate();
		}

		public PinItemizedOverlay(Drawable defaultMarker, Context context) {
			this(defaultMarker);
			mContext = context;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlay;
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
				setOverlay(new OverlayItem(p, null, "Lat: "
						+ (p.getLatitudeE6() / 1E6) + ", Long: "
						+ (p.getLongitudeE6() / 1E6)));

				// Set enabled the OK button
				buttonOK.setEnabled(true);
			}
			return false;
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlay;
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		}

		public void setOverlay(OverlayItem overlay) {
			mOverlay = overlay;
			hasOverlay = true;
			populate();
		}

		@Override
		public int size() {
			return 1;
		}

	}
	public static final int ADDRESS_NOT_FOUND = 2;
	private static final String ADDRESS_TEXT = "address_text";
	private static final String LATITUDE = "latitude";

	private static final String LONGITUDE = "longitude";

	// UI handler codes.
	public static final int UPDATE_ADDRESS = 1;
	private static final int ZOOM_LEVEL = 16;
	private Button buttonOK;
	private PinItemizedOverlay itemizedOverlay;

	private Handler mHandler;
	private MyLocationOverlay myLocation = null;
	private boolean searching;

	private ToggleButton togglePickLocation;

	private TextView tv;

	private void doGeocoding(String locationName) {
		// Since the geocoding API is synchronous and may take a while. You
		// don't want to lock up the UI thread. Invoking geocoding in an
		// AsyncTask.
		(new GeocodingTask(this)).execute(new String[] { locationName });
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private boolean isSearching() {
		return searching;
	}

	public void onClickOK(View v) {
		OverlayItem item = itemizedOverlay.getItem(0);

		Intent data = new Intent();
		data.putExtra(LATITUDE, item.getPoint().getLatitudeE6() / 1E6);
		data.putExtra(LONGITUDE, item.getPoint().getLongitudeE6() / 1E6);
		data.putExtra(ADDRESS_TEXT, item.getSnippet());

		setResult(RESULT_OK, data);
		finish();
	}

	public void onClickPickLocation(View v) {
		// TODO
	}

	public void onClickSearch(View v) {
		CharSequence locationName = tv.getText();
		if (locationName != null && locationName.length() > 0) {
			doGeocoding(locationName.toString());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_picker_map);

		tv = (TextView) findViewById(R.id.editText1);
		buttonOK = (Button) findViewById(R.id.buttonOK);
		togglePickLocation = (ToggleButton) findViewById(R.id.togglePickLocation);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		final List<Overlay> mapOverlays = mapView.getOverlays();

		// Add an overlay for the location picked by the user
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.location_place);
		itemizedOverlay = new PinItemizedOverlay(drawable, this);
		mapOverlays.add(itemizedOverlay);

		// Add current location on the map
		myLocation = new MyLocationOverlay(getApplicationContext(), mapView);
		mapOverlays.add(myLocation);
		myLocation.enableMyLocation();

		final MapController mc = mapView.getController();
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
				case UPDATE_ADDRESS:
					Address address = (Address) msg.obj;
					// Format the first line of address (if available), city,
					// and country name.
					String addressText = String.format(
							"%s, %s, %s",
							address.getMaxAddressLineIndex() > 0 ? address
									.getAddressLine(0) : "", address
									.getLocality(), address.getCountryName());

					GeoPoint point = new GeoPoint(
							(int) (address.getLatitude() * 1E6),
							(int) (address.getLongitude() * 1E6));
					OverlayItem overlayitem = new OverlayItem(point, null,
							addressText);

					itemizedOverlay.setOverlay(overlayitem);

					// Hide the virtual keyboard
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(tv.getWindowToken(), 0);

					// Center the map on the overlay
					mc.animateTo(point);
					mc.setZoom(ZOOM_LEVEL);

					// Set enabled the OK button
					buttonOK.setEnabled(true);

					break;
				case ADDRESS_NOT_FOUND:
					String text = (String) msg.obj;
					Toast.makeText(getApplicationContext(), text,
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
}
