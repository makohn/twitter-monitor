package de.htwsaar.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import de.htwsaar.model.User;

/**
 * The UserDao Class encapsulates the database access for the user entity.
 * It provides methods for loading and storing User Objects.
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

	/**
	 * This method inserts a user into the database.
	 * @param user - the User Object that should be stored.
	 */
	public void insertUser(User user) {

		String insertUsers = "insert into users (username, email, password, enabled) values (:username, :email, :password, :enabled)";
		String insertAuthorities = "insert into authorities (username, authority) values (:username, :authority)";
		
		MapSqlParameterSource paramSource = getUserParameterSource(user);

		jdbc.update(insertUsers, paramSource);
		jdbc.update(insertAuthorities, paramSource);
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
	
	private MapSqlParameterSource getUserParameterSource(User user) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("username", user.getUsername());
		paramSource.addValue("enabled", user.getEnabled());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("password", user.getPassword());
		paramSource.addValue("authority", user.getAuthority());

		return paramSource;
	}
}

