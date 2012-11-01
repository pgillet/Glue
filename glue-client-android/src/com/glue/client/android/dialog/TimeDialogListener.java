package com.glue.client.android.dialog;

import android.support.v4.app.DialogFragment;

/**
 * The activity that creates an instance of Date or TimePickerFragment must
 * implement this interface in order to receive event callbacks. Each method
 * passes the DialogFragment in case the host needs to query it.
 */
public interface TimeDialogListener {
	/**
	 * Called when the user set the time from a Date or a TimePicker
	 * 
	 * @param dialog
	 */
	public void onTimeSet(DialogFragment dialog);
}