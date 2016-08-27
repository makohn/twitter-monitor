package de.htwsaar.validator.model;

import java.util.Date;

import de.htwsaar.exception.model.UserException;

public class UserValidator {

	public static void checkUserId(int userId) throws UserException {		
		if ( userId < 0 )
			throw new UserException();
	}

	public static String checkEmail(String email) {
		if ( email == null )
			email = "";
		if ( email.length() > 60 )
			email = email.substring(0, 59);		
		return email;
	}

	public static void checkPassword(String password) throws UserException {
		if ( password == null )
			throw new UserException();
	}

	public static Date checkDate(Date registeredAt) {
		if ( registeredAt == null )
			registeredAt = new Date();
		return registeredAt;
	}
}
