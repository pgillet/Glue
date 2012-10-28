package com.glue.client.android.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends AbstractDateTimePickerFragment implements
		TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		mListener.onTimeSet(TimePickerFragment.this);
	}

}
