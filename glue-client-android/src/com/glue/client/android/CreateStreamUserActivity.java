package com.glue.client.android;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.glue.client.android.view.FlowLayout;

public class CreateStreamUserActivity extends FragmentActivity implements
		OnItemClickListener, RemoveParticipantDialog.NoticeDialogListener {

	private static final String PICK_EMAIL = "pick_email";
	private static final String REMOVE_PARTICIPANT = "remove_participant";
	private static final String PARTICIPANTS = "participants";
	static final int PICK_CONTACT_REQUEST = 0;
	private FlowLayout contactList;
	private Bundle participants = new Bundle();

	private TextView tv;

	private boolean mShowInvisible;
	private AutoCompleteTextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_user);

		tv = (TextView) findViewById(R.id.textView1);
		contactList = (FlowLayout) findViewById(R.id.contactList);

		if (savedInstanceState != null) {
			Bundle participants = savedInstanceState.getBundle(PARTICIPANTS);

			Set<String> emailAddresses = participants.keySet();
			for (String emailAddress : emailAddresses) {
				String name = participants.getString(emailAddress);
				addContact(emailAddress, name);
			}
		}

		textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_contacts);
		populateContactList();

		textView.setOnItemClickListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle(PARTICIPANTS, participants);
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 * 
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts(String arg) {
		// Run query
		Uri uri = Data.CONTENT_URI;
		String[] projection = new String[] { Data._ID, Data.DISPLAY_NAME,
				Email.ADDRESS };
		// String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP +
		// " = '"
		// + (mShowInvisible ? "0" : "1") + "'";

		String selection = Data.MIMETYPE + "='" + Email.CONTENT_ITEM_TYPE + "'";
		String[] selectionArgs = null;
		if (arg != null) {
			// selectionArgs = new String[]{arg, arg}; // Messing up!
			selection += " AND (" + Data.DISPLAY_NAME + " LIKE '%" + arg + "%'"
					+ " OR " + Email.ADDRESS + " LIKE '%" + arg + "%')";
		}

		String sortOrder = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		return getContentResolver().query(uri, projection, selection,
				selectionArgs, sortOrder);
	}

	/**
	 * Populate the contact list based on account currently selected in the
	 * account spinner.
	 */
	private void populateContactList() {
		// Build adapter with contact entries
		Cursor cursor = getContacts(null);
		String[] fields = new String[] { Data.DISPLAY_NAME, Email.ADDRESS };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.contact_entry, cursor, fields, new int[] {
						R.id.contactEntryDisplayName,
						R.id.contactEntryEmailAddress });

		adapter.setCursorToStringConverter(new CursorToStringConverter() {

			@Override
			public String convertToString(android.database.Cursor cursor) {
				// Get the label for this row out of the "state" column
				final int columnIndex = cursor
						.getColumnIndexOrThrow(Data.DISPLAY_NAME);
				final String str = cursor.getString(columnIndex);
				return str;
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence constraint) {
				return getContacts(constraint.toString());
			}
		});

		textView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_user, menu);
		return true;
	}

	public void onClickToggle(View view) {
		ToggleButton toggle = (ToggleButton) view;
		boolean on = toggle.isChecked();

		Resources res = getResources();
		Drawable drawable = null;
		if (on) {
			drawable = res.getDrawable(R.drawable.social_add_group);
			tv.setText(R.string.open_description);
		} else {
			drawable = res.getDrawable(R.drawable.social_group);
			tv.setText(R.string.closed_description);
		}
		toggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable,
				null);
	}

	public void onClickAdd(View view) {
		// Create an intent to "pick" a contact, as defined by the content
		// provider URI
		Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// If the request went well (OK) and the request was
		// PICK_CONTACT_REQUEST
		if (resultCode == Activity.RESULT_OK
				&& requestCode == PICK_CONTACT_REQUEST) {
			// Perform a query to the contact's content provider for the
			// contact's name

			Uri result = data.getData();

			// get the contact id from the Uri
			String id = result.getLastPathSegment();

			Cursor cursor = getContentResolver().query(Email.CONTENT_URI, null,
					Email.CONTACT_ID + "=?", new String[] { id }, null);

			Bundle bundle = new Bundle();
			String displayName = null;
			while (cursor.moveToNext()) {
				int columnIndex = cursor.getColumnIndex(Email.ADDRESS);
				String emailAddress = cursor.getString(columnIndex);

				columnIndex = cursor.getColumnIndex(Email.TYPE);
				int type = cursor.getInt(columnIndex);

				columnIndex = cursor.getColumnIndex(Data.DISPLAY_NAME);
				displayName = cursor.getString(columnIndex);

				bundle.putInt(emailAddress, type);
			}

			EmailPickerDialog newFragment = EmailPickerDialog.newInstance(
					displayName, bundle);
			newFragment.show(getSupportFragmentManager().beginTransaction(),
					PICK_EMAIL, true);
		}
	}

	/**
	 * Adds a button with the given contact name above the auto-complete text
	 * view, and with the associated email address.
	 * 
	 * @param emailAddress
	 * @param name
	 */
	private void addContact(final String emailAddress, final String name) {

		participants.putString(emailAddress, name);

		Button button = new Button(this, null, android.R.attr.buttonStyleSmall);
		button.setText(name);
		button.setId(emailAddress.hashCode());

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment newFragment = RemoveParticipantDialog
						.newInstance(emailAddress, name);
				newFragment.show(getSupportFragmentManager(),
						REMOVE_PARTICIPANT);
			}
		});

		contactList.addView(button);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Cursor cursor = (Cursor) arg0.getItemAtPosition(arg2);

		int columnIndex = cursor.getColumnIndex(Email.ADDRESS);
		String emailAddress = cursor.getString(columnIndex);

		columnIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
		String name = cursor.getString(columnIndex);

		addContact(emailAddress, name);

		// Clear
		textView.setText(null);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// Nothing to do
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {

		RemoveParticipantDialog f = (RemoveParticipantDialog) dialog;
		int id = f.getEmailAddress().hashCode();

		for (int i = 0; i < contactList.getChildCount(); i++) {
			View view = contactList.getChildAt(i);
			if (id == view.getId()) {
				contactList.removeView(view);
				participants.remove(f.getEmailAddress());
			}
		}
	}

	public void onClickHelp(View view) {
		// TODO
	}

	public void onClickFinish(View view) {
		// TODO
	}

	public void onClickNext(View view) {
		// TODO
	}

}