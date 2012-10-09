package com.glue.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
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
		Intent intent = new Intent();
		intent.setClassName("com.glue.client.android",
				"com.glue.client.android.CreateStreamMainActivity");
		startActivity(intent);
	}
}
