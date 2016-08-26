package de.htwsaar.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

//@Aspect
//@Component("tweetLogger")
public class TweetLogger {

	private static final String LOG_PATH = "/home/philipp/logfiles/log";
	private static final String ARCHIVE_PATH = "/home/philipp/logfiles/archive";

	// private BufferedWriter logWriter;
	// private File logFile;

	// public TweetLogger() {
	//
	// System.out.println("Logger gestartet"); // DEBUG
	//
	// logFile = new File(LOG_PATH);
	// try {
	// logFile.createNewFile();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// @Pointcut("execution(void
	// de.htwsaar.service.twitter.StreamService.stopStream())")
	// public void archivePoint() {
	// }

	// @Pointcut("execution(* *(..))")
	// public void logPoint() {
	// }

	// @AfterThrowing(pointcut = "logPoint()", throwing = "e")
	public static synchronized void insertLog(String msg) {

		System.out.println("einfügen"); // DEBUG

		try {
			new File(LOG_PATH).createNewFile();

			BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_PATH, true));
			logWriter.write((new Date()).toString() + " - " + msg);
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @After("archivePoint()")
	// @After("execution(void
	// de.htwsaar.service.twitter.StreamService.stopStream())")
	public static synchronized void archiveLog() {

		System.out.println("Archiviere"); // DEBUG

		File logFile = new File(LOG_PATH);
		if (logFile.exists()) {
			try {
				BufferedReader logReader = new BufferedReader(new FileReader(LOG_PATH));
				BufferedWriter archiveWriter = new BufferedWriter(new FileWriter(ARCHIVE_PATH, true));

				String logLine;
				while ((logLine = logReader.readLine()) != null) {
					archiveWriter.write(logLine);
				}

				logReader.close();
				archiveWriter.close();

				logFile.delete();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
