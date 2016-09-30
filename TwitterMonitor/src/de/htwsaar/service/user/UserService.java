package de.htwsaar.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.UserDao;
import de.htwsaar.model.User;

@Service
public class UserService {

	private UserDao userDao;
//	private KeywordDao keywordDao;

	@Autowired
	public UserService(UserDao userDao/*, KeywordDao keywordDao*/) {
		this.userDao = userDao;
//		this.keywordDao = keywordDao;
	}

	public void insertUser(User user) {
		userDao.insertUser(user);
	}

	public void deleteUser(String username) {
		userDao.deleteUser(username);
	}
	
	public void changePassword(String username, String password) {
		userDao.changePassword(username, password);		
	}

	public void changeEmail(String username, String newEmail) {
		userDao.changeEmail(username, newEmail);		
	}

	public String getEmail(String username) {
		return userDao.getUser(username).getEmail();
	}
	
	public void enableNotifications(String username, boolean enabled) {
		userDao.enableNotifications(username, enabled);
	}
	
	public boolean isNotificationEnabled(String username) {
		return userDao.isNotificationEnabled(username);
	}

//	public void insertKeyword(Keyword keyword) {
//		keywordDao.insertKeyword(keyword);
//	}
//
//	public void deleteKeyword(Keyword keyword) {
//		keywordDao.deleteKeyword(keyword);
//	}
//
//	public List<Keyword> getKeywords(String username, boolean positive) {
//		return keywordDao.getKeywords(username, positive);
//	}
//
//	public void switchActive(Keyword keyword) {
//		keyword.setActive(!keyword.getActive());
//		keywordDao.insertKeyword(keyword);
//	}
}
