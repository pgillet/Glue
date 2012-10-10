package com.glue.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CreateStreamMainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_main, menu);
		return true;
	}

	public void onToggleClicked(View view) {

		ToggleButton toggle = (ToggleButton) view;
		boolean on = toggle.isChecked();
		TextView tv = (TextView) findViewById(R.id.textView4);

		if (on) {
			tv.setText(R.string.public_description);
		} else {
			tv.setText(R.string.private_description);
		}
	}
}
