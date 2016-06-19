package de.htwsaar.model;

import java.util.Date;

import de.htwsaar.exceptions.model.UserException;
import de.htwsaar.validators.model.UserValidator;

public class User {
	
	private String email;
	private String password;
	private Date registeredAt;
	
	public User() {}
	
	public User(String email, String password, Date registeredAt) throws UserException {
		setEmail(email);
		setPassword(password);
		setRegisteredAt(registeredAt);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		email = UserValidator.checkEmail(email);
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws UserException {
		UserValidator.checkPassword(password);
		this.password = password;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Date registeredAt) {
		registeredAt = UserValidator.checkDate(registeredAt);
		this.registeredAt = registeredAt;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", registeredAt="
				+ registeredAt + "]";
	}
}
