package com.glue.client.android.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * AsyncTask encapsulating the reverse-geocoding API. Since the geocoder API is
 * blocked, we do not want to invoke it from the UI thread.
 * 
 * @author pgillet
 */
class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {

	Context mContext;
	private Handler mHandler;

	public ReverseGeocodingTask(Context context, Handler handler) {
		super();
		mContext = context;
		mHandler = handler;
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

			Message.obtain(mHandler, LocationConstants.UPDATE_ADDRESS,
					locationMsg).sendToTarget();
		}
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);
			// Format the first line of address (if available), city, and
			// country name.
			String addressText = String.format("%s, %s, %s", address
					.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0)
					: "", address.getLocality(), address.getCountryName());
			// Update the UI via a message handler.
			locationMsg.setAddressText(addressText);
			Message.obtain(mHandler, LocationConstants.UPDATE_ADDRESS,
					locationMsg).sendToTarget();
		}
		return null;
	}
}