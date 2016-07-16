package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import de.htwsaar.db.psc.AllKeywordStatementCreator;
import de.htwsaar.db.psc.DeleteKeywordStatementCreator;
import de.htwsaar.db.psc.InsertKeywordStatementCreator;
import de.htwsaar.db.psc.TweetKeywordStatementCreator;
import de.htwsaar.db.psc.UserKeywordStatementCreator;
import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.model.Keyword;

/**
 * The KeywordDao Class encapsulates the database access for the keyword entity.
 * It provides methods for loading and storing Keyword Objects.
 * 
 * @author Philipp Schaefer, Marek Kohn
 *
 */

@Component("keywordDao")
public class KeywordDao {

	private JdbcTemplate jdbc;

	public KeywordDao() {}
	
	@Autowired
	public KeywordDao(DataSource jdbc) {
		this.jdbc = new JdbcTemplate(jdbc);
	}
	
	/**
	 * This method returns a list of all keywords that are stored in
	 * the database and match the specified user
	 * @param username - the unique identifier of a user
	 * @return a list of Keyword Objects
	 */
	public List<Keyword> getKeywords(String username) {

		return jdbc.query(new UserKeywordStatementCreator(username), new KeywordRowMapper());
	}
	
	/**
	 * This method returns an array of all distinct keywords stored
	 * in the database. Duplicates will not be loaded.
	 * 
	 * @return an array of Keyword Objects
	 */
	public String[] getKeywords() {
		
		List<String> keywords = jdbc.query(new AllKeywordStatementCreator(), new StringRowMapper()); 
		
		String[] keywordArray = new String[keywords.size()];
		for (int i=0; i<keywords.size(); i++)
			keywordArray[i] = keywords.get(i);
		
		return keywordArray;
	}

	/**
	 * This method saves or updates a keyword in the database.
	 * If the keyword, identified by its name, doesn't exist it is
	 * added to the database.If the keyword does exist, its priority 
	 * and active status get updated.
	 * @param keyword - the keyword that should be stored or updated
	 */
	public void insertKeyword(Keyword keyword) {

		jdbc.update(new InsertKeywordStatementCreator(keyword));
	}

	/**
	 * This method deletes a keyword from the database.
	 * @param keyword - the keyword that should be deleted
	 * (keyword and username are primary key in the table)
	 */
	public void deleteKeyword(Keyword keyword) {
		
		jdbc.update(new DeleteKeywordStatementCreator(keyword.getUsername(), keyword.getKeyword()));
	}

	/**
	 * This method returns all the keywords that are associated with
	 * a tweet. 
	 * @param tweetId - the unique identifier of a Tweet
	 */
	public List<String> getKeywordsOfTweet(long tweetId) {
		
		return jdbc.query(new TweetKeywordStatementCreator(tweetId), new StringRowMapper());
	}
	
	/**
	 * This class serves as a utility to create Keyword Objects out
	 * of a ResultSet that is received from a database query.
	 */
	private class KeywordRowMapper implements RowMapper<Keyword> {
		
		@Override
		public Keyword mapRow(ResultSet rs, int rowNum) throws SQLException {

			Keyword keyword = new Keyword();

			try {
				keyword.setKeyword(rs.getString("keyword"));				
				keyword.setUsername(rs.getString("username"));
				keyword.setPriority(rs.getInt("priority"));		
				keyword.setActive(rs.getBoolean("active"));
			} catch (KeywordException e) {
				keyword = null;
				e.printStackTrace();
			}
			
			return keyword;
		}
	}
	
	/**
	 * This class serves as a utility to create keywords (just the raw
	 * String Object) out of a ResultSet that is received from a
	 * database query.
	 */
	private class StringRowMapper implements RowMapper<String> {
		
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("keyword");				
		}
	}
}