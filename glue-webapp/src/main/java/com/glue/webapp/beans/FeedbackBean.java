package com.glue.webapp.beans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class FeedbackBean {

	/*
	 * @Resource(name = "mail/gluemail") private Session mailSession;
	 */
	private static final String FEEDBACK_FILE_NAME = "Feedback.txt";

	private String subject;
	private String mailAddress;
	private String message;

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param name
	 *            the name to subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @param mailAddress
	 *            the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public synchronized void send() {

		// FacesContext context = FacesContext.getCurrentInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYY hh:mm:ss");
		FileWriter writer = null;
		BufferedWriter fbw = null;

		try {
			writer = new FileWriter(System.getenv("GLUE_HOME") + File.separator + FEEDBACK_FILE_NAME, true);
			fbw = new BufferedWriter(writer);
			fbw.write("Date: " + formatter.format(new Date()));
			fbw.newLine();
			fbw.write("Sujet: " + (subject == null ? "" : subject));
			fbw.newLine();
			fbw.write("Mail: " + (mailAddress == null ? "" : mailAddress));
			fbw.newLine();
			fbw.write("Message:");
			fbw.newLine();
			fbw.write(message == null ? "" : message);
			fbw.newLine();
			fbw.write("----------------------------------------");
			fbw.newLine();
			fbw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (fbw != null) {
				try {
					fbw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			subject = null;
			mailAddress = null;
			message = null;
		}
	}
}
