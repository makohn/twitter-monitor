package de.htwsaar.exception.model;

public class UserException extends ModelException {

	private static final long serialVersionUID = 7276583207380025355L;

	public UserException() {
		super("Unbekannte UserException");
	}
	
	public UserException(String msg) {
		super("UserException: " + msg);
	}
	
}
