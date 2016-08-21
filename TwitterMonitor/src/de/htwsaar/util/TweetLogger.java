package de.htwsaar.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component("tweetLogger")
public class TweetLogger {

	private static final String LOG_PATH = "/home/philipp/logfiles/log";
	private static final String ARCHIVE_PATH = "/home/philipp/logfiles/archive";

	private BufferedWriter logWriter;
	private File logFile;

	public TweetLogger() {
		
		System.out.println("Logger gestartet");			// DEBUG
		
		logFile = new File(LOG_PATH);
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	@Pointcut("execution(* de.htwsaar.service.twitter.StreamService.stopStream(..))")
	public void archivePoint() {
	}

	@Pointcut("execution(* *(..))")
	public void logPoint() {
	}

	@AfterThrowing(pointcut = "logPoint()", throwing = "e")
	public synchronized void insertLog(Throwable e) throws IOException {
		
		System.out.println("einfügen");			// DEBUG
		
		logWriter = new BufferedWriter(new FileWriter(logFile, true));
		logWriter.write((new Date()).toString() + " - " + e.getMessage());
		logWriter.close();
	}

	@After("archivePoint()")
	public synchronized void archiveLog() throws IOException {
		
		System.out.println("Archiviere");			// DEBUG
				
		BufferedReader logReader = new BufferedReader(new FileReader(LOG_PATH));
		BufferedWriter archiveWriter = new BufferedWriter(new FileWriter(ARCHIVE_PATH, true));
		
		String logLine;
		while ((logLine = logReader.readLine()) != null) {
			archiveWriter.write(logLine);
		}
		
		logReader.close();
		archiveWriter.close();
		
		logFile.delete();
		logFile.createNewFile();
	}
}
