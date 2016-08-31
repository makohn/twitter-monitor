package de.htwsaar.exception;

public class TwitterMonitorException extends Exception {

	private static final long serialVersionUID = 2093045877934395643L;

	private static final String MSG = "Unbekannte TwitterMonitorException";

	public TwitterMonitorException() {
		super(MSG);
	}

	public TwitterMonitorException(String msg) {
		super(msg);
	}
}
