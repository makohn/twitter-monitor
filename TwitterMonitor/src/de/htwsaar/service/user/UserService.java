package de.htwsaar.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.KeywordDao;
import de.htwsaar.db.UserDao;
import de.htwsaar.model.Keyword;
import de.htwsaar.model.User;

@Service
public class UserService {
	
	private UserDao userDao;
	private KeywordDao keywordDao;
	
	@Autowired
	public UserService(UserDao userDao, KeywordDao keywordDao) {
		this.userDao = userDao;
		this.keywordDao = keywordDao;
	}

	public void insertUser(User user) {
		userDao.insertUser(user);		
	}

//	public List<User> getUsers() {
//		return userDao.getUsers();
//	}
	
//	public void joinCategory(String username, String category) {
//		userDao.joinCategory(username, category);
//	}
//	
//	public void leaveCategory(String username, String category) {
//		userDao.leaveCategory(username, category);
//	}
	
	public void deleteUser(String username) {
		userDao.deleteUser(username);
	}
	
	public void insertKeyword(Keyword keyword) {
		keywordDao.insertKeyword(keyword);		
	}
	
	public void deleteKeyword(Keyword keyword) {
		keywordDao.deleteKeyword(keyword);
	}
	
//	public List<Keyword> getKeywords(String username) {
//		return keywordDao.getKeywords(username);
//	}
	
	public List<Keyword> getKeywords(String username, boolean positive) {
		return keywordDao.getKeywords(username, positive);
	}
	
	public void switchActive(Keyword keyword) {
		keyword.setActive(!keyword.getActive());
		keywordDao.insertKeyword(keyword);		
	}
	
//	public void setNotificationThreshold(String username, int threshold) {
//		userDao.setNotificationThreshold(username, true, threshold)
//	}
	

//	public void enableNotifications(String username, boolean enabled) {
//		userDao.enableNotifications(username, enabled);
//	}
	
	public void sendNotifications() {
		
		
		
	}
}
