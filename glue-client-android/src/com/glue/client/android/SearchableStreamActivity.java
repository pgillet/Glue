package com.glue.client.android;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.client.android.adapter.StreamAdapter;
import com.glue.struct.IStream;

public class SearchableStreamActivity extends ListActivity {

	/** The tag used to log to adb console. */
	private static final String TAG = "SearchableStreamActivity";

	private List<IStream> streams;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchable_stream);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			// Loading products in Background Thread
			new SearchStreamsTask(query).execute();
		}

		// Get listview
		ListView lv = getListView();

		// On selecting a stream, see the associated medias
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// Getting values from selected ListItem
				Long streamId = Long.getLong(((TextView) view.findViewById(R.id.stream_id)).getText().toString());

				// Starting new intent
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);

				// sending pid to next activity
				// in.putExtra(TAG_PID, pid);

				// starting new activity and expecting some response back
				// startActivityForResult(in, 100);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_searchable_stream, menu);
		return true;
	}

	/**
	 * Background Async Task to search for Streams
	 * */
	class SearchStreamsTask extends AsyncTask<Void, String, String> {
		private final ProgressDialog dialog = new ProgressDialog(SearchableStreamActivity.this);
		private final String query;
		private Glue glue = new GlueFactory().getInstance();

		public SearchStreamsTask(String query) {
			this.query = query;
		}

		// Before starting background thread Show Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Searching for streams ...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		// Retrieving Streams
		protected String doInBackground(Void... params) {
			try {
				// Search for streams
				streams = glue.searchStreams(query);

			} catch (Exception ex) {
				Log.e(TAG, "SearchStreamsTask.doInBackground");
				Log.i(TAG, ex.toString());
				return null;
			}
			return null;
		}

		// After completing background task dismiss the progress dialog
		protected void onPostExecute(String result) {

			// dismiss the dialog after getting all products
			dialog.dismiss();

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {

					ListAdapter adapter = new StreamAdapter(SearchableStreamActivity.this, R.layout.list_streams,
							streams);

					// Updating view
					setListAdapter(adapter);
				}
			});

		}

	}
}
