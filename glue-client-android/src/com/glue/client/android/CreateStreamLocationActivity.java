package com.glue.client.android;

import java.util.Calendar;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import com.glue.client.android.dialog.DatePickerFragment;
import com.glue.client.android.dialog.TimeDialogListener;
import com.glue.client.android.dialog.TimePickerFragment;
import com.glue.client.android.location.LocationActivity;
import com.glue.client.android.location.SimpleLocation;
import com.glue.client.android.utils.Utils;

public class CreateStreamLocationActivity extends LocationActivity implements
		TimeDialogListener {

	private static final int DEFAULT_STREAM_LENGTH = 2;
	private static final String TIME_PICKER = "timePicker";
	private static final String DATE_PICKER = "datePicker";

	private Calendar from;
	private Calendar to;

	private String datePattern;
	private String timePattern;

	private Button buttonFromDate;
	private Button buttonFromTime;

	private Button buttonToDate;
	private Button buttonToTime;

	private CheckBox checkBoxFrom;
	private CheckBox checkBoxTo;

	private ViewGroup layoutFrom;
	private ViewGroup layoutTo;

	private ProgressBar locationProgressBar;
	private TextView address;
	private SimpleLocation location;
	private Button locationMap;
	private Button locationSwitch;

	private Handler mHandler;

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
		address = (TextView) findViewById(R.id.address);
		locationMap = (Button) findViewById(R.id.locationMapButton);
		locationSwitch = (Button) findViewById(R.id.toggleButton1);

		// Handler for updating text fields on the UI like the lat/long and
		// address.
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				// The location may have be disabled just right when the handler
				// received an update
				if (isLocationEnabled()) {

					location = (SimpleLocation) msg.obj;
					switch (msg.what) {
					case UPDATE_ADDRESS:
						if (location.getAddressText() == null) {
							Toast.makeText(CreateStreamLocationActivity.this,
									getString(R.string.address_not_available),
									Toast.LENGTH_SHORT).show();
						}
						updateAddressTextView();
						break;
					case UPDATE_LATLNG:
						if (!isGeocoderAvailable()
						/* || !isReverseGeocodingEnabled() */) {
							updateAddressTextView();
						}
						break;
					}
				}
			}
		};

		// Set up location.
		setLocationEnabled(true);
	}

	private void updateAddressTextView() {
		String text = null;
		if (location.getAddressText() != null) {
			text = location.getAddressText();
		} else {
			text = "Lat: " + location.getLatitude() + ", Long: "
					+ location.getLongitude();
		}
		address.setText(text);
		address.setVisibility(View.VISIBLE);
		locationProgressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_location, menu);
		return true;
	}

	public void onClickFromDate(View v) {
		showDatePickerDialog(from);
	}

	public void onClickFromTime(View v) {
		showTimePickerDialog(from);
	}

	public void onClickToDate(View v) {
		showDatePickerDialog(to);
	}

	public void onClickToTime(View v) {
		showTimePickerDialog(to);
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
	public Handler getHandler() {
		return mHandler;
	}

	@Override
	protected void setLocationEnabled(boolean b) {
		super.setLocationEnabled(b);

		Resources res = getResources();
		Drawable drawable = null;

		if (isLocationEnabled()) {
			// Switch on
			locationProgressBar.setVisibility(View.VISIBLE);
			address.setText(R.string.unknown);
			address.setVisibility(View.INVISIBLE);
			drawable = res.getDrawable(R.drawable.device_access_location_found);
		} else {
			// Switch off
			locationProgressBar.setVisibility(View.GONE);
			address.setText(R.string.none);
			address.setVisibility(View.VISIBLE);
			drawable = res.getDrawable(R.drawable.device_access_location_off);
		}

		locationMap.setEnabled(isLocationEnabled());
		locationSwitch.setCompoundDrawablesWithIntrinsicBounds(null, null,
				drawable, null);
	}

	public void onClickFinish(View v) {
		// TODO
	}

	public void onClickLocationMap(View v) {
		Intent intent = new Intent();
		intent.setClassName(this,
				"com.glue.client.android.location.LocationPickerMapActivity");
		startActivity(intent);
	}

	public void onClickToggle(View v) {
		// Cannot use a ToggleButton here as it is not well rendered when there
		// is just an icon to display without text.
		// The checked state is given by the locationEnabled boolean
		// property.

		// Switch state
		setLocationEnabled(!isLocationEnabled());
	}
}
