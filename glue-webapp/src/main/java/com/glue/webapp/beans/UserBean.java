package com.glue.webapp.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import com.glue.struct.impl.User;
import com.glue.webapp.auth.LoginBean;
import com.glue.webapp.logic.AlreadyExistsException;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.UserController;

@ManagedBean
public class UserBean {

	private String username;
	private String mailAddress;
	private String password;

	// TODO: should probably use Dependency Injection here!
	UserController userController = new UserController();

	@ManagedProperty(value = "#{loginBean}")
	private LoginBean loginBean;

	// must provide the setter method
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
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

		FacesContext context = FacesContext.getCurrentInstance();

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

		try {
			userController.createUser(user);

			loginBean.setUsername(mailAddress);
			loginBean.setPassword(password);

			return loginBean.login();
		} catch (InternalServerException e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
		} catch (AlreadyExistsException e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}

		return "register";
	}
}
