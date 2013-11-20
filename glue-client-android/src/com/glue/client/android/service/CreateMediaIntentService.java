package com.glue.client.android.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.client.android.intent.IntentParams;
import com.glue.client.android.receiver.CreateMediaReceiver;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;
import com.glue.struct.impl.Stream;

public class CreateMediaIntentService extends IntentService {

	public CreateMediaIntentService() {
		super("UploadIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Retrieve parameters
		Long streamId = intent.getExtras().getLong(IntentParams.PARAM_STREAM_ID);
		String media = intent.getStringExtra(IntentParams.PARAM_MEDIA_TYPE);
		String title = intent.getStringExtra(IntentParams.PARAM_MEDIA_TITLE);
		String path = intent.getStringExtra(IntentParams.PARAM_MEDIA_PATH);
		String ext = intent.getStringExtra(IntentParams.PARAM_MEDIA_EXT);

		// Dummy stream
		IStream stream = new Stream();
		stream.setId(streamId);

		// Upload media
		uploadMedia(stream, media, title, ext, path);

		// Broadcast the result
		Intent myIntent = new Intent(CreateMediaReceiver.ACTION_RESP);
		LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);
	}

	private void uploadMedia(IStream stream, String media, String title, String ext, String path) {
		File file = null;
		Glue glue = new GlueFactory().getInstance();

		if (path != null) {
			Uri uri = Uri.parse(path);
			try {
				file = new File(new URI(uri.toString()));
			} catch (URISyntaxException e) {
				file = null;
			}
		}

		try {
			glue.createMedia(stream, title, ext, media, null, null, null, file);
		} catch (GlueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}