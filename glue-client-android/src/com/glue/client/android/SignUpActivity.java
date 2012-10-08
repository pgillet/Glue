package com.glue.client.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class SignUpActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		Account[] accounts = AccountManager.get(this).getAccounts();

		if (accounts.length > 0) {
			Account account = accounts[0];

			final TextView tx = (TextView) findViewById(R.id.editText3);
			tx.setText(account.name);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sign_up, menu);
		return true;
	}
}
