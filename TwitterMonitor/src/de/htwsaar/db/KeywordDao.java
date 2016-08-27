package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

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

	private NamedParameterJdbcTemplate jdbc;

	public KeywordDao() {
	}

	@Autowired
	public KeywordDao(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	/**
	 * This method returns a list of all keywords that are stored in the
	 * database and match the specified user
	 * 
	 * Used in UserService to display users keywords.
	 * 
	 * @param username
	 *            - the unique identifier of a user
	 * @return a list of Keyword Objects
	 * @throws Exception 
	 */
//	public List<Keyword> getKeywords(String username, boolean positive) {											// POS/NEG
	public List<Keyword> getKeywords(String username) {

//		String query = "select * from keywords where username = :username and positive = :positive";				// POS/NEG
		String query = "select * from keywords where username = :username";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
//		paramSource.addValue("positive", positive);																	// POS/NEG

		List<Keyword> keywords = null;
		try {
			keywords = jdbc.query(query, paramSource, new KeywordRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			keywords = new ArrayList<Keyword>();
		}

		return keywords;
	}

	/**
	 * This method returns an array of all distinct, positive keywords stored in the
	 * database. Duplicates will not be loaded.
	 * 
	 * Used in StreamService to initialize the stream.
	 * 
	 * @return an array of Keyword Objects
	 */
	public String[] getKeywords() {

//		String query = "select distinct keyword from keywords where positive = true";						// POS/NEG
		String query = "select distinct keyword from keywords";

		List<String> keywords = null;
		try {
			keywords = jdbc.query(query, new StringRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			keywords = new ArrayList<String>();
		}

		String[] keywordArray = new String[keywords.size()];
		for (int i = 0; i < keywords.size(); i++)
			keywordArray[i] = keywords.get(i);

		return keywordArray;
	}

	/**
	 * This method deletes a keyword from the database.
	 * 
	 * Used in UserService to delete Keywords.
	 * 
	 * @param keyword
	 *            - the keyword that should be deleted (keyword and username are
	 *            primary key in the table)
	 */
	public void deleteKeyword(Keyword keyword) {

		String delete = "delete from keywords where keyword=:keyword and username=:username";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("keyword", keyword.getKeyword());
		paramSource.addValue("username", keyword.getUsername());

		try {
			jdbc.update(delete, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method saves or updates a keyword in the database. If the keyword,
	 * identified by its name, doesn't exist it is added to the database. If the
	 * keyword does exist, its priority and active status get updated.
	 * 
	 * Used in UserService to insert Keyword. 
	 * Used in UserService to switch Keyword active/inactive.
	 * 
	 * @param keyword
	 *            - the keyword that should be stored or updated
	 */
	public void insertKeyword(Keyword keyword) {

//		String insert = "insert into keywords (keyword, username, priority, positive, active)"		// POS/NEG
//				+ " values (:keyword, :username, :priority, :positive, :active)"					// POS/NEG
//				+ " on duplicate key update priority=:priority, active=:active";					// POS/NEG
		String insert = "insert into keywords (keyword, username, priority, active)"
				+ " values (:keyword, :username, :priority, :active)"
				+ " on duplicate key update priority=:priority, active=:active";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("keyword", keyword.getKeyword());
		paramSource.addValue("username", keyword.getUsername());
		paramSource.addValue("priority", keyword.getPriority());
//		paramSource.addValue("positive", keyword.isPositive());					// POS/NEG
		paramSource.addValue("active", keyword.getActive());

		try {
			jdbc.update(insert, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This class serves as a utility to create Keyword Objects out of a
	 * ResultSet that is received from a database query.
	 */
	private class KeywordRowMapper implements RowMapper<Keyword> {

		@Override
		public Keyword mapRow(ResultSet rs, int rowNum) throws SQLException {

			Keyword keyword = new Keyword();

			try {
				keyword.setKeyword(rs.getString("keyword"));
				keyword.setUsername(rs.getString("username"));
				keyword.setPriority(rs.getInt("priority"));
//				keyword.setPositive(rs.getBoolean("positive"));		// POS/NEG	
				keyword.setActive(rs.getBoolean("active"));
			} catch (KeywordException e) {
				keyword = null;
				e.printStackTrace();
			}

			return keyword;
		}
	}

	/**
	 * This class serves as a utility to create keywords (just the raw String
	 * Object) out of a ResultSet that is received from a database query.
	 */
	private class StringRowMapper implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("keyword");
		}
	}
}