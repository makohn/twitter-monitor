package de.htwsaar.db.psc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Creates a PreparedStatement to query all keywords.
*
*/
public class AllKeywordStatementCreator implements PreparedStatementCreator {

	private String query;
	
	public AllKeywordStatementCreator() {
		this.query = "select distinct keyword from keywords";
	}		
			
	@Override
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);			
		return preparedStatement;
	}		
}
