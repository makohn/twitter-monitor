package de.htwsaar.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.htwsaar.db.NotificationDao;
import de.htwsaar.model.Notification;
import de.htwsaar.util.MailSender;

/**
 * Service zum Verwalten der Benachrichtigungen
 * @author Oliver Seibert
 *
 */
@Service
public class NotificationService {
	private static final String TYPE_EMAIL = "ema";
	private static final int FIVE_MINUTES = 5*60*1000;
	
	private NotificationDao notificationDao;
	
	@Autowired
	public NotificationService(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}
	
	public List<Notification> getNotificationsNotSent(){
		return notificationDao.getNotificationsNotSent();
	}
	
	public void updateNotificationSent(Notification notification){
		notificationDao.updateNotificationSent(notification);
	}
	
	/**
	 * Versendet eine Benachrichtigung und markiert sie entsprechend als versendet
	 */
	@Scheduled(fixedDelay = FIVE_MINUTES)
	private void sendNotifications() {
		List<Notification> notificationList = getNotificationsNotSent();

		for (Notification notification : notificationList) {
			System.out.println("Versende: " + notification);
			switch (notification.getType()) {
			case TYPE_EMAIL:
				MailSender.sendMail(notification.getEmail(), notification.getSubject(), notification.getBody());
				updateNotificationSent(notification);
				break;
			default:
				System.out.println("Notification-Type " + notification.getType() + " wird nicht unterstützt");
				break;
			}
		}		
	}
}
