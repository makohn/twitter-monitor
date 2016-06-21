package de.htwsaar.exception.model;

import de.htwsaar.exception.TwitterMonitorException;

public class ModelException extends TwitterMonitorException {

	private static final long serialVersionUID = -692337546665666845L;

	public ModelException() {
		super("Unbekannte ModelException");
	}
	
	public ModelException(String msg) {
		super(msg);
	}
	
}
