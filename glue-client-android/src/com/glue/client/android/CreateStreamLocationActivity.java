package com.glue.client.android;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.glue.client.android.dialog.DatePickerFragment;
import com.glue.client.android.dialog.TimePickerFragment;

public class CreateStreamLocationActivity extends FragmentActivity {

	private static final String TIME_PICKER = "timePicker";
	private static final String DATE_PICKER = "datePicker";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_location);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_location, menu);
		return true;
	}

	public void onClickFromDate(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), DATE_PICKER);
	}

	public void onClickFromTime(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), TIME_PICKER);
	}

}
