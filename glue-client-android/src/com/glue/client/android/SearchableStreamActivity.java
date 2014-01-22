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
import android.widget.ListView;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.client.android.adapter.StreamAdapter;
import com.glue.domain.IStream;

public class SearchableStreamActivity extends ListActivity {

	private static final String TAG = "SearchableStreamActivity";
	public static final String PARAM_STREAM_ID = "streamId";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchable_stream);

		StreamAdapter adapter = new StreamAdapter(this);
		setListAdapter(adapter);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			// Loading products in Background Thread
			new SearchStreamsTask(query, adapter).execute();
		}

		// Get listview
		ListView lv = getListView();

		// On selecting a stream, see the associated medias
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// Getting values from selected ListItem
				Long streamId = ((IStream) view.getTag()).getId();

				// Starting new intent with stream ID
				Intent intent = new Intent(getApplicationContext(), StreamDetailActivity.class);
				intent.putExtra(PARAM_STREAM_ID, streamId);
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
	class SearchStreamsTask extends AsyncTask<Void, Void, List<IStream>> {
		private final StreamAdapter adapter;
		private final ProgressDialog dialog = new ProgressDialog(SearchableStreamActivity.this);
		private final String query;
		private Glue glue = new GlueFactory().getInstance();

		public SearchStreamsTask(String query, StreamAdapter adapter) {
			this.query = query;
			this.adapter = adapter;
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
		protected List<IStream> doInBackground(Void... params) {
			List<IStream> result = null;
			try {
				// Search for streams
				result = glue.searchStreams(query);

			} catch (Exception ex) {
				Log.e(TAG, "SearchStreamsTask.doInBackground");
				Log.i(TAG, ex.toString());
				return null;
			}
			return result;
		}

		// After completing background task dismiss the progress dialog
		protected void onPostExecute(List<IStream> streams) {

			// dismiss the dialog after getting all products
			dialog.dismiss();
			adapter.updateStreams(streams);
		}

	}
}
