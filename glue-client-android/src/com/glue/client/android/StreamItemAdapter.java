package com.glue.client.android;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.glue.client.android.StreamDetailListFragment.StreamItem;
import com.glue.client.android.view.FlowLayout;

public class StreamItemAdapter extends ArrayAdapter<StreamItem<?>> {

	public StreamItemAdapter(Context context, List<StreamItem<?>> objects) {
		super(context, 0, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		StreamItem<?> item = getItem(position);
		View view = null;

		if (item.getValue() instanceof String) { /*
												 * String.class.isAssignableFrom(
												 * item.getValueType())
												 */
			view = getStringView(position, convertView, parent);
		} else if (item.getValue() instanceof Map<?, ?>) {
			view = getMapView(position, convertView, parent);
		}

		return view;
	}

	private View getStringView(int position, View convertView, ViewGroup parent) {
		View view;
		StreamItem<String> item = (StreamItem<String>) getItem(position);
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.fragment_stream_item, null);

		TextView textViewTop = (TextView) view.findViewById(R.id.item_label);
		TextView textViewBottom = (TextView) view.findViewById(R.id.item_value);

		textViewTop.setText(item.getTitleId());

		int drawableId = item.getDrawableId();
		if (drawableId != 0) {
			textViewTop.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					item.getDrawableId(), 0);
		}

		textViewBottom.setText((String) item.getValue());

		if (item.isEnabled()) {
			textViewBottom.setTextAppearance(getContext(), R.style.Accent);
		} else {
			textViewBottom.setEnabled(false);
		}

		return view;
	}

	private View getMapView(int position, View convertView, ViewGroup parent) {
		View view;
		StreamItem<Map<String, String>> item = (StreamItem<Map<String, String>>) getItem(position);
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.fragment_stream_map_item, null);

		TextView textViewTop = (TextView) view.findViewById(R.id.item_label);
		textViewTop.setText(item.getTitleId());
		int drawableId = item.getDrawableId();
		if (drawableId != 0) {
			textViewTop.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					item.getDrawableId(), 0);
		}

		FlowLayout layoutBottom = (FlowLayout) view
				.findViewById(R.id.item_map_value);

		Map<String, String> m = item.getValue();
		for (Entry<String, String> entry : m.entrySet()) {
			Button button = new Button(getContext(), null,
					android.R.attr.buttonStyleSmall);
			button.setText(entry.getValue() != null ? entry.getValue() : entry
					.getKey());
			button.setId(entry.getKey().hashCode());

			layoutBottom.addView(button);
		}

		return view;
	}
}
