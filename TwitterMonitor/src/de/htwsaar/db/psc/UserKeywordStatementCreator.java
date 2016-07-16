package de.htwsaar.db.psc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Creates a PreparedStatement to query the keywords of a certain user.
 *
 */
public class UserKeywordStatementCreator implements PreparedStatementCreator {

	private String query;
	private String username;
	
	public UserKeywordStatementCreator(String username) {
		this.query = "select * from keywords where username=?";
		this.username = username;
	}		
			
	@Override
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);			
		preparedStatement.setString(1, username);			
		return preparedStatement;
	}		
}