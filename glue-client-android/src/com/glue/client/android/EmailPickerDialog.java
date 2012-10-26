package com.glue.client.android;

import java.util.Set;

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

	private boolean mRemoved;

	private int mBackStackId;

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

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		displayName = getArguments().getString(Data.DISPLAY_NAME);
		Bundle emailAddresses = getArguments().getBundle(EMAIL_ADDRESSES);

		Set<String> keys = emailAddresses.keySet();

		builder.setTitle(displayName).setItems(
				keys.toArray(new String[keys.size()]),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item
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

}
