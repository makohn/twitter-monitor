package de.htwsaar.util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import de.htwsaar.model.Notification;
import de.htwsaar.service.user.NotificationService;

/**
 * Stellt einen Dienst bereit zum automatisierten Versenden von Benachrichtigungen an die Benutzer
 * @author Oliver Seibert
 *
 */
@Component
public class NotificationManager {
	
	private NotificationService notificationService;
	private MailSender mailSender = new MailSender();
	private final String TYPE_EMAIL = "ema";
	private Timer timer;
	
	/**
	 * Startet den Dienst und sieht alle 5 Minuten nach, ob neue Benachrichtigungen vorhanden sind
	 */
	@PostConstruct
	public void start(){
		TimerTask action = new TimerTask() {
            public void run() {
            	sendNotifications();
            }
        };
        timer = new Timer();
        timer.schedule(action, 1000, 300000);
	}
	
	/**
	 * Stop den Dienst
	 */
	public void stop(){
		timer.cancel();
	}
	
	/**
	 * Versendet eine Benachrichtigung und markiert sie entsprechend als versendet
	 */
	private void sendNotifications(){
		List<Notification> notificationList = notificationService.getNotificationsNotSent();
		
		for (Notification notification: notificationList){
			System.out.println("Versende: " + notification);
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
