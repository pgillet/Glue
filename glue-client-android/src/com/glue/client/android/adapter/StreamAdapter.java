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
import com.glue.domain.Event;

public class StreamAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Event> events = new ArrayList<Event>();
    private final ImageDownloader imageDownloader = new ImageDownloader();

    public StreamAdapter(Context context) {
	inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
	if (events != null) {
	    return events.size();
	}
	return 0;
    }

    @Override
    public Object getItem(int position) {
	return events.get(position);
    }

    @Override
    public long getItemId(int position) {
	return Long.getLong(events.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	RelativeLayout itemView;

	if (convertView == null) {
	    itemView = (RelativeLayout) inflater.inflate(R.layout.list_streams,
		    parent, false);
	} else {
	    itemView = (RelativeLayout) convertView;
	}

	ImageView imageView = (ImageView) itemView.findViewById(R.id.listImage);
	ImageView publicView = (ImageView) itemView
		.findViewById(R.id.publicImage);
	TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
	TextView nbOfParticipantText = (TextView) itemView
		.findViewById(R.id.nbOfParticipant);

	String imageUrl = events.get(position).getImages().get(0).getUrl();
	imageDownloader.download(Configuration.getBaseUrl() + imageUrl,
		imageView);
	titleText.setText(events.get(position).getTitle());
	nbOfParticipantText.setText(Long.toString(0));

	boolean isPublic = true;
	if (isPublic) {
	    publicView
		    .setImageResource(R.drawable.holo_dark_device_access_not_secure);
	} else {
	    publicView
		    .setImageResource(R.drawable.holo_dark_device_access_secure);
	}

	itemView.setTag(events.get(position));
	return itemView;
    }

    public void updateEvents(List<Event> events) {
	this.events = events;
	notifyDataSetChanged();
    }

}
