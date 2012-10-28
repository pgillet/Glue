package com.glue.client.android;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.glue.client.android.dialog.DatePickerFragment;
import com.glue.client.android.dialog.TimePickerFragment;

public class CreateStreamLocationActivity extends FragmentActivity {

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
		buttonFromDate.setText(DateFormat.format(datePattern, from));

		buttonFromTime = (Button) findViewById(R.id.buttonFromTime);
		buttonFromTime.setText(DateFormat.format(timePattern, from));

		buttonToDate = (Button) findViewById(R.id.buttonToDate);
		buttonToDate.setText(DateFormat.format(datePattern, to));

		buttonToTime = (Button) findViewById(R.id.buttonToTime);
		buttonToTime.setText(DateFormat.format(timePattern, to));
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

}
