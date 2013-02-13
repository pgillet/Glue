package com.glue.client.android;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.actionbarcompat.ActionBarActivity;
import com.glue.client.android.intent.IntentParams;
import com.glue.client.android.receiver.CreateMediaReceiver;
import com.glue.client.android.service.CreateMediaIntentService;

public class StreamDetailActivity extends ActionBarActivity {

	private static final int REQUEST_CAMERA_CODE = 100;
	private BroadcastReceiver mMessageReceiver = new CreateMediaReceiver();
	private Long streamID;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stream_detail);

		// Register to receive messages.
		// We are registering an observer (mMessageReceiver) to receive Intents
		// with actions named "custom-event-name".
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
				new IntentFilter(CreateMediaReceiver.ACTION_RESP));
		setTitle(R.string.app_name);

		// Get the stream ID
		streamID = this.getIntent().getExtras().getLong(IntentParams.PARAM_STREAM_ID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_stream_detail, menu);
		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_content_edit:

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			input.setHeight(50);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					createText(input.getText().toString());
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});

			alert.show();
			break;

		case R.id.menu_device_access_camera:
			Toast.makeText(this, "Tapped camera", Toast.LENGTH_SHORT).show();
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				fileUri = getOutputMediaFile();
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case R.id.menu_device_access_mic:
			Toast.makeText(this, "Tapped mic", Toast.LENGTH_SHORT).show();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	protected void createText(String text) {
		Intent intent = new Intent(this, CreateMediaIntentService.class);
		intent.putExtra(IntentParams.PARAM_STREAM_ID, streamID);
		intent.putExtra(IntentParams.PARAM_MEDIA_TYPE, "Text");
		intent.putExtra(IntentParams.PARAM_MEDIA_TITLE, text);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CAMERA_CODE) {
			if (resultCode == RESULT_OK) {
				if (data == null) {

					// The picture was taken but not returned
					Toast.makeText(getApplicationContext(),
							"The picture was taken and is located here: " + fileUri.toString(), Toast.LENGTH_LONG)
							.show();

					createPicture(fileUri.toString());

				} else {
					// The picture was returned
					Bundle extras = data.getExtras();
					// ImageView imageView1 = (ImageView)
					// findViewById(R.id.imageView1);
					// imageView1.setImageBitmap((Bitmap) extras.get("data"));
				}
			}
		}
	}

	private void createPicture(String path) {
		Intent intent = new Intent(this, CreateMediaIntentService.class);
		intent.putExtra(IntentParams.PARAM_STREAM_ID, streamID);
		intent.putExtra(IntentParams.PARAM_MEDIA_TYPE, "Picture");
		intent.putExtra(IntentParams.PARAM_MEDIA_TITLE, "Picture title ...");
		intent.putExtra(IntentParams.PARAM_MEDIA_EXT, "jpg");
		intent.putExtra(IntentParams.PARAM_MEDIA_PATH, path);
		startService(intent);

	}

	protected Uri getOutputMediaFile() throws IOException {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Glue");

		// Create a media file name
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + streamID + ".jpg");

		if (mediaFile.exists() == false) {
			mediaFile.getParentFile().mkdirs();
			mediaFile.createNewFile();
		}
		return Uri.fromFile(mediaFile);
	}

}
