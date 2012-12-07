package com.glue.client.android;

import java.util.Calendar;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.client.android.stream.StreamData;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;

public class CreateStreamSummaryActivity extends FragmentActivity implements StreamDetailListFragment.Callbacks {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_stream_summary);

		Button button = (Button) findViewById(R.id.button1);

		Calendar rightNow = Calendar.getInstance();
		Calendar other = Calendar.getInstance();
		other.setTimeInMillis(StreamData.getInstance().getStartDate());

		if (other.after(rightNow)) {
			button.setText(R.string.save);
			button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.holo_light_content_save, 0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_stream_summary, menu);
		return true;
	}

	public void onClickStart(View v) {
		IStream data = StreamData.getInstance();
		(new CreateStreamTask()).execute(data);
	}

	// AsyncTask
	private class CreateStreamTask extends AsyncTask<IStream, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(CreateStreamSummaryActivity.this);
		private Glue glue = new GlueFactory().getInstance();

		public CreateStreamTask() {
			super();
		}

		// can use UI thread here
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Creating the stream...");
			dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		@Override
		protected String doInBackground(final IStream... streams) {
			String result = null;

			// Connect ...

			// Create a stream
			try {
				IStream myFirstStream = glue.createStream(streams[0]);
				result = Long.toString(myFirstStream.getId());
			} catch (GlueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		// can use UI thread here
		@Override
		protected void onPostExecute(final String result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			Toast.makeText(CreateStreamSummaryActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onItemSelected(String id) {
	}

	@Override
	public IStream getStreamData() {
		return StreamData.getInstance();
	}
}
