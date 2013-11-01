package com.glue.webapp.beans;

import java.util.Properties;

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

		Properties props = new Properties();
		// TLS secure connection
		props.setProperty("mail.transport.protocol", "smtps");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.stmp.user", "glue.contact@gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		// props.put("mail.debug", "true");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("glue.contact@gmail.com", "pascal&greg");
			}
		});
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("glue.contact@gmail.com"));
			msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("glue.contact@gmail.com"));
			msg.setSubject(subject);
			msg.setText("Provenant de " + mailAddress + "\n" + message);
			Transport.send(msg);
		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}

	}
}
