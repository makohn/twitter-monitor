package de.htwsaar.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.UserDao;
import de.htwsaar.model.User;

@Service
public class UserService {
	
	private UserDao dao;
	
	@Autowired
	public UserService(UserDao dao) {
		this.dao = dao;
	}

	public void insertUser(User user) {
		dao.insertUser(user);		
	}

	public List<User> getUsers() {
		return dao.getUsers();
	}

}
