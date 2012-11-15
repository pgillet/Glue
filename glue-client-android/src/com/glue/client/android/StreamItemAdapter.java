package com.glue.client.android;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glue.client.android.StreamContent.StreamItem;

public class StreamItemAdapter extends ArrayAdapter<StreamItem> {

	private int layout;

	public StreamItemAdapter(Context context, int resource,
			int textViewResourceId, List<StreamItem> objects) {
		super(context, resource, textViewResourceId, objects);
		layout = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(layout, null);

		TextView textViewTop = (TextView) view.findViewById(R.id.item_label);
		TextView textViewBottom = (TextView) view.findViewById(R.id.item_value);

		StreamItem item = getItem(position);
		// if (address != null) {
		textViewTop.setText(item.getLabelId());

		int drawableId = item.getDrawableId();
		if (drawableId != 0) {
			textViewTop.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					item.getDrawableId(), 0);
		}

		textViewBottom.setText(item.getValue());

		// }

		return view;
	}
}
