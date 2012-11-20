package com.glue.client.android;

import java.util.Calendar;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.glue.client.android.dialog.DatePickerFragment;
import com.glue.client.android.dialog.TimeDialogListener;
import com.glue.client.android.dialog.TimePickerFragment;
import com.glue.client.android.location.LocationActivity;
import com.glue.client.android.location.LocationConstants;
import com.glue.client.android.stream.StreamData;
import com.glue.client.android.utils.Utils;

public class CreateStreamLocationActivity extends LocationActivity implements
		TimeDialogListener {

	private static final String DATE_PICKER = "datePicker";
	private static final int DEFAULT_STREAM_LENGTH = 2;
	private static final int PICK_LOCATION_REQUEST = 0;
	private static final String TIME_PICKER = "timePicker";

	private Button buttonFromDate;
	private Button buttonFromTime;

	private Button buttonToDate;
	private Button buttonToTime;

	private CheckBox checkBoxFrom;
	private CheckBox checkBoxTo;

	private String datePattern;
	private Calendar from;

	private ViewGroup layoutFrom;
	private ViewGroup layoutTo;

	private Button locationMap;
	private ProgressBar locationProgressBar;

	private Address mAddress;
	private Handler mHandler;
	private Location mLocation;
	private TextView textViewAddress;

	private String timePattern;
	private Calendar to;
	private ToggleButton toggleLocation;

	private void collectStreamData() {
		StreamData data = StreamData.getInstance();
		data.setStartDate(from.getTime().getTime());
		if (checkBoxTo.isChecked()) {
			data.setEndDate(to.getTime().getTime());
		}
		if (isLocationEnabled()) {
			if (mAddress != null) {
				data.setAddress(formatAddress(mAddress));
				data.setLatitude(mAddress.getLatitude());
				data.setLongitude(mAddress.getLongitude());
			} else if (mLocation != null) {
				data.setLatitude(mLocation.getLatitude());
				data.setLongitude(mLocation.getLongitude());
			}
		}
	}

	@Override
	public Handler getHandler() {
		return mHandler;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_LOCATION_REQUEST) {
			if (resultCode == RESULT_OK) {
				// A location was picked

				Address address = data
						.getParcelableExtra(LocationConstants.ADDRESS);
				if (address == null) {
					Location location = data
							.getParcelableExtra(LocationConstants.LOCATION);
					doReverseGeocoding(location);
				} else {
					updateAddress(address);
				}

			} else {
				// No location was picked.
				// Pick the current location again.
				super.setLocationEnabled(true);
			}
		}
	}

	public void onClickCheckBoxFrom(View v) {
		boolean checked = checkBoxFrom.isChecked();
		Utils.setEnabled(checked, layoutFrom, checkBoxFrom.getId());
		checkBoxTo.setEnabled(checked);
		if (!checked) {
			checkBoxTo.setChecked(checked);
			onClickCheckBoxTo(null);
		}
	}

	public void onClickCheckBoxTo(View v) {
		boolean checked = checkBoxTo.isChecked();
		Utils.setEnabled(checked, layoutTo, checkBoxTo.getId());
	}

	public void onClickFinish(View v) {
		collectStreamData();
		
		Intent intent = new Intent();
		intent.setClassName(this,
				"com.glue.client.android.CreateStreamSummaryActivity");
		startActivity(intent);
	}

	public void onClickFromDate(View v) {
		showDatePickerDialog(from);
	}

	public void onClickFromTime(View v) {
		showTimePickerDialog(from);
	}

	public void onClickLocationMap(View v) {
		super.setLocationEnabled(false);

		Intent intent = new Intent();
		intent.setClassName(this,
				"com.glue.client.android.location.LocationPickerMapActivity");
		startActivityForResult(intent, PICK_LOCATION_REQUEST);
	}

	public void onClickToDate(View v) {
		showDatePickerDialog(to);
	}

	public void onClickToggle(View v) {
		// Switch state
		setLocationEnabled(toggleLocation.isChecked());
	}

	public void onClickToTime(View v) {
		showTimePickerDialog(to);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_location);

		from = Calendar.getInstance(); // Locale?
		to = (Calendar) from.clone();
		to.add(Calendar.HOUR_OF_DAY, DEFAULT_STREAM_LENGTH);

		datePattern = getString(R.string.date_pattern);
		timePattern = getString(R.string.time_pattern);

		buttonFromDate = (Button) findViewById(R.id.buttonFromDate);
		buttonFromTime = (Button) findViewById(R.id.buttonFromTime);
		buttonToDate = (Button) findViewById(R.id.buttonToDate);
		buttonToTime = (Button) findViewById(R.id.buttonToTime);

		checkBoxFrom = (CheckBox) findViewById(R.id.checkBoxFrom);
		checkBoxTo = (CheckBox) findViewById(R.id.checkBoxTo);

		// Set the initial text to Date & Time text views
		onTimeSet(null);

		layoutFrom = (ViewGroup) findViewById(R.id.layoutFrom);
		layoutTo = (ViewGroup) findViewById(R.id.layoutTo);

		// Set the initial enabled state of both layouts, according to the
		// default checked value of their respective checkbox
		onClickCheckBoxFrom(null);
		onClickCheckBoxTo(null);

		// Location section
		locationProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textViewAddress = (TextView) findViewById(R.id.address);
		locationMap = (Button) findViewById(R.id.locationMapButton);
		toggleLocation = (ToggleButton) findViewById(R.id.toggleButton1);

		// Handler for updating text fields on the UI like the lat/long and
		// address.
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				// The location may have be disabled just right when the handler
				// received an update
				if (isLocationEnabled()) {
					switch (msg.what) {
					case LocationConstants.UPDATE_ADDRESS:
						Address address = (Address) msg.obj;
						updateAddress(address);
						break;

					case LocationConstants.UPDATE_LATLNG:
						Location location = (Location) msg.obj;
						updateLocation(location);

						if (!isGeocoderAvailable()
						/* || !isReverseGeocodingEnabled() */) {
							Toast.makeText(CreateStreamLocationActivity.this,
									getString(R.string.address_not_available),
									Toast.LENGTH_SHORT).show();
						}
						break;

					case LocationConstants.ADDRESS_NOT_FOUND:
						Location loc = (Location) msg.obj;
						updateLocation(loc);

						Toast.makeText(CreateStreamLocationActivity.this,
								getString(R.string.address_not_available),
								Toast.LENGTH_SHORT).show();
						break;

					}
				}
			}
		};

		// Set up location.
		setLocationEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_location, menu);
		return true;
	}

	@Override
	public void onTimeSet(DialogFragment dialog) {
		// One of both Calendar instances has changed
		// Refresh all the Date & Time text views

		buttonFromDate.setText(DateFormat.format(datePattern, from));
		buttonFromTime.setText(DateFormat.format(timePattern, from));
		buttonToDate.setText(DateFormat.format(datePattern, to));
		buttonToTime.setText(DateFormat.format(timePattern, to));
	}

	@Override
	protected void setLocationEnabled(boolean b) {
		super.setLocationEnabled(b);

		if (isLocationEnabled()) {
			// Switch on
			locationProgressBar.setVisibility(View.VISIBLE);
			textViewAddress.setText(R.string.unknown);
			textViewAddress.setVisibility(View.INVISIBLE);
		} else {
			// Switch off
			locationProgressBar.setVisibility(View.GONE);
			textViewAddress.setText(R.string.none);
			textViewAddress.setVisibility(View.VISIBLE);
		}
		locationMap.setEnabled(isLocationEnabled());
	}

	/**
	 * Shows the date picker.
	 * 
	 * @param c
	 */
	private void showDatePickerDialog(Calendar c) {
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.setCalendar(c);
		newFragment.show(getSupportFragmentManager(), DATE_PICKER);
	}

	/**
	 * Shows the time picker.
	 * 
	 * @param c
	 */
	private void showTimePickerDialog(Calendar c) {
		TimePickerFragment newFragment = new TimePickerFragment();
		newFragment.setCalendar(c);
		newFragment.show(getSupportFragmentManager(), TIME_PICKER);
	}

	private void updateAddress(Address address) {
		mAddress = address;
		String text = formatAddress(address);
		updateAddressTextView(text);
	}

	private void updateAddressTextView(String text) {
		textViewAddress.setText(text);
		textViewAddress.setVisibility(View.VISIBLE);
		locationProgressBar.setVisibility(View.GONE);
	}

	private void updateLocation(Location location) {
		mLocation = location;
		String text = "Lat: " + location.getLatitude() + ", Long: "
				+ location.getLongitude();
		updateAddressTextView(text);
	}
}
