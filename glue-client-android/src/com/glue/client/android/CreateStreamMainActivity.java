package com.glue.client.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CreateStreamMainActivity extends Activity {

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_main);
		tv = (TextView) findViewById(R.id.textView4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_main, menu);
		return true;
	}

	public void onClickToggle(View view) {

		ToggleButton toggle = (ToggleButton) view;
		boolean on = toggle.isChecked();
		
		Resources res = getResources();
		Drawable drawable = null;
		if (on) {
			drawable = res.getDrawable(R.drawable.device_access_not_secure);
			tv.setText(R.string.public_description);
		} else {
			drawable = res.getDrawable(R.drawable.device_access_secure);
			tv.setText(R.string.private_description);
		}
		toggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable,
				null);
	}

	public void onClickFinish(View view) {

	}

	public void onClickNext(View view) {
		Intent intent = new Intent();
		intent.setClassName("com.glue.client.android",
				"com.glue.client.android.CreateStreamUserActivity");
		startActivity(intent);
	}
}
