package com.glue.client.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CreateMediaReceiver extends BroadcastReceiver {
	public static final String ACTION_RESP = "com.glue.client.android.receiver.MESSAGE_PROCESSED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// String text =
		// intent.getStringExtra(UploadIntentService.PARAM_OUT_MSG);
		Toast.makeText(context, "Yes gros!", Toast.LENGTH_SHORT).show();
	}
}
