package de.htwsaar.exception;

public class TwitterMonitorException extends Exception {
	
	private static final long serialVersionUID = 2093045877934395643L;
	
	public TwitterMonitorException() {
		super("Unbekannte TwitterMonitorException");
	}	
	
	public TwitterMonitorException(String msg) {
		super(msg);
	}
}

