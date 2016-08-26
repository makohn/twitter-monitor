package de.htwsaar.model;

import java.util.Date;

/**
 * Klasse bildet ein Eintrag der Tabelle notifications inkl. der Empfaengeraddresse ab
 * @author Oliver Seibert
 *
 */
public class Notification {
	private long notificationID;
	private String username;
	private String email;
	private String type;
	private String body;
	private String subject;
	private Date createdAt;
	
	public Notification() {}
	
	public Notification(long notificationID, String username, String email, String type, String body, String subject, Date createdAt){
		setNotificationID(notificationID);
		setUsername(username);
		setEmail(email);
		setType(type);
		setBody(body);
		setSubject(subject);
		setCreatedAt(createdAt);
	}
	
	public long getNotificationID() {
		return notificationID;
	}
	public void setNotificationID(long notificationID) {
		this.notificationID = notificationID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
