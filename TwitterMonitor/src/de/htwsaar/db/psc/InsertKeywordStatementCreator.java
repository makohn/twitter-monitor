package de.htwsaar.db.psc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

import de.htwsaar.model.Keyword;

/**
 * Creates a PreparedStatement to insert or update a keywords.
 *
 */
public class InsertKeywordStatementCreator implements PreparedStatementCreator {

	private String query;
	private Keyword keyword;
	
	public InsertKeywordStatementCreator(Keyword keyword) {
		this.query = "insert into keywords (keyword, username, priority, active)"
				   + " values (?, ?, ?, ?)"
				   + " on duplicate key update priority=?, active=?";
		this.keyword = keyword;
	}		
			
	@Override
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);			
		preparedStatement.setString(1, keyword.getKeyword());
		preparedStatement.setString(2, keyword.getUsername());
		preparedStatement.setInt(3, keyword.getPriority());
		preparedStatement.setBoolean(4, keyword.getActive());
		preparedStatement.setInt(5, keyword.getPriority());
		preparedStatement.setBoolean(6, keyword.getActive());
		return preparedStatement;
	}		
}
