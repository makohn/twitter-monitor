package de.htwsaar.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.NotificationDao;
import de.htwsaar.model.Notification;

/**
 * Service zum Verwalten der Benachrichtigungen
 * @author Oliver Seibert
 *
 */
@Service
public class NotificationService {
	private NotificationDao notificationDao = new NotificationDao();
	
	public NotificationService(){}
	
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
}
