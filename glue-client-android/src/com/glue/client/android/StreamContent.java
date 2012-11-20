package com.glue.client.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.format.DateFormat;

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

		// Title
		addItem(R.string.title, 0, stream.getTitle());

		// Description
		addItem(R.string.description, R.drawable.holo_dark_collections_labels,
				stream.getDescription());

		// Privacy
		addItem(R.string.privacy,
				stream.isPublic() ? R.drawable.holo_dark_device_access_not_secure
						: R.drawable.holo_dark_device_access_secure,
				stream.isPublic() ? R.string.togglePublic
						: R.string.togglePrivate);

		// Participation type
		addItem(R.string.participation_type,
				stream.isOpen() ? R.drawable.holo_dark_social_add_group
						: R.drawable.holo_dark_social_group,
				stream.isOpen() ? R.string.open : R.string.closed);

		// Participants
		if (stream.getInvitedParticipants().isEmpty()) {
			addItem(R.string.participants, 0, null);
		} else {
			items.add(new StreamItem<Map<String, String>>(
					R.string.participants, 0, stream.getInvitedParticipants()));
		}

		// Shared secret question
		String secretQuestion = stream.getSharedSecretQuestion();
		if (secretQuestion != null && secretQuestion.length() > 0) {
			addItem(R.string.shared_secret_question,
					R.drawable.holo_dark_device_access_accounts, secretQuestion
							+ " / " + stream.getSharedSecretAnswer());
		} else {
			addItem(R.string.shared_secret_question,
					R.drawable.holo_dark_device_access_accounts, null);
		}

		// Participation request
		addItem(R.string.participation_request, 0,
				stream.shouldRequestToParticipate() ? R.string.yes
						: R.string.no);

		// Start date
		Calendar from = Calendar.getInstance();
		from.setTimeInMillis(stream.getStartDate());

		String datePattern = context.getString(R.string.date_pattern);
		String timePattern = context.getString(R.string.time_pattern);
		StringBuilder sb = new StringBuilder()
				.append(DateFormat.format(datePattern, from)).append(" ")
				.append(DateFormat.format(timePattern, from));
		addItem(R.string.start_date, R.drawable.holo_dark_device_access_time,
				sb.toString());

		// End date
		if (stream.getEndDate() > 0) {
			Calendar to = Calendar.getInstance();
			to.setTimeInMillis(stream.getEndDate());
			sb = new StringBuilder().append(DateFormat.format(datePattern, to))
					.append(" ").append(DateFormat.format(timePattern, to));
			addItem(R.string.end_date, R.drawable.holo_dark_device_access_time,
					sb.toString());
		} else {
			addItem(R.string.end_date,
					R.drawable.holo_dark_device_access_time, R.string.none);
		}

		// Location
		String location = stream.getAddress();
		if (location == null) {
			double latitude = stream.getLatitude();
			double longitude = stream.getLongitude();

			if (latitude != 0 && longitude != 0) {
				location = "Lat: " + latitude + ", Long: " + longitude;
			} else {
				location = null;
			}
		}

		addItem(R.string.location, R.drawable.holo_dark_location_place,
				location);

	}

	private void addItem(int labelId, int drawableId, int resId) {
		addItem(labelId, drawableId, context.getString(resId));
	}

	private void addItem(int labelId, int drawableId, String value) {

		if (value == null || value.length() == 0) {
			value = context.getString(R.string.none);
		}

		items.add(new StreamItem<String>(labelId, drawableId, value));
	}

	/**
	 * @return the items
	 */
	public List<StreamItem<?>> getItems() {
		return items;
	}

}
