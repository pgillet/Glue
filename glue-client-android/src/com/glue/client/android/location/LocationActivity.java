package com.glue.client.android.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.glue.client.android.R;

/**
 * Code largely inspired from
 * http://developer.android.com/training/basics/location/index.html
 * 
 * @author pgillet
 */
public abstract class LocationActivity extends FragmentActivity {

	/**
	 * Dialog to prompt users to enable GPS on the device.
	 */
	private class EnableGpsDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.gps_disabled)
					.setMessage(R.string.enable_gps_dialog)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									enableLocationSettings();
								}
							}).create();
		}
	}

	// AsyncTask encapsulating the reverse-geocoding API. Since the geocoder API
	// is blocked,
	// we do not want to invoke it from the UI thread.
	private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
		Context mContext;

		public ReverseGeocodingTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected Void doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			Location loc = params[0];
			List<Address> addresses = null;
			SimpleLocation locationMsg = new SimpleLocation();
			locationMsg.setLatitude(loc.getLatitude());
			locationMsg.setLongitude(loc.getLongitude());

			try {
				// Call the synchronous getFromLocation() method by passing in
				// the lat/long values.
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
				// Update UI field with the exception.
				// Message.obtain(getHandler(), UPDATE_ADDRESS, e.toString())
				// .sendToTarget();

				Message.obtain(getHandler(), UPDATE_ADDRESS, locationMsg)
						.sendToTarget();
			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Format the first line of address (if available), city, and
				// country name.
				String addressText = String.format(
						"%s, %s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address.getLocality(),
						address.getCountryName());
				// Update the UI via a message handler.
				locationMsg.setAddressText(addressText);
				Message.obtain(getHandler(), UPDATE_ADDRESS, locationMsg)
						.sendToTarget();
			}
			return null;
		}
	}

	private static final int TEN_METERS = 10;
	private static final int TEN_SECONDS = 10000;
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	// UI handler codes.
	public static final int UPDATE_ADDRESS = 1;
	public static final int UPDATE_LATLNG = 2;

	private final LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// A new location update is received. Do something useful with it.
			// Update the UI with the location update.
			updateUILocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private boolean locationEnabled = true;

	private boolean mGeocoderAvailable;

	LocationManager mLocationManager;

	private boolean reverseGeocodingEnabled = true;

	private void doReverseGeocoding(Location location) {
		// Since the geocoding API is synchronous and may take a while. You
		// don't want to lock
		// up the UI thread. Invoking reverse geocoding in an AsyncTask.
		(new ReverseGeocodingTask(this)).execute(new Location[] { location });
	}

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix. Code taken from
	 * http://developer.android.com/guide/topics/location
	 * /obtaining-user-location.html
	 * 
	 * @param newLocation
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 * @return The better Location object based on recency and accuracy.
	 */
	protected Location getBetterLocation(Location newLocation,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return newLocation;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved.
		if (isSignificantlyNewer) {
			return newLocation;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return currentBestLocation;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return newLocation;
		} else if (isNewer && !isLessAccurate) {
			return newLocation;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return newLocation;
		}
		return currentBestLocation;
	}

	/**
	 * Handler for updating text fields on the UI like the lat/long and address.
	 * 
	 * @return
	 */
	public abstract Handler getHandler();

	public boolean isGeocoderAvailable() {
		return mGeocoderAvailable;
	}

	/**
	 * Tells whether the location is enabled or disabled.
	 * 
	 * @return true if enabled, false otherwise
	 */
	protected boolean isLocationEnabled() {
		return locationEnabled;
	}

	/**
	 * Tells whether the reverse geocoding is enabled or disabled.
	 * 
	 * @return
	 */
	public boolean isReverseGeocodingEnabled() {
		return reverseGeocodingEnabled;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get a reference to the LocationManager object.
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// The isPresent() helper method is only available on Gingerbread or
		// above.
		mGeocoderAvailable = true; // We try our luck!
		// Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
		// && Geocoder.isPresent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isLocationEnabled()) {
			setup();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// This verification should be done during onStart() because the system
		// calls this method when the user returns to the activity, which
		// ensures the desired location provider is enabled each time the
		// activity resumes from the stopped state.
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			// Build an alert dialog here that requests that the user enable
			// the location services, then when the user clicks the "OK" button,
			// call enableLocationSettings()
			new EnableGpsDialogFragment().show(getSupportFragmentManager(),
					"enableGpsDialog");
		}
	}

	// Stop receiving location updates whenever the Activity becomes invisible.
	@Override
	protected void onStop() {
		super.onStop();
		mLocationManager.removeUpdates(listener);
	}

	/**
	 * Method to register location updates with a desired location provider. If
	 * the requested provider is not available on the device, the app displays a
	 * Toast with a message referenced by a resource id.
	 * 
	 * @param provider
	 *            Name of the requested provider.
	 * @param errorResId
	 *            Resource id for the string message to be displayed if the
	 *            provider does not exist on the device.
	 * @return A previously returned {@link android.location.Location} from the
	 *         requested provider, if exists.
	 */
	private Location requestUpdatesFromProvider(final String provider,
			final int errorResId) {
		Location location = null;
		if (mLocationManager.isProviderEnabled(provider)) {
			mLocationManager.requestLocationUpdates(provider, TEN_SECONDS,
					TEN_METERS, listener);
			location = mLocationManager.getLastKnownLocation(provider);
		} else {
			Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
	}

	/**
	 * Enables (or disables) the location capabilities of this Activity.
	 * 
	 * @param b
	 *            true to enable the location, otherwise false
	 */
	protected void setLocationEnabled(boolean b) {
		locationEnabled = b;
		if (locationEnabled) {
			setup();
		} else {
			// Stop receiving location updates
			mLocationManager.removeUpdates(listener);
		}
	}

	/**
	 * Enables (or disables) the reverse geocoding, i.e. the process of
	 * translating latitude longitude coordinates to a human-readable address.
	 * 
	 * @param reverseGeocodingEnabled
	 */
	public void setReverseGeocodingEnabled(boolean reverseGeocodingEnabled) {
		this.reverseGeocodingEnabled = reverseGeocodingEnabled;
	}

	/**
	 * Set up fine and/or coarse location providers depending on whether both
	 * providers are enabled and supported.
	 */
	protected void setup() {
		Location gpsLocation = null;
		Location networkLocation = null;
		mLocationManager.removeUpdates(listener);

		final boolean gpsEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (gpsEnabled) {
			// Request updates from the fine (gps) provider.
			gpsLocation = requestUpdatesFromProvider(
					LocationManager.GPS_PROVIDER, R.string.not_support_gps);
			// Update the UI immediately if a location is obtained.
			if (gpsLocation != null) {
				updateUILocation(gpsLocation);
			}
		}

		// Request updates from coarse (network) provider.
		networkLocation = requestUpdatesFromProvider(
				LocationManager.NETWORK_PROVIDER, R.string.not_support_network);

		// If both providers return last known locations, compare the two
		// and use the better one to update the UI. If only one provider
		// returns a location, use it.
		if (gpsLocation != null && networkLocation != null) {
			updateUILocation(getBetterLocation(gpsLocation, networkLocation));
		} else if (gpsLocation != null) {
			updateUILocation(gpsLocation);
		} else if (networkLocation != null) {
			updateUILocation(networkLocation);
		}
	}

	private void updateUILocation(Location location) {
		// We're sending the update to a handler which then updates the UI with
		// the new location.

		SimpleLocation locationMsg = new SimpleLocation();
		locationMsg.setLatitude(location.getLatitude());
		locationMsg.setLongitude(location.getLongitude());

		Message.obtain(getHandler(), UPDATE_LATLNG, locationMsg).sendToTarget();

		// Bypass reverse-geocoding only if the Geocoder service is available on
		// the device.
		if (isGeocoderAvailable() && isReverseGeocodingEnabled()) {
			doReverseGeocoding(location);
		}
	}

}
