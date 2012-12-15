package com.glue.client.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.exceptions.GlueException;

public class LoginActivity extends Activity {

	private TextView usernameView;
	private TextView passwordView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		usernameView = (TextView) findViewById(R.id.editText3);
		passwordView = (TextView) findViewById(R.id.editText2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void onClickSignUp(View v) {
		Intent intent = new Intent();
		intent.setClassName("com.glue.client.android",
				"com.glue.client.android.SignUpActivity");
		startActivity(intent);
	}

	public void onClickLogin(View v) {

		String username = usernameView.getText().toString();
		String password = passwordView.getText().toString();
		(new LoginTask()).execute(username, password);
	}

	// AsyncTask
	private class LoginTask extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				LoginActivity.this);
		private Glue glue = new GlueFactory().getInstance();

		public LoginTask() {
			super();
		}

		// can use UI thread here
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Log in...");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;

			// Login ...
			try {
				glue.login(params[0], params[1]);
			} catch (GlueException e) {
				return e.getMessage();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (result != null) {
				Toast.makeText(LoginActivity.this, result,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(LoginActivity.this, "Logged in",
						Toast.LENGTH_LONG).show();

				Intent intent = new Intent();
				intent.setClassName("com.glue.client.android",
						"com.glue.client.android.MainActivity");
				startActivity(intent);
			}
		}
	}

}
