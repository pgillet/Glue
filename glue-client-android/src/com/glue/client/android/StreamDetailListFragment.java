package com.glue.client.android;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ListView;

import com.glue.client.android.widget.SeparatedListAdapter;
import com.glue.domain.Event;
import com.glue.domain.Venue;

public class StreamDetailListFragment extends ListFragment {

    public interface Callbacks {

	public Event getEventData();

	public void onItemSelected(String id);
    }

    public static class StreamItem<T> {

	private T caption;
	private int drawableId;

	private boolean enabled;
	private int titleId;

	public StreamItem(int titleId, int drawableId, T caption) {
	    this.titleId = titleId;
	    this.drawableId = drawableId;
	    this.caption = caption;
	}

	public StreamItem(int titleId, int drawableId, T caption,
		boolean enabled) {
	    this.titleId = titleId;
	    this.drawableId = drawableId;
	    this.caption = caption;
	    this.enabled = enabled;
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
	public int getTitleId() {
	    return titleId;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
	    return caption;
	}

	public Class<T> getValueType() {
	    return (Class<T>) caption.getClass();
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
	    return enabled;
	}

    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
	@Override
	public Event getEventData() {
	    return null;
	}

	@Override
	public void onItemSelected(String id) {
	}
    };

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private int mActivatedPosition = ListView.INVALID_POSITION;

    private Callbacks mCallbacks = sDummyCallbacks;

    private Event event;

    public StreamDetailListFragment() {
    }

    private StreamItem<String> createItem(int labelId, int drawableId, int resId) {
	return createItem(labelId, drawableId, getString(resId));
    }

    private StreamItem<String> createItem(int labelId, int drawableId,
	    String value) {

	StreamItem<String> item = null;
	boolean enabled = true;
	if (value == null || value.length() == 0) {
	    enabled = false;
	    value = getString(R.string.none);
	}

	item = new StreamItem<String>(labelId, drawableId, value, enabled);

	return item;
    }

    private List<StreamItem<?>> createSectionContent() {
	List<StreamItem<?>> content = new LinkedList<StreamItem<?>>();

	// Title
	content.add(createItem(R.string.title, 0, event.getTitle()));

	// Description
	// TODO tag management ...
	content.add(createItem(R.string.description,
		R.drawable.holo_dark_collections_labels, "test"));

	boolean isPublic = true;

	// Privacy
	StreamItem<String> privacyItem = createItem(R.string.privacy,
		isPublic ? R.drawable.holo_dark_device_access_not_secure
			: R.drawable.holo_dark_device_access_secure,
		isPublic ? R.string.togglePublic : R.string.togglePrivate);
	content.add(privacyItem);

	return content;
    }

    private List<StreamItem<?>> createSectionLocation() {
	List<StreamItem<?>> location = new LinkedList<StreamItem<?>>();

	// Start date
	String datePattern = getString(R.string.date_pattern);
	String timePattern = getString(R.string.time_pattern);
	StringBuilder sb = new StringBuilder()
		.append(DateFormat.format(datePattern, event.getStartTime()))
		.append(" ")
		.append(DateFormat.format(timePattern, event.getStartTime()));
	location.add(createItem(R.string.start_date,
		R.drawable.holo_dark_device_access_time, sb.toString()));

	// End date
	sb = new StringBuilder();
	if (event.getStopTime() != null) {
	    sb.append(DateFormat.format(datePattern, event.getStopTime()))
		    .append(" ")
		    .append(DateFormat.format(timePattern, event.getStopTime()));
	}
	location.add(createItem(R.string.end_date,
		R.drawable.holo_dark_device_access_time, sb.toString()));

	// Location
	Venue venue = event.getVenue();
	String loc = venue.getAddress();
	if (loc == null) {
	    double latitude = venue.getLatitude();
	    double longitude = venue.getLongitude();

	    if (latitude != 0 && longitude != 0) {
		loc = "Lat: " + latitude + ", Long: " + longitude;
	    }
	}
	location.add(createItem(R.string.location,
		R.drawable.holo_dark_location_place, loc));
	return location;
    }

    private List<StreamItem<?>> createSectionParticipation(Event event) {
	List<StreamItem<?>> participation = new LinkedList<StreamItem<?>>();

	boolean isOpen = true;
	// Participation type
	StreamItem<String> typeItem = createItem(R.string.participation_type,
		isOpen ? R.drawable.holo_dark_social_add_group
			: R.drawable.holo_dark_social_group,
		isOpen ? R.string.open : R.string.closed);
	participation.add(typeItem);

	// Participants
	Map<String, String> invitedParticipants = new HashMap<String, String>();
	StreamItem<?> participantsItem = null;
	if (invitedParticipants.isEmpty()) {
	    participantsItem = createItem(R.string.participants, 0, null);
	} else {
	    participantsItem = new StreamItem<Map<String, String>>(
		    R.string.participants, 0, invitedParticipants);
	}
	participation.add(participantsItem);

	// Shared secret question
	String sharedSecretQuestion = null, sharedSecretAnswer = null;

	StringBuilder secretQA = new StringBuilder();
	if (sharedSecretQuestion != null && sharedSecretQuestion.length() > 0) {
	    secretQA.append(sharedSecretQuestion).append(" / ")
		    .append(sharedSecretAnswer);
	}
	participation.add(createItem(R.string.shared_secret_question,
		R.drawable.holo_dark_device_access_accounts,
		secretQA.toString()));

	// Participation request
	boolean shouldRequestToParticipate = false;

	StreamItem<String> requestItem = createItem(
		R.string.participation_request, 0,
		shouldRequestToParticipate ? R.string.yes
			: R.string.no);
	participation.add(requestItem);

	return participation;
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	if (!(activity instanceof Callbacks)) {
	    throw new IllegalStateException(
		    "Activity must implement fragment's callbacks.");
	}

	mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	event = mCallbacks.getEventData();

	// create our list and custom adapter
	SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity());

	// Section Content
	List<StreamItem<?>> content = createSectionContent();
	adapter.addSection(getString(R.string.content), new StreamItemAdapter(
		getActivity(), content));

	// Section Participation
	List<StreamItem<?>> participation = createSectionParticipation(event);
	adapter.addSection(getString(R.string.participation),
		new StreamItemAdapter(getActivity(), participation));

	// Section Location
	List<StreamItem<?>> location = createSectionLocation();
	adapter.addSection(getString(R.string.localization),
		new StreamItemAdapter(getActivity(), location));

	setListAdapter(adapter);
    }

    @Override
    public void onDetach() {
	super.onDetach();
	mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
	    long id) {
	super.onListItemClick(listView, view, position, id);
	mCallbacks.onItemSelected(Integer.toString(position)); // TODO:
							       // content.getItems().get(position).id
							       // ?
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	if (mActivatedPosition != ListView.INVALID_POSITION) {
	    outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
	}
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
	if (savedInstanceState != null
		&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
	    setActivatedPosition(savedInstanceState
		    .getInt(STATE_ACTIVATED_POSITION));
	}
    }

    public void setActivatedPosition(int position) {
	if (position == ListView.INVALID_POSITION) {
	    getListView().setItemChecked(mActivatedPosition, false);
	} else {
	    getListView().setItemChecked(position, true);
	}

	mActivatedPosition = position;
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
	getListView().setChoiceMode(
		activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
			: ListView.CHOICE_MODE_NONE);
    }
}
