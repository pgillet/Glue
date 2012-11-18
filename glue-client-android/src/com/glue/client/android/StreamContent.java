package com.glue.client.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.glue.struct.IStream;

public class StreamContent {

	public static class StreamItem<T> {

		private int drawableId;
		private int labelId;

		private T value;

		public StreamItem(int labelId, int drawableId, T value) {
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
		public T getValue() {
			return value;
		}

		public Class<T> getValueType() {
			return (Class<T>) value.getClass();
		}

	}

	private Context context;

	private List<StreamItem<?>> items = new ArrayList<StreamItem<?>>();

	private IStream stream;

	public StreamContent(Context context, IStream stream) {
		this.context = context;
		this.stream = stream;

		addItem(R.string.title, 0, stream.getTitle());
		addItem(R.string.description, R.drawable.holo_dark_collections_labels,
				stream.getDescription());
		addItem(R.string.privacy,
				stream.isPublic() ? R.drawable.holo_dark_device_access_not_secure
						: R.drawable.holo_dark_device_access_secure,
				stream.isPublic() ? R.string.togglePublic
						: R.string.togglePrivate);
		addItem(R.string.participation_type,
				stream.isOpen() ? R.drawable.holo_dark_social_add_group
						: R.drawable.holo_dark_social_group,
				stream.isOpen() ? R.string.open : R.string.closed);

		items.add(new StreamItem<Map<String, String>>(R.string.participants, 0,
				stream.getInvitedParticipants()));

	}

	private void addItem(int labelId, int drawableId, int resId) {
		addItem(labelId, drawableId, context.getString(resId));
	}

	private void addItem(int labelId, int drawableId, String value) {
		items.add(new StreamItem<String>(labelId, drawableId, value));
	}

	/**
	 * @return the items
	 */
	public List<StreamItem<?>> getItems() {
		return items;
	}

}
