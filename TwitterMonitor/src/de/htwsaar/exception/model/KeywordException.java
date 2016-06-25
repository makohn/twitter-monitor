package de.htwsaar.exception.model;

public class KeywordException extends ModelException {

	private static final long serialVersionUID = -4063866121305097709L;

	public KeywordException() {
		super("Unbekannte KeywordException");
	}
	
	public KeywordException(String msg) {
		super("KeywordException " + msg);
	}
	
}
