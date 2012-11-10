package com.glue.client.android.location;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glue.client.android.R;

public class AddressAdapter extends ArrayAdapter<Address> {

	private int layout;

	public AddressAdapter(Context context, int layoutId) {
		super(context, layoutId);
		layout = layoutId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(layout, null);

		TextView textViewTop = (TextView) view.findViewById(R.id.addressLine);
		TextView textViewBottom = (TextView) view
				.findViewById(R.id.addressLocality);

		Address address = getItem(position);
//		if (address != null) {
			textViewTop.setText(address.getMaxAddressLineIndex() > 0 ? address
					.getAddressLine(0) : "");
			textViewBottom.setText(address.getLocality());

//		}

		return view;
	}
}
