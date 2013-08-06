package com.glue.webapp.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.glue.struct.IUser;
import com.glue.struct.impl.Stream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.StreamController;
import com.glue.webapp.logic.UserController;

@ManagedBean
public class StreamBean /* implements IStream */{

	// TODO: should probably use Dependency Injection here!
	StreamController streamController = new StreamController();
	UserController userController = new UserController();

	private String title;

	private String description;

	private boolean publicc;

	private boolean open = true;

	private String invitedParticipants;

	private String invitedGuests;

	private String sharedSecretQuestion;

	private String sharedSecretAnswer;

	private boolean shouldRequestToParticipate;

	private Date startDate;

	private Date endDate;

	private double latitude;

	private double longitude;

	private String address;

	private Set<String> tags;

	private int numberOfParticipant;

	private String thumbPath;

	public StreamBean() {
		Calendar calendar = Calendar.getInstance();
		startDate = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY, 2);
		endDate = calendar.getTime();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the publicc
	 */
	public boolean isPublicc() {
		return publicc;
	}

	/**
	 * @param publicc
	 *            the publicc to set
	 */
	public void setPublicc(boolean publicc) {
		this.publicc = publicc;
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/**
	 * @return the invitedParticipants
	 */
	public String getInvitedParticipants() {
		return invitedParticipants;
	}

	/**
	 * @param invitedParticipants
	 *            the invitedParticipants to set
	 */
	public void setInvitedParticipants(String invitedParticipants) {
		this.invitedParticipants = invitedParticipants;
	}

	/**
	 * @return the invitedGuests
	 */
	public String getInvitedGuests() {
		return invitedGuests;
	}

	/**
	 * @param invitedGuests
	 *            the invitedGuests to set
	 */
	public void setInvitedGuests(String invitedGuests) {
		this.invitedGuests = invitedGuests;
	}

	/**
	 * @return the sharedSecretQuestion
	 */
	public String getSharedSecretQuestion() {
		return sharedSecretQuestion;
	}

	/**
	 * @param sharedSecretQuestion
	 *            the sharedSecretQuestion to set
	 */
	public void setSharedSecretQuestion(String sharedSecretQuestion) {
		this.sharedSecretQuestion = sharedSecretQuestion;
	}

	/**
	 * @return the sharedSecretAnswer
	 */
	public String getSharedSecretAnswer() {
		return sharedSecretAnswer;
	}

	/**
	 * @param sharedSecretAnswer
	 *            the sharedSecretAnswer to set
	 */
	public void setSharedSecretAnswer(String sharedSecretAnswer) {
		this.sharedSecretAnswer = sharedSecretAnswer;
	}

	/**
	 * @return the shouldRequestToParticipate
	 */
	public boolean isShouldRequestToParticipate() {
		return shouldRequestToParticipate;
	}

	/**
	 * @param shouldRequestToParticipate
	 *            the shouldRequestToParticipate to set
	 */
	public void setShouldRequestToParticipate(boolean shouldRequestToParticipate) {
		this.shouldRequestToParticipate = shouldRequestToParticipate;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the numberOfParticipant
	 */
	public int getNumberOfParticipant() {
		return numberOfParticipant;
	}

	/**
	 * @param numberOfParticipant
	 *            the numberOfParticipant to set
	 */
	public void setNumberOfParticipant(int numberOfParticipant) {
		this.numberOfParticipant = numberOfParticipant;
	}

	/**
	 * @return the thumbPath
	 */
	public String getThumbPath() {
		return thumbPath;
	}

	/**
	 * @param thumbPath
	 *            the thumbPath to set
	 */
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String create() {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		IUser authenticatedUser = (IUser) request.getUserPrincipal();

		Stream stream = new Stream();
		stream.setAddress(address);
		stream.setDescription(description);
		stream.setEndDate(endDate.getTime());
		stream.setLatitude(latitude);
		stream.setLongitude(longitude);
		stream.setOpen(open);
		stream.setPublicc(publicc);
		stream.setSharedSecretAnswer(sharedSecretAnswer);
		stream.setSharedSecretQuestion(sharedSecretQuestion);
		stream.setShouldRequestToParticipate(shouldRequestToParticipate);
		stream.setStartDate(startDate.getTime());
		stream.setTags(tags);
		stream.setThumbPath(thumbPath);
		stream.setTitle(title);

		try {
			String[] addresses = invitedParticipants.split("\\s+,\\s+");
			// List<IUser> users = userController.getUsers(addresses);
			Map<String, String> m = new LinkedHashMap<String, String>();
			// For now, we cannot know the name associated to a mail address
			// unless we integrate contact API from various mail services.
			for (String address : addresses) {
				m.put(address, null);
			}
			stream.setInvitedParticipants(m);

			streamController.createStream(stream, authenticatedUser);
		} catch (InternalServerException e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}

		return "main";
	}

}
