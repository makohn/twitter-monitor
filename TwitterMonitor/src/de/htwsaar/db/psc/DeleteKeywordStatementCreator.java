package de.htwsaar.db.psc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Creates a PreparedStatement to delete a keywords of a user.
 *
 */
public class DeleteKeywordStatementCreator implements PreparedStatementCreator {

	private String query;
	private String username;
	private String keyword;		
	
	public DeleteKeywordStatementCreator(String username, String keyword) {
		this.query = "delete from keywords where keyword=? and username=?)";
		this.keyword = keyword;
		this.username = username;
	}		
			
	@Override
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);			
		preparedStatement.setString(1, keyword);
		preparedStatement.setString(2, username);
		return preparedStatement;
	}		
}