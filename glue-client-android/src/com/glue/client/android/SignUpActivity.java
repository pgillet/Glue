package com.glue.client.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.struct.IUser;

public class SignUpActivity extends Activity {

	/**
	 * Represents an asynchronous task used to register a new user.
	 */
	public class UserSignUpTask extends AsyncTask<String, Void, IUser> {

		private Exception ex;

		@Override
		protected IUser doInBackground(String... args) {

			IUser res = null;
			try {
				Glue glue = new GlueFactory().getInstance();
				res = glue.createUser(args[0], args[1], args[2], args[3]);
			} catch (Exception ex) {
				Log.e(TAG, "UserSignUpTask.doInBackground: failed to register");
				Log.i(TAG, ex.toString());
				this.ex = ex;
				return null;
			}

			return res;
		}

		@Override
		protected void onCancelled() {
			// If the action was canceled (by the user clicking the cancel
			// button in the progress dialog), then call back into the
			// activity to let it know.
			// onAuthenticationCancel();
		}

		@Override
		protected void onPostExecute(final IUser createdUser) {
			// On a successful registration, call back into the Activity to
			// communicate the created user (or null for an error).
			onRegistrationResult(createdUser);
		}

	}
	/** The tag used to log to adb console. */
	private static final String TAG = "SignUpActivity";
	private EditText mFirstNameEdit;
	private EditText mLastNameEdit;

	private EditText mMailAddressEdit;

	private EditText mPasswordEdit;

	/** Keep track of the progress dialog so we can dismiss it */
	private ProgressDialog mProgressDialog = null;

	/** Keep track of the sign up task so we can cancel it if requested */
	private UserSignUpTask mSignUpTask = null;

	/**
	 * Hides the progress UI for a lengthy operation.
	 */
	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	public void onClickSignUp(View view) {
		String firstName = mFirstNameEdit.getText().toString();
		String lastName = mLastNameEdit.getText().toString();
		String mailAddress = mMailAddressEdit.getText().toString();
		String password = mPasswordEdit.getText().toString();

		// Form validation
		if (TextUtils.isEmpty(firstName)) {
			final CharSequence msg = getText(R.string.required_field);
			mFirstNameEdit.setError(msg);
			return;
		}
		if (TextUtils.isEmpty(lastName)) {
			final CharSequence msg = getText(R.string.required_field);
			mLastNameEdit.setError(msg);
			return;
		}
		if (!Patterns.EMAIL_ADDRESS.matcher(mailAddress).matches()) {
			final CharSequence msg = getText(R.string.email_address_type_field);
			mMailAddressEdit.setError(msg);
			return;
		}
		if (TextUtils.isEmpty(password)) {
			final CharSequence msg = getText(R.string.required_field);
			mPasswordEdit.setError(msg);
			return;
		}

		// Show a progress dialog, and kick off a background task to perform
		// the user login attempt.
		showProgress();
		mSignUpTask = new UserSignUpTask();
		mSignUpTask.execute(firstName, lastName, mailAddress, password);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		mFirstNameEdit = (EditText) findViewById(R.id.first_name_edit);
		mLastNameEdit = (EditText) findViewById(R.id.last_name_edit);
		mMailAddressEdit = (EditText) findViewById(R.id.mail_address_edit);
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

	/**
	 * Called when the registration process completes.
	 * 
	 * @param createdUser
	 *            the created user returned by the server, or NULL if
	 *            registration failed.
	 */
	public void onRegistrationResult(IUser createdUser) {

		boolean success = (createdUser != null);
		Log.i(TAG, "onRegistrationResult(" + success + ")");

		// Our task is complete, so clear it out
		mSignUpTask = null;

		// Hide the progress dialog
		hideProgress();

		CharSequence text = null;
		if (success) {
			text = "Welcome to Glue " + createdUser.getFirstName() + " "
					+ createdUser.getLastName() + "!";
		} else {
			text = "Error";
		}

		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public void onSignUpCancel() {
		Log.i(TAG, "onSignUpCancel()");

		// Our task is complete, so clear it out
		mSignUpTask = null;

		// Hide the progress dialog
		hideProgress();
	}

	/**
	 * Shows the progress UI for a lengthy operation.
	 */
	private void showProgress() {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getText(R.string.signing_up));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Log.i(TAG, "user cancelling registration");
				if (mSignUpTask != null) {
					mSignUpTask.cancel(true);
				}
			}
		});
		// We save off the progress dialog in a field so that we can dismiss
		// it later. We can't just call dismissDialog(0) because the system
		// can lose track of our dialog if there's an orientation change.
		mProgressDialog = dialog;
		mProgressDialog.show();
	}
}
