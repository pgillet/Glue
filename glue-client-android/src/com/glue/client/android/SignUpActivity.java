package com.glue.client.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.client.android.authenticator.AuthenticatorActivity;

public class SignUpActivity extends AuthenticatorActivity {

	public class UserSignUpTask extends AuthenticatorActivity.UserLoginTask {
		@Override
		protected String doInBackground(String... params) {

			try {
				Glue glue = new GlueFactory().getInstance();
				glue.createUser(params[0], params[1], params[2]);
				
				// There is no need to log in as we just created the user
				// identity on the target server.
				// We just register the user credentials for subsequent requests
				// instead.
				glue.registerCredentials(params[1], params[2]);
				final String authToken = "Dummy";
				return authToken;

				// return super.doInBackground(params[2], params[3]);
			} catch (Exception ex) {
				Log.e(TAG, "UserSignUpTask.doInBackground: failed to register");
				Log.i(TAG, ex.toString());
				this.ex = ex;
				return null;
			}
		}
	}

	/** The tag used to log to adb console. */
	private static final String TAG = "SignUpActivity";

	private EditText mNameEdit;

	@Override
	public void finish() {
		super.finish();
		Intent intent = new Intent();
		intent.setClassName(this, "com.glue.client.android.MainActivity");
		startActivity(intent);
	}

	public void onClickSignUp(View view) {
		String name = mNameEdit.getText().toString();
		mUsername = mUsernameEdit.getText().toString();
		mPassword = mPasswordEdit.getText().toString();

		// Form validation
		if (TextUtils.isEmpty(name)) {
			final CharSequence msg = getText(R.string.required_field);
			mNameEdit.setError(msg);
			return;
		}
		if (!Patterns.EMAIL_ADDRESS.matcher(mUsername).matches()) {
			final CharSequence msg = getText(R.string.email_address_type_field);
			mUsernameEdit.setError(msg);
			return;
		}
		if (TextUtils.isEmpty(mPassword)) {
			final CharSequence msg = getText(R.string.required_field);
			mPasswordEdit.setError(msg);
			return;
		}

		// Show a progress dialog, and kick off a background task to perform
		// the user login attempt.
		showProgress();
		mAuthTask = new UserSignUpTask();
		mAuthTask.execute(name, mUsername, mPassword);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		mNameEdit = (EditText) findViewById(R.id.name_edit);
		mUsernameEdit = (EditText) findViewById(R.id.mail_address_edit);
		mPasswordEdit = (EditText) findViewById(R.id.password_edit);

		Account[] accounts = AccountManager.get(this).getAccounts();

		if (accounts.length > 0) {
			Account account = accounts[0];

			final TextView tx = (TextView) findViewById(R.id.mail_address_edit);
			tx.setText(account.name);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sign_up, menu);
		return true;
	}

}
