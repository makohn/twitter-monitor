package de.htwsaar.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.UserDao;
import de.htwsaar.model.User;

@Service
public class UserService {
	
	private UserDao userDao;
	
	@Autowired
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public void insertUser(User user) {
		userDao.insertUser(user);		
	}

	public List<User> getUsers() {
		return userDao.getUsers();
	}

}
