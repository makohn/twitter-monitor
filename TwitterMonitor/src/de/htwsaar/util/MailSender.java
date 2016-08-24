package de.htwsaar.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 * Klasse zum Versenden der täglich Mails mit Top-Tweets
 * @author Marius Backes
 */

public class MailSender {
	/* Angaben zum Mailserver */
	private final String emailSMTPserver = "smtp.gmail.com";
	private final String emailServerPort = "587";

	/* Empfaengeradresse, Betreff und Inhalt der Mail */
	private String empfaengerMailAdresse = "test@mail.de";
	private String emailSubject = "TOP-Tweets des Tages";
	private String emailBody = "Inhalt des Tweets";

	public void sendMail(String empfaengerMailAdresse, String emailSubject, String emailBody) {
		this.empfaengerMailAdresse = empfaengerMailAdresse;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.user", SMTPAuthenticator.getSenderMailAdresse());
		props.put("mail.smtp.host", emailSMTPserver);
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", emailServerPort);
		//props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//props.put("mail.smtp.socketFactory.fallback", "true");
		
		SecurityManager security = System.getSecurityManager();
		try {
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			MimeMessage message = new MimeMessage(session);
			message.setText(emailBody);
			message.setSubject(emailSubject);
			message.setFrom(new InternetAddress(SMTPAuthenticator.getSenderMailAdresse()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(empfaengerMailAdresse));
			Transport.send(message);
			
			System.out.println("E-Mail erfolgreich versendet!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}