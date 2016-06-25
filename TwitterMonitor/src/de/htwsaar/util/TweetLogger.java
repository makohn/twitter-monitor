package de.htwsaar.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class TweetLogger {

	private static HashMap<Date, String> log;
	
	public TweetLogger() {
		log = new HashMap<Date, String>();
	}

	public HashMap<Date, String> getLog() {
		return log;
	}
	
	public static void insertLog(String message) {
		log.put(new Date(), message);
		
		if ( log.size() > 500 )
			saveLog();
	}
	
	
	public static void saveLog() {
		try {			
			BufferedWriter writer = new BufferedWriter(new FileWriter("/logfiles/log - " + new Date().toString() + ".txt"));
			
			for (Date date : log.keySet())
				writer.write(date.toString() + " : " + log.get(date)+"\n\n");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log = new HashMap<Date, String>();
	}	
	
}
