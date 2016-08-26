package de.htwsaar.util;

import java.util.List;

import de.htwsaar.model.Notification;
import de.htwsaar.service.user.NotificationService;

/**
 * Versendet automatisiert Benachrichtigungen an die Benutzer
 * @author Oliver Seibert
 *
 */
public class NotificationManager {
	
	private NotificationService notificationService = new NotificationService();
	private MailSender mailSender = new MailSender();
	private final String TYPE_EMAIL = "ema";
	
	/**
	 * Versendet eine Benachrichtigung und markiert sie entsprechend als versendet
	 */
	public void sendNotifications(){
		List<Notification> notificationList = notificationService.getNotificationsNotSent();
		
		for (Notification notification: notificationList){
			switch (notification.getType())
			{
				case TYPE_EMAIL:
					mailSender.sendMail(notification.getEmail(), notification.getSubject(), notification.getBody());
					notificationService.updateNotificationSent(notification);
					break;
				default:
					System.out.println("Notification-Type " + notification.getType() + " wird nicht unterstützt");
					break;
			}
		}		
	}
}
