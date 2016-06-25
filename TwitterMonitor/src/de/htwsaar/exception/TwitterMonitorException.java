package de.htwsaar.exception;

import de.htwsaar.util.TweetLogger;

public class TwitterMonitorException extends Exception {
	
	private static final long serialVersionUID = 2093045877934395643L;

	public TwitterMonitorException() {
		TweetLogger.insertLog("Unbekannte TwitterMonitorException");
	}
	
	public TwitterMonitorException(String msg) {
		TweetLogger.insertLog(msg);
	}
}

