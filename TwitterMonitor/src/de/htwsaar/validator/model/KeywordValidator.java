package de.htwsaar.validator.model;

import de.htwsaar.exception.model.KeywordException;

public class KeywordValidator {

	public static void checkKeyword(String keyword) throws KeywordException {
		if ( keyword == null )
			throw new KeywordException();
		
		if ( keyword.length() > 49 )
			throw new KeywordException();
	}

	public static void checkUserId(int userId) throws KeywordException {
		if ( userId < 0 )
			throw new KeywordException();		
	}
	
	public static String checkUsername(String username) throws KeywordException {
		if ( username == null )
			throw new KeywordException();
		if ( username.length() > 59 )
			throw new KeywordException();
		
		return username;
	}

	public static void checkPriority(int priority) throws KeywordException {
		if ( (priority < 1) || (priority > 5) )
			throw new KeywordException();		
	}
	
}
