package com.example.sendfile;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UploadActivity extends Activity {

	protected Uri image = null;
	File fichier = new File("/sdcard/DCIM/Camera/photo.jpg");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button validbutton = (Button) findViewById(R.id.send);
		validbutton.setOnClickListener(send_listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_upload, menu);
		return true;
	}

	@Override
	protected void

	onPause() {
		super.onPause();
	}

	@Override
	protected void

	onStop() {
		Log.i(getClass().getSimpleName(), "on stop");

		super.onStop();
	}

	@Override
	protected void

	onDestroy() {
		super.onDestroy();
	}

	OnClickListener send_listener = new OnClickListener() {
		public void onClick(View v) {

			image = Uri.fromFile(fichier);
			Intent uploadIntent = new Intent();
			uploadIntent.setClassName("com.example.sendfile", "com.example.sendfile.HttpUploader");
			uploadIntent.setData(image);
			startService(uploadIntent);
		}
	};
}
