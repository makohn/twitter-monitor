package de.htwsaar.util;

import javax.mail.PasswordAuthentication;

/**
 * Klasse für die Anmeldung des Mailers
 * @author Marius Backes
 */

public class SMTPAuthenticator extends javax.mail.Authenticator {
	
	/* GoogleMail Adresse die als Sendeadresse dient 
	 * Passwort fuer die Authentifizierung mit der GoogleMail Adresse
	 */
	private final static String senderMailAdresse = "htw.twittermonitor@gmail.com";
	private final static String senderPasswort = "MasideHa!101";
	
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(senderMailAdresse, senderPasswort);
	}

	public static String getSenderMailAdresse() {
		return senderMailAdresse;
	}

	public static String getSenderPasswort() {
		return senderPasswort;
	}
}