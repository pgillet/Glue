package com.glue.webapp.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.impl.User;
import com.glue.webapp.auth.LoginBean;
import com.glue.webapp.logic.AlreadyExistsException;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.UserController;

@ManagedBean
public class UserBean {
	
	static final Logger LOG = LoggerFactory.getLogger(UserBean.class);

	private String name;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

		user.setName(name);
		user.setMailAddress(mailAddress);
		user.setPassword(password);

		try {
			userController.createUser(user);

			loginBean.setUsername(mailAddress);
			loginBean.setPassword(password);

			return loginBean.login();
		} catch (InternalServerException e) {
			LOG.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(e.getMessage()));
		} catch (AlreadyExistsException e) {
			LOG.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}

		return "register";
	}
}
