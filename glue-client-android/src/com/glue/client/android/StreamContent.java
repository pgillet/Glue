package com.glue.client.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.glue.IStream;

public class StreamContent {

	public static class StreamItem {

		private int drawableId;
		private int labelId;

		private String value;

		public StreamItem(int labelId, int drawableId, String value) {
			this.labelId = labelId;
			this.drawableId = drawableId;
			this.value = value;
		}

		/**
		 * @return the drawableId
		 */
		public int getDrawableId() {
			return drawableId;
		}

		/**
		 * @return the labelId
		 */
		public int getLabelId() {
			return labelId;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}

	private Context context;

	private List<StreamItem> items = new ArrayList<StreamItem>();

	private IStream stream;

	public StreamContent(Context context, IStream stream) {
		this.context = context;
		this.stream = stream;

		addItem(R.string.title, 0, stream.getTitle());
		addItem(R.string.description, R.drawable.collections_labels,
				stream.getDescription());
		addItem(R.string.privacy,
				stream.isPublic() ? R.drawable.device_access_not_secure_holo_dark
						: R.drawable.device_access_secure_holo_dark,
				stream.isPublic() ? R.string.togglePublic
						: R.string.togglePrivate);

	}

	private void addItem(int labelId, int drawableId, int resId) {
		addItem(labelId, drawableId, context.getString(resId));
	}

	private void addItem(int labelId, int drawableId, String value) {
		items.add(new StreamItem(labelId, drawableId, value));
	}

	/**
	 * @return the items
	 */
	public List<StreamItem> getItems() {
		return items;
	}

}
