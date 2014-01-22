package com.glue.client.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glue.api.conf.Configuration;
import com.glue.client.android.R;
import com.glue.client.android.util.ImageDownloader;
import com.glue.domain.IStream;

public class StreamAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<IStream> streams = new ArrayList<IStream>();
	private final ImageDownloader imageDownloader = new ImageDownloader();

	public StreamAdapter(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		RelativeLayout itemView;

		if (convertView == null) {
			itemView = (RelativeLayout) inflater.inflate(R.layout.list_streams, parent, false);
		} else {
			itemView = (RelativeLayout) convertView;
		}

		ImageView imageView = (ImageView) itemView.findViewById(R.id.listImage);
		ImageView publicView = (ImageView) itemView.findViewById(R.id.publicImage);
		TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
		TextView nbOfParticipantText = (TextView) itemView.findViewById(R.id.nbOfParticipant);

		String imageUrl = streams.get(position).getThumbPath();
		imageDownloader.download(Configuration.getBaseUrl() + imageUrl, imageView);
		titleText.setText(streams.get(position).getTitle());
		nbOfParticipantText.setText(Long.toString(streams.get(position).getNumberOfParticipant()));

		if (streams.get(position).isPublicc()) {
			publicView.setImageResource(R.drawable.holo_dark_device_access_not_secure);
		} else {
			publicView.setImageResource(R.drawable.holo_dark_device_access_secure);
		}

		itemView.setTag(streams.get(position));
		return itemView;
	}

	public void updateStreams(List<IStream> streams) {
		this.streams = streams;
		notifyDataSetChanged();
	}

}
