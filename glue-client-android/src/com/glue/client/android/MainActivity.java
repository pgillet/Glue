package com.glue.client.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.actionbarcompat.ActionBarActivity;
import com.glue.client.android.authenticator.AuthenticatorActivity;
import com.glue.client.android.authenticator.Constants;

public class MainActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check if a Glue account has been added
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
		if (accounts.length == 0) {
			final Intent intent = new Intent(this, AuthenticatorActivity.class);
			startActivity(intent);
			return;
		}
		setContentView(R.layout.activity_main);
		setTitle(R.string.app_name);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_content_new:
			Intent intent = new Intent();
			intent.setClassName("com.glue.client.android", "com.glue.client.android.CreateStreamMainActivity");
			startActivity(intent);
			break;

		case R.id.menu_content_import_export:
			Toast.makeText(this, "Tapped Import-Export", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_search:
			onSearchRequested();
			break;

		}
		return super.onOptionsItemSelected(item);
	}
}
