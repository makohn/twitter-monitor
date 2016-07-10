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

	public List<User> getUsers() {
		return userDao.getUsers();
	}
	
	public void insertKeyword(Keyword keyword) {
		keywordDao.insertKeyword(keyword);		
	}
	
	public List<Keyword> getKeywords(String username) {
		return keywordDao.getKeywords(username);
	}
	
	public void switchActive(Keyword keyword) {
		keyword.setActive(!keyword.getActive());
		keywordDao.insertKeyword(keyword);		
	}
}
