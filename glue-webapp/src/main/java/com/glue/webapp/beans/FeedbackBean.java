package com.glue.webapp.beans;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@ManagedBean
public class FeedbackBean {

	@Resource(name = "mail/gluemail")
	private Session mailSession;

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

	public void send() {

		// FacesContext context = FacesContext.getCurrentInstance();

		// It seems that Tomee does not do that job!
		Session session = Session.getInstance(mailSession.getProperties(), new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailSession.getProperty("mail.smtp.user"), mailSession
						.getProperty("password"));
			}
		});
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("glue.contact@gmail.com"));
			msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("glue.contact@gmail.com"));
			msg.setSubject(subject);
			msg.setText("Provenant de " + mailAddress + "\n" + message);
			Transport.send(msg);
			subject = null;
			mailAddress = null;
			message = null;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
}