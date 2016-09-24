package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import de.htwsaar.model.Notification;

/**
 * The NotificationDao Class encapsulates the database access for the notifications
 * entity. It provides methods for loading and updating notifications Objects.
 * 
 * @author Oliver Seibert
 *
 */
@Component("notificationDao")
public class NotificationDao {

	private NamedParameterJdbcTemplate jdbc;
	
	public NotificationDao(){}
	
	@Autowired
	public NotificationDao(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}
	
	/**
	 * This method returns a list of all notifications that are stored in the
	 * database and were not sent yet.
	 * 
	 * @return a list of Author Objects
	 */
	public List<Notification> getNotificationsNotSent() {

		String query = "select u.email, n.* from notifications n, users u where n.sentAt is null and n.username = u.username";

		return jdbc.query(query, new NotificationRowMapper());
	}
	
	/**
	 * This Method updates the sentAt column of an Notification when it was sent
	 * 
	 * @param notification
	 *            - the Notification Object that should be updated
	 */
	public void updateNotificationSent(Notification notification) {

		String sql = "update notifications set sentAt = sysdate() where notificationId = :notificationId";

		MapSqlParameterSource paramSource = getNotificationParameterSource(notification);

		jdbc.update(sql, paramSource);
	}
	
	private MapSqlParameterSource getNotificationParameterSource(Notification notification) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("notificationId", notification.getNotificationID());
		paramSource.addValue("username", notification.getUsername());
		paramSource.addValue("type", notification.getType());
		paramSource.addValue("body", notification.getBody());
		paramSource.addValue("subject", notification.getSubject());
		paramSource.addValue("createdAt", notification.getCreatedAt());

		return paramSource;
	}
	
	/**
	 * This class serves as a utility to create Notification Objects out of a
	 * ResultSet that is received from a database query.
	 */
	private class NotificationRowMapper implements RowMapper<Notification> {

		@Override
		public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {

			Notification notification = new Notification();

			try {
				notification.setEmail(rs.getString("email"));
				notification.setNotificationID(rs.getLong("notificationId"));
				notification.setUsername(rs.getString("username"));
				notification.setType(rs.getString("type"));
				notification.setBody(rs.getString("body"));
				notification.setSubject(rs.getString("subject"));
				notification.setCreatedAt(rs.getTimestamp("createdAt"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return notification;
		}
	}
}
