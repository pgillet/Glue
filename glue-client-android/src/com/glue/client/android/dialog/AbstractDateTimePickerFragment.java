package com.glue.client.android.dialog;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.support.v4.app.DialogFragment;

public class AbstractDateTimePickerFragment extends DialogFragment {

	protected static TimeDialogListener mListener;
	protected Calendar calendar;

	public AbstractDateTimePickerFragment() {
		super();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (TimeDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

}