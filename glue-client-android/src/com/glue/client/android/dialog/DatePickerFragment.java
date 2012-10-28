package com.glue.client.android.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends AbstractDateTimePickerFragment
		implements DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		mListener.onTimeSet(DatePickerFragment.this);
	}
}
