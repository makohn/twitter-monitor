package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.htwsaar.exception.model.UserException;
import de.htwsaar.model.User;

/**
 * The UserDao Class encapsulates the database access for the user entity.
 * It provides methods for loading and storing User Objects.
 * 
 * @author Philipp Schaefer, Niko Kleer, Martin Feick
 *
 */
@Component("userDao")
public class UserDao {

	private NamedParameterJdbcTemplate jdbc;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDao() {
	}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public User getUser(String username) {
		
		String query = "select * from users natural join authorities where username = :username";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		
		User user = null;
		try {
			user = jdbc.queryForObject(query, paramSource, new UserRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	/**
	 * This method inserts a user into the database.
	 * @param user - the User Object that should be stored.
	 */
	public void insertUser(User user) {

		String insertUsers = "insert into users (username, email, password, enabled) values (:username, :email, :password, :enabled)";
		String insertAuthorities = "insert into authorities (username, authority) values (:username, :authority)";
		String insertPreferences = "insert into user_x_preferences (username, preferenceType, value) values (:username, 'not', 0)";
		
		MapSqlParameterSource paramSource = getUserParameterSource(user);

		try {
			jdbc.update(insertUsers, paramSource);
			jdbc.update(insertAuthorities, paramSource);
			jdbc.update(insertPreferences, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}	
	
	public void changePassword(String username, String password) {
		String changePassword = "update users set password = :password where username = :username";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		paramSource.addValue("password", passwordEncoder.encode(password));
		
		try {
			jdbc.update(changePassword, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public void changeEmail(String username, String email) {
		String changeEmail = "update users set email = :email where username = :username";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		paramSource.addValue("email", email);
		
		try {
			jdbc.update(changeEmail, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUser(String username) {
																																																																																						
		String deleteKeywords = "delete from keywords where username = :username";
		String deleteAuthority = "delete from authorities where username = :username";
		String deleteUser = "delete from users where username = :username";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);

		try {
			jdbc.update(deleteKeywords, paramSource);
			jdbc.update(deleteAuthority, paramSource);
			jdbc.update(deleteUser, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void enableNotifications(String username, boolean enabled) {
		
		String enableNotifications = "update user_x_preferences set value = :enabled "
				+ "where preferenceType = 'not' and username = :username";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		paramSource.addValue("enabled", enabled);
		
		try {
			jdbc.update(enableNotifications, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNotificationEnabled(String username) {
		String query = "select value from user_x_preferences where username = :username and preferenceType = 'not'";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		
		Integer isEnabled = 0;
		try {
			isEnabled = jdbc.queryForObject(query, paramSource, Integer.class);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return (isEnabled.intValue() == 1);
	}
	
	private MapSqlParameterSource getUserParameterSource(User user) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("username", user.getUsername());
		paramSource.addValue("enabled", user.getEnabled());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("password", passwordEncoder.encode(user.getPassword()));
		paramSource.addValue("authority", user.getAuthority());

		return paramSource;
	}
	
	private class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();

			try {
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEnabled(rs.getBoolean("enabled"));
				user.setEmail(rs.getString("email"));
				user.setAuthority(rs.getString("authority"));
			} catch (UserException e) {
				e.printStackTrace();
			}
			return user;
		}
	}
}

