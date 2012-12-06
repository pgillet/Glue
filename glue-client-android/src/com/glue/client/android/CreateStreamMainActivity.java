package com.glue.client.android;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.glue.client.android.location.LocationActivity;
import com.glue.client.android.location.LocationConstants;
import com.glue.client.android.stream.StreamData;

public class CreateStreamMainActivity extends LocationActivity {

	private TextView tv;
	private TextView textViewTitle;
	private TextView textViewDescription;
	private ToggleButton toggleButtonPrivacy;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_main);
		tv = (TextView) findViewById(R.id.textView4);

		textViewTitle = (TextView) findViewById(R.id.editText1);
		textViewDescription = (TextView) findViewById(R.id.editText2);
		toggleButtonPrivacy = (ToggleButton) findViewById(R.id.toggleButton1);

		// Silent mode
		setGpsProviderAllowed(false);

		// For best location accuracy and because the
		// CreateStreamLocationActivity can be ignored, we start listening for
		// location updates when users begin creating the stream.
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				// The location may have be disabled just right when the handler
				// received an update
				if (isLocationEnabled()) {

					StreamData data = StreamData.getInstance();

					switch (msg.what) {
					case LocationConstants.UPDATE_ADDRESS:
						Address address = (Address) msg.obj;
						data.setLatitude(address.getLatitude());
						data.setLongitude(address.getLongitude());
						data.setAddress(formatAddress(address));
						break;

					default:
						Location location = (Location) msg.obj;
						data.setLatitude(location.getLatitude());
						data.setLongitude(location.getLongitude());
						break;
					}
				}
			}
		};
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

		Intent intent = new Intent();
		intent.setClassName(this, "com.glue.client.android.CreateStreamSummaryActivity");
		startActivity(intent);
	}

	public void onClickNext(View view) {
		collectStreamData();

		Intent intent = new Intent();
		intent.setClassName("com.glue.client.android", "com.glue.client.android.CreateStreamUserActivity");
		startActivity(intent);
	}

	private void collectStreamData() {
		StreamData data = StreamData.getInstance();
		data.setTitle(textViewTitle.getText().toString());

		// TODO manage tags ...
		// data.setDescription(textViewDescription.getText().toString());
		data.setPublicc(toggleButtonPrivacy.isChecked());
	}

	@Override
	public Handler getHandler() {
		return mHandler;
	}

}
