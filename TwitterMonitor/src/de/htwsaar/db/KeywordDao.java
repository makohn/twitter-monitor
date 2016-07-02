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

import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.model.Keyword;

@Component("keywordDao")
public class KeywordDao {

	private NamedParameterJdbcTemplate jdbc;

	public KeywordDao() {}
	
	@Autowired
	public KeywordDao(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}
	
	public List<Keyword> getKeywords(String username) {

		String query = "select * from keywords where username = :username";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);

		return jdbc.query(query, paramSource, new KeywordRowMapper()); 
	}
	
	public String[] getKeywords() {
		
		String query = "select distinct keyword from keywords";
		
		List<String> keywords = jdbc.query(query, new StringRowMapper()); 
		
		String[] keywordArray = new String[keywords.size()];
		for (int i=0; i<keywords.size(); i++)
			keywordArray[i] = keywords.get(i);
		
		return keywordArray;
	}

	public void insertKeyword(Keyword keyword) {

		String insert = "insert into keywords (keyword, username, priority, active)"
								   + " values (:keyword, :username, :priority, :active)";
//								   + " on duplicate key update priority=:priority, active=:active";

		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();		
		paramSource.addValue("keyword", keyword.getKeyword());
		paramSource.addValue("username", keyword.getUsername());
		paramSource.addValue("priority", keyword.getPriority());
		paramSource.addValue("active", keyword.getActive());

		jdbc.update(insert, paramSource);
	}

	public void deleteKeyword(Keyword keyword) {

		String delete = "delete from keywords where keyword=:keyword and username=:username)";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("keyword", keyword.getKeyword());
		paramSource.addValue("username", keyword.getUsername());

		jdbc.update(delete, paramSource);
	}

	public List<String> getKeywordsOfTweet(long tweetId) {

		String query = "select * from tweets_x_keywords where tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);

		return jdbc.query(query, paramSource, new StringRowMapper());
	}
	
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
	
	private class StringRowMapper implements RowMapper<String> {
		
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("keyword");				
		}
	}
}