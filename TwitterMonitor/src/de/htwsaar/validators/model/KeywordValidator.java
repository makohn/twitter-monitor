package de.htwsaar.validators.model;

import de.htwsaar.exceptions.model.KeywordException;

public class KeywordValidator {

	public static void checkKeyword(String keyword) throws KeywordException {
		if ( keyword == null )
			throw new KeywordException();
		
		if ( keyword.length() > 50 )
			throw new KeywordException();
	}

	public static void checkUserId(int userId) throws KeywordException {
		if ( userId < 0 )
			throw new KeywordException();		
	}

	public static void checkPriority(int priority) throws KeywordException {
		if ( (priority < 1) || (priority > 5) )
			throw new KeywordException();		
	}
	
}
