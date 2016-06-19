package de.htwsaar.model;

import java.util.Date;

import de.htwsaar.exceptions.model.UserException;
import de.htwsaar.validators.model.UserValidator;

public class User {
	
	private int userId;
	private String email;
	private String password;
	private Date registeredAt;
	
	public User() {}
	
	public User(int userId, String email, String password, Date registeredAt) throws UserException {		
		setUserId(userId);
		setEmail(email);
		setPassword(password);
		setRegisteredAt(registeredAt);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) throws UserException {
		UserValidator.checkUserId(userId);
		this.userId = userId;
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
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", registeredAt="
				+ registeredAt + "]";
	}
}
