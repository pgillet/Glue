package com.glue.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.glue.client.android.stream.StreamData;

public class CreateStreamMainActivity extends Activity {

	private TextView tv;
	private TextView textViewTitle;
	private TextView textViewDescription;
	private ToggleButton toggleButtonPrivacy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_main);
		tv = (TextView) findViewById(R.id.textView4);

		textViewTitle = (TextView) findViewById(R.id.editText1);
		textViewDescription = (TextView) findViewById(R.id.editText2);
		toggleButtonPrivacy = (ToggleButton) findViewById(R.id.toggleButton1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_main, menu);
		return true;
	}

	public void onClickToggle(View view) {

		ToggleButton toggle = (ToggleButton) view;
		boolean on = toggle.isChecked();

		if (on) {
			tv.setText(R.string.public_description);
		} else {
			tv.setText(R.string.private_description);
		}
	}

	public void onClickFinish(View view) {
		collectStreamData();
	}

	public void onClickNext(View view) {
		collectStreamData();

		Intent intent = new Intent();
		intent.setClassName("com.glue.client.android",
				"com.glue.client.android.CreateStreamUserActivity");
		startActivity(intent);
	}

	private void collectStreamData() {
		StreamData data = StreamData.getInstance();
		data.setTitle(textViewTitle.getText().toString());
		data.setDescription(textViewDescription.getText().toString());
		data.setPublic(toggleButtonPrivacy.isChecked());
	}

}
