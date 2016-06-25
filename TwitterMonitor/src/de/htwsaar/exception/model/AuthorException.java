package de.htwsaar.exception.model;

public class AuthorException extends ModelException {

	private static final long serialVersionUID = 7534697965669102893L;

	public AuthorException() {
		super("Unbekannte AuthorException");
	}
	
	public AuthorException(String msg) {
		super("AuthorException: " + msg);
	}
	
}