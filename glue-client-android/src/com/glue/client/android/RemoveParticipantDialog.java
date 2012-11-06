package com.glue.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RemoveParticipantDialog extends DialogFragment {
	public static final String PARTICIPANT_TO_REMOVE = "participantToRemove";

	private String emailAddress;
	private String name;

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface NoticeDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	static NoticeDialogListener mListener;

	/**
	 * Create a new instance of RemoveParticipantDialog, providing
	 * "emailAddress" and "name" as arguments.
	 */
	static RemoveParticipantDialog newInstance(String emailAddress, String name) {

		RemoveParticipantDialog f = new RemoveParticipantDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putStringArray(PARTICIPANT_TO_REMOVE, new String[] { emailAddress,
				name });
		f.setArguments(args);

		return f;
	}

	// Override the Fragment.onAttach() method to instantiate the
	// NoticeDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String[] args = getArguments().getStringArray(PARTICIPANT_TO_REMOVE);

		this.emailAddress = args[0];
		this.name = args[1];

		builder.setMessage(name + " (" + emailAddress + ")")
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								mListener
										.onDialogNegativeClick(RemoveParticipantDialog.this);
							}
						})
				.setPositiveButton(R.string.remove,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onDialogPositiveClick(RemoveParticipantDialog.this);
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}