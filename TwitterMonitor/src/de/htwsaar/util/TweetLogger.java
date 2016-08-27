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
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component("tweetLogger")
public class TweetLogger {

	private static final String LOG_PATH = "C:\\Users\\philipp\\logfiles\\log";
	private static final String ARCHIVE_PATH = "C:\\Users\\philipp\\logfiles\\archive";

	public TweetLogger() {
		System.out.println("Logger gestartet"); // DEBUG
	}

	@Pointcut("execution(void de.htwsaar.service.twitter.StreamService.restartStream())")
	public void archivePoint() {
	}

	@Pointcut("execution(* de.htwsaar.db.*.*(..)) or execution(* de.htwsaar.validator.model.*.*(..)")
	public void logPoint() {
	}

	@AfterThrowing(pointcut = "logPoint()", throwing = "e")
	public /*static*/ synchronized void insertLog(Throwable e) {

		System.out.println("log einfuegen"); // DEBUG

		try {
			new File(LOG_PATH).createNewFile();

			BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_PATH, true));
			logWriter.write((new Date()).toString() + " - " + e.getMessage());
			logWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Before("archivePoint()")
	// @After("execution(void de.htwsaar.service.twitter.StreamService.stopStream())")
	public /*static*/ synchronized void archiveLog() {

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
