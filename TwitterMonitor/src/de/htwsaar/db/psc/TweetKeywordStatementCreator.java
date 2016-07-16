package de.htwsaar.db.psc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Creates a PreparedStatement to query for all keywords of a tweet.
 *
 */
public class TweetKeywordStatementCreator implements PreparedStatementCreator {

	private String query;
	private long tweetId;		
	
	public TweetKeywordStatementCreator(long tweetId) {
		this.query = "select * from tweets_x_keywords where tweetId = ?";
		this.tweetId = tweetId;
	}		
			
	@Override
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);			
		preparedStatement.setLong(1, tweetId);
		return preparedStatement;
	}		
}
