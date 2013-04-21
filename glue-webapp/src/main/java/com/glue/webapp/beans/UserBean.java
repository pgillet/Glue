package com.glue.webapp.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.naming.NamingException;

import com.glue.struct.impl.User;
import com.glue.webapp.auth.LoginBean;
import com.glue.webapp.services.UserResource;

@ManagedBean
public class UserBean {

	private String username;
	private String mailAddress;
	private String password;

	private UserResource userResource;
	
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
 
	//must povide the setter method
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	/**
	 * TODO: shoud probably use DI
	 * 
	 * @return
	 * @throws NamingException
	 */
	protected UserResource getUserResource() throws NamingException {
		if (userResource == null) {
			userResource = new UserResource();
		}

		return userResource;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String register() {

		try {
			userResource = getUserResource();

			User user = new User();

			String firstName = null;
			String lastName = "";
			int index = username.trim().indexOf(" ");
			if (index != -1) {
				firstName = username.substring(0, index);
				lastName = username.substring(index + 1);
			}
			
			
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setMailAddress(mailAddress);
			user.setPassword(password);

			userResource.createUser(user);
			
			loginBean.setUsername(mailAddress);
			loginBean.setPassword(password);
			
			return loginBean.login();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
