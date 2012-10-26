package com.glue.client.android;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

public class EmailPickerDialog extends DialogFragment {

	private static final String EMAIL_ADDRESSES = "email_addresses";

	private String displayName;

	private String selectedItem;

	private boolean mRemoved;

	private int mBackStackId;

	/**
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface NoticeDialogListener {
		/**
		 * Called when the user selected an item from the list
		 * 
		 * @param dialog
		 */
		public void onSelectedItem(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	static NoticeDialogListener mListener;

	/**
	 * Create a new instance of RemoveParticipantDialog, providing
	 * "emailAddress" and "name" as arguments.
	 */
	static EmailPickerDialog newInstance(String displayName,
			Bundle emailAddresses) {

		EmailPickerDialog f = new EmailPickerDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString(Data.DISPLAY_NAME, displayName);
		args.putBundle(EMAIL_ADDRESSES, emailAddresses);
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

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		displayName = getArguments().getString(Data.DISPLAY_NAME);
		Bundle emailAddresses = getArguments().getBundle(EMAIL_ADDRESSES);

		Set<String> keys = emailAddresses.keySet();
		final String[] items = keys.toArray(new String[keys.size()]);

		builder.setTitle(displayName).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item
						setSelectedItem(items[which]);
						mListener.onSelectedItem(EmailPickerDialog.this);
					}
				});
		return builder.create();
	}

	@Override
	public int show(FragmentTransaction transaction, String tag) {
		return show(transaction, tag, false);
	}

	/**
	 * Issue with show DialogFragment from onActivityResult
	 * 
	 * @see http
	 *      ://stackoverflow.com/questions/10114324/show-dialogfragment-from-
	 *      onactivityresult
	 * @see http://code.google.com/p/android/issues/detail?id=23761
	 * @see http://code.google.com/p/android/issues/detail?id=17787
	 * 
	 * @param transaction
	 * @param tag
	 * @param allowStateLoss
	 * @return
	 */
	public int show(FragmentTransaction transaction, String tag,
			boolean allowStateLoss) {
		transaction.add(this, tag);
		mRemoved = false;
		mBackStackId = allowStateLoss ? transaction.commitAllowingStateLoss()
				: transaction.commit();
		return mBackStackId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

}
