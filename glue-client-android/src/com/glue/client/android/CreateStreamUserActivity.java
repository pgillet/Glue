package com.glue.client.android;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CreateStreamUserActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_user);
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
}
