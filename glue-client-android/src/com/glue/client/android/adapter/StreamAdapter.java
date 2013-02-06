package com.glue.client.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glue.client.android.R;
import com.glue.struct.IStream;

public class StreamAdapter extends BaseAdapter {

	Context context;
	List<IStream> streams;
	private LayoutInflater inflater;
	private int resource;

	public StreamAdapter(Context context, int resource, List<IStream> streams) {
		this.context = context;
		this.streams = streams;
		this.resource = resource;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		if (streams != null) {
			return streams.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return streams.get(position);
	}

	@Override
	public long getItemId(int position) {
		return streams.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Create a new view
		convertView = (LinearLayout) inflater.inflate(resource, null);

		// Get my stream
		IStream stream = streams.get(position);

		// TxtView ID
		TextView txtId = (TextView) convertView.findViewById(R.id.stream_id);
		txtId.setText(Long.toString(stream.getId()));

		// TxtView Title
		TextView txtTitle = (TextView) convertView.findViewById(R.id.stream_title);
		txtTitle.setText(stream.getTitle());

		return convertView;

	}

}
