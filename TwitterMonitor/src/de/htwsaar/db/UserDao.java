package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import de.htwsaar.exceptions.model.UserException;
import de.htwsaar.model.User;

@Component("userDao")
public class UserDao {

	private NamedParameterJdbcTemplate jdbc;

	public UserDao() {
	}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public List<User> getUsers() {

		String query = "select * from benutzer";

		return jdbc.query(query, new RowMapper<User>() {

			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				User user = new User();

				try {
					user.setUserId(rs.getInt("benutzer_id"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("passwort"));
					user.setRegisteredAt(rs.getDate("registrierdatum"));
				} catch (UserException e) {
					e.printStackTrace();
				}

				return user;
			}
		});
	}

	/**
	 * This method loads a User from the database.
	 * 
	 * @param userId
	 * @return
	 */
	public User getUser(int userId) {

		String query = "select * from benutzer where benutzer_id = :userId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);

		List<User> list = jdbc.query(query, paramMap, new RowMapper<User>() {

			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				User user = new User();

				try {
					user.setUserId(rs.getInt("benutzer_id"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("passwort"));
					user.setRegisteredAt(rs.getDate("registrierdatum"));
				} catch (UserException e) {
					e.printStackTrace();
				}
				
				return user;
			}

		});

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	public void insertUser(User user) {

		String insert = "insert into benutzer (benutzer_id, email, passwort) values (:userId, :email, :password)"
				+ " on duplicate key update email=:email, passwort=:password;";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", user.getUserId());
		paramMap.put("email", user.getEmail());
		paramMap.put("password", user.getPassword());

		jdbc.update(insert, paramMap);
	}
}
