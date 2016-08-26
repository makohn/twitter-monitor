package de.htwsaar.exception;

import de.htwsaar.util.TweetLogger;

public class TwitterMonitorException extends Exception {
	
	private static final long serialVersionUID = 2093045877934395643L;
	
	private static final String msg = "Unbekannte TwitterMonitorException";
	
	public TwitterMonitorException() {		
		super(msg);
		TweetLogger.insertLog(msg);
	}	
	
	public TwitterMonitorException(String msg) {
		super(msg);
		TweetLogger.insertLog(msg);
	}
}

