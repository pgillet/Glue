package com.glue.client.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.glue.client.android.view.FlowLayout;

public class CreateStreamUserActivity extends Activity {

	static final int PICK_CONTACT_REQUEST = 0;
	private EditText editText;
	private FlowLayout contactList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_user);
		editText = (EditText) findViewById(R.id.editText1);
		contactList = (FlowLayout) findViewById(R.id.contactList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_user, menu);
		return true;
	}

	public void onClickToggle(View view) {
		ToggleButton toggle = (ToggleButton) view;
		boolean on = toggle.isChecked();
		TextView tv = (TextView) findViewById(R.id.textView1);
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
			Cursor cursor = getContentResolver().query(data.getData(),
					new String[] { Contacts.DISPLAY_NAME }, null, null, null);
			if (cursor.moveToFirst()) { // True if the cursor is not empty
				int columnIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
				String name = cursor.getString(columnIndex);

				Button button = new Button(this, null,
						android.R.attr.buttonStyleSmall);
				button.setText(name);
				contactList.addView(button);
			}
		}
	}
}
