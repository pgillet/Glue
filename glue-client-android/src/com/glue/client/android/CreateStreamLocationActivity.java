package com.glue.client.android;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.glue.client.android.dialog.DatePickerFragment;
import com.glue.client.android.dialog.TimeDialogListener;
import com.glue.client.android.dialog.TimePickerFragment;
import com.glue.client.android.utils.Utils;

public class CreateStreamLocationActivity extends FragmentActivity implements
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

}
