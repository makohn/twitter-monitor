package de.htwsaar.exception.model;

public class TweetException extends ModelException {

	private static final long serialVersionUID = -7881335846383066063L;

	public TweetException() {
		super("Unbekannte TweetException");
	}
	
	public TweetException(String msg) {
		super("TweetException: " + msg);
	}
	
}
