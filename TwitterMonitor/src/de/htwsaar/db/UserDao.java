package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import de.htwsaar.exception.model.UserException;
import de.htwsaar.model.User;

/**
 * The UserDao Class encapsulates the database access for the user entity. It
 * provides methods for loading and storing User Objects.
 * 
 * @author Philipp Schaefer
 *
 */
@Component("userDao")
public class UserDao {

	private NamedParameterJdbcTemplate jdbc;

	public UserDao() {
	}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	// /**
	// * This method returns a list of all user that are stored in the data base
	// * including their authority.
	// *
	// * @return a list of User Objects
	// */
	// public List<User> getUsers() {
	//
	// String query = "select * from users natural join authorities";
	//
	// List<User> users = null;
	// try {
	// users = jdbc.query(query, new UserRowMapper());
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// users = new ArrayList<User>();
	// }
	// return users;
	// }
	//
	// /**
	// * This method loads a single user from the database.
	// *
	// * @param username
	// * - the unique id of a user
	// * @return an User Object, if the user exists or null if not.
	// */
	// public User getUser(String username) {
	//
	// String query = "select * from users natural join authorities where
	// username = :username";
	//
	// MapSqlParameterSource paramSource = new MapSqlParameterSource();
	// paramSource.addValue("username", username);
	//
	// User user = null;
	// try {
	// user = (User) jdbc.queryForObject(query, paramSource, new
	// UserRowMapper());
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// }
	//
	// return user;
	// }

	/**
	 * This method inserts a user into the database.
	 * 
	 * @param user
	 *            - the User Object that should be stored.
	 */
	public void insertUser(User user) {

		String insertUsers = "insert into users (username, email, password, enabled) values (:username, :email, :password, :enabled)";
		// + " on duplicate key update email=:email, password=:password,
		// enabled=:enabled";
		String insertAuthorities = "insert into authorities (username, authority) values (:username, :authority)";
		// + " on duplicate key update username=:username,
		// authority=:authority";
		// String insertNotifications = "insert into notifications (username,
		// enabled, threshold) values (:username, :enabled, :threshold)";

		MapSqlParameterSource paramSource = getUserParameterSource(user);

		try {
			jdbc.update(insertUsers, paramSource);
			jdbc.update(insertAuthorities, paramSource);
			// jdbc.update(insertNotifications, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

//	public void setNotificationThreshold(String username, int threshold) {
//		
//		 String setNotificationThreshold = "update notifications set threshold = :threshold where username = :username";
//		
//		 MapSqlParameterSource paramSource = new MapSqlParameterSource();
//		 paramSource.addValue("username", username);
//		 paramSource.addValue("threshold", threshold);
//		
//		 try {
//		 	jdbc.update(setNotificationThreshold, paramSource);
//		 } catch (DataAccessException e) {
//		 	e.printStackTrace();
//		 }
//	}
	
//	public void enableNotifications(String username, boolean enabled) {
//		
//		 String setNotificationsEnabled = "update notifications set enabled = :enabled where username = :username";
//		
//		 MapSqlParameterSource paramSource = new MapSqlParameterSource();
//		 paramSource.addValue("username", username);
//		 paramSource.addValue("enabled", enabled);
//		
//		 try {
//			 jdbc.update(setNotificationsEnabled, paramSource);
//		 } catch (DataAccessException e) {
//			 e.printStackTrace();
//		 }
//	}

	// public void setNotifications(String username, boolean enabled, int
	// threshold) {
	//
	// // + " on duplicate key update username=:username,
	// // authority=:authority";
	// String insertNotifications = "insert into notifications (username,
	// enabled, threshold) values (:username, :enabled, :threshold) "
	// + "on duplicate key update enabled = :enabled, threshold = :threshold";
	//
	// MapSqlParameterSource paramSource = new MapSqlParameterSource();
	// paramSource.addValue("username", username);
	// paramSource.addValue("enabled", enabled);
	// paramSource.addValue("threshold", threshold);
	//
	// try {
	// jdbc.update(insertNotifications, paramSource);
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// }
	// }

	// public boolean isNotified(String username) {
	//
	// String queryNotified = "select * from notifications where username =
	// :username";
	//
	// MapSqlParameterSource paramSource = new MapSqlParameterSource();
	// paramSource.addValue("username", username);
	//
	// boolean notified = false;
	// try {
	// notified = (boolean) jdbc.queryForObject(queryNotified, paramSource, new
	// NotifyRowMapper());
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// }
	//
	// return notified;
	// }

	// public List<User> getNotified() {
	//
	// String queryNotified = "select * from users, authorities, notifications "
	// + "where users.username = authorities.username "
	// + "and users.username = notifications.username";
	//
	// List<User>notified = null;
	// try {
	// notified = jdbc.query(queryNotified, new UserRowMapper());
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// notified = new ArrayList<User>();
	// }
	//
	// return notified;
	// }

	public void deleteUser(String username) {

		String deleteKeywords = "delete from keywords where username = :username";
		String deleteAuthority = "delete from authorities where username = :username";
		// String deleteNotifications = "delete from notifications where
		// username = :username";
		String deleteUser = "delete from users where username = :username";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);

		try {
			jdbc.update(deleteKeywords, paramSource);
			jdbc.update(deleteAuthority, paramSource);
			// jdbc.update(deleteNotifications, paramSource);
			jdbc.update(deleteUser, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	// public void joinCategory(String username, String category) {
	//
	// String insertCategory = "insert into users_x_categories (username,
	// category) values (:username, :category)";
	//
	// MapSqlParameterSource paramSource = new MapSqlParameterSource();
	// paramSource.addValue("username", username);
	// paramSource.addValue("category", category);
	//
	// try {
	// jdbc.update(insertCategory, paramSource);
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// }
	// }

	// public void leaveCategory(String username, String category) {
	//
	// String deleteCategory = "delete from users_x_categories where username =
	// :username and category = :category)";
	//
	// MapSqlParameterSource paramSource = new MapSqlParameterSource();
	// paramSource.addValue("username", username);
	// paramSource.addValue("category", category);
	//
	// try {
	// jdbc.update(deleteCategory, paramSource);
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// }
	// }

	private MapSqlParameterSource getUserParameterSource(User user) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("username", user.getUsername());
		paramSource.addValue("enabled", user.getEnabled());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("password", user.getPassword());
		paramSource.addValue("authority", user.getAuthority());
		// paramSource.addValue("enabled", user.getNotificationsEnabled());
		// paramSource.addValue("threshold", user.getNotificationThreshold());

		return paramSource;
	}

//	/**
//	 * This class serves as a utility to create User Objects out of a ResultSet
//	 * that is received from a database query.
//	 */
//	private class UserRowMapper implements RowMapper<User> {
//
//		@Override
//		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//			User user = new User();
//
//			try {
//				user.setUsername(rs.getString("username"));
//				user.setEmail(rs.getString("email"));
//				user.setPassword(rs.getString("password"));
//				user.setAuthority(rs.getString("authority"));
//				// user.setNotificationsEnabled(rs.getBoolean("enabled"));
//				// user.setNotificationThreshold(rs.getInt("threshold"));
//			} catch (UserException e) {
//				e.printStackTrace();
//			}
//			return user;
//		}
//	}
}
