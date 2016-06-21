package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import de.htwsaar.exception.model.TweetException;
import de.htwsaar.model.Tweet;

/**
 * The TweetDao Class encapsulates the database access for the tweet entity.
 * It provides methods for loading and storing Tweet Objects.
 * 
 * @author Philipp Schaefer, Marek Kohn
 *
 */

//TODO: SQL Statements evtl. in .properties Datei auslagern ?

@Component("tweetDao")
public class TweetDao {

	private NamedParameterJdbcTemplate jdbc;

	public TweetDao() {}
	
	@Autowired
	public TweetDao(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);		
	}

	/**
	 * This method returns a list of all tweets that are stored in
	 * the database.
	 * @return a list of Tweet Objects
	 */
	public List<Tweet> getTweets() {

		String query = "select * from tweets";

		return jdbc.query(query, new TweetRowMapper());
	}

	/**
	 * This method loads a single tweet from the database.
	 * @param tweetId - the unique id of a tweet
	 * @return a Tweet Object, if the tweet exists.
	 */
	public Tweet getTweet(long tweetId) {

		String query = "select * from tweets where tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);
		
		try {
			return (Tweet) jdbc.queryForObject(query, paramSource, new TweetRowMapper()); 
		
		}
		catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
			
	}

	/**
	 * This method returns a list of Media URLs that are connected 
	 * to a tweet (Pictures, Videos,...).
	 * @param tweetId - the unique identifier of the tweet
	 * @return a list of String Objects
	 */
	public List<String> getUrlsOfTweet(long tweetId) {

		String query = "select * from tweetMedia where tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);

		return jdbc.query(query, paramSource, new RowMapper<String>() {

			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("url");
			}
		});
	}

	/**
	 * This method inserts tweets into the database. 
	 * If the tweet already exists, its  retweet/favorite count
	 * are being updated.
	 * @param tweet - the Tweet Object that should be stored.
	 */
	public void insertTweet(Tweet tweet) {

		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, createdAt) values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :createdAt)"
							+ " on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweet.getTweetId());
		paramSource.addValue("authorId", tweet.getAuthorId());
		paramSource.addValue("text", tweet.getText());
		paramSource.addValue("favoriteCount", tweet.getFavoriteCount());
		paramSource.addValue("retweetCount", tweet.getRetweetCount());
		paramSource.addValue("place", tweet.getPlace());
		paramSource.addValue("createdAt", tweet.getCreatedAt());

		jdbc.update(insert, paramSource);

		insertUrlsOfTweet(tweet.getTweetId(), tweet.getUrls());
	}
	
	/**
	 * This method inserts a list of multiple tweets at once into
	 * the database. Duplicates are handled as in the insertTweet method.
	 * @param tweets - a list of tweets that should be stored.
	 */
	public void insertTweets(List<Tweet> tweets) {
		
		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, createdAt) values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :createdAt)"
				+ " on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";
		
		SqlParameterSource[] paramSources = new MapSqlParameterSource[tweets.size()];		
		HashMap<Long, String> urls = new HashMap<Long, String>();
		
		for (int i=0; i<tweets.size(); i++) {
			
			Tweet tweet = tweets.get(i);
			
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			// TODO: Hier wird Code dupliziert, macht es Sinn folgende Zeilen auszulagern ? 
			paramSource.addValue("tweetId", tweet.getTweetId());
			paramSource.addValue("authorId", tweet.getAuthorId());
			paramSource.addValue("text", tweet.getText());
			paramSource.addValue("favoriteCount", tweet.getFavoriteCount());
			paramSource.addValue("retweetCount", tweet.getRetweetCount());
			paramSource.addValue("place", tweet.getPlace());
			paramSource.addValue("createdAt", tweet.getCreatedAt());
			
			for (String url : tweet.getUrls())
				urls.put(tweet.getTweetId(), url);
			
			paramSources[i] = paramSource;
		}
		
		jdbc.batchUpdate(insert, paramSources);
		
		insertUrlsOfTweet(urls);
	}

	/**
	 * This method inserts a single Media URL of a tweet into the
	 * corresponding table in the database.
	 * @param tweetId - the unique id of the tweet
	 * @param url - a Media URL that belongs to the tweet
	 */
	public void insertUrlOfTweet(long tweetId, String url) {

		String insert = "insert into tweetMedia (tweetId, url) values (:tweetId, :url)"
						+ " on duplicate key update url=:url"; //TODO: work around, andere Loesung finden

		MapSqlParameterSource paramSource = new MapSqlParameterSource();			
		paramSource.addValue("tweetId", tweetId);
		paramSource.addValue("url", url);

		jdbc.update(insert, paramSource);
	}
	
	/**
	 * This method inserts multiple Media URLs into the
	 * database. 
	 * @param tweetId - the unique id of the tweet
	 * @param urls - a java.util.List
	 * 					 of Media URLs that belong to the tweet
	 */
	public void insertUrlsOfTweet(long tweetId, List<String> urls) {
		
		String insert = "insert into tweetMedia (tweetId, url) values (:tweetId, :url)"
						+ " on duplicate key update url=:url"; //TODO: work around, andere Loesung finden
		
		SqlParameterSource[] paramSources = new MapSqlParameterSource[urls.size()];
						
		for (int i=0; i<urls.size(); i++) {
			
			MapSqlParameterSource paramSource = new MapSqlParameterSource();			
			paramSource.addValue("tweetId", tweetId);
			paramSource.addValue("url", urls.get(i));
			
			paramSources[i] = paramSource;
		}		
		
		jdbc.batchUpdate(insert, paramSources);		
	}
	
	/**
	 * This method inserts multiple Media URLs into the
	 * database.
	 * @param urls - a java.util.HashMap
	 * 					 of Media URLs that belong to the tweet
	 */
	public void insertUrlsOfTweet(HashMap<Long, String> urls) {
		
		String insert = "insert into tweetMedia (tweetId, url) values (:tweetId, :url)"
						+ " on duplicate key update url=:url"; //TODO: work around, andere Lˆsung finden
		
		SqlParameterSource[] paramSources = new MapSqlParameterSource[urls.size()];		
						
		int i=0;
		for (Long id : urls.keySet()) {			
			
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			
			paramSource.addValue("tweetId", id.longValue());
			paramSource.addValue("url", urls.get(id));
			
			paramSources[i] = paramSource;
			i++;
		}		
		jdbc.batchUpdate(insert, paramSources);		
	}
	
	/**
	 * This method removes a tweet from the database.
	 * @param tweetId - the unique identifier of the tweet.
	 */
	public void deleteTweet(long tweetId) {
		String delete = "delete from tweets where tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);

		deleteUrlsOfTweet(tweetId);
		jdbc.update(delete, paramSource);
	}
	
	/**
	 * This method removes a tweet URL from the database.
	 * @param tweetId - the unique identifier of the tweet.
	 */
	private void deleteUrlsOfTweet(long tweetId) {
		String delete = "delete from tweetMedia where tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);

		jdbc.update(delete, paramSource);
	}

	/**
	 * This method returns a list of all tweets that contain a certain
	 * passed keyword.
	 * @param keyword - the requested keyword
	 * @return a list of tweets that match with the keyword
	 */
	public List<Tweet> getTweetsWithKeyword(String keyword) {

		String query = "select * from tweets_x_keywords where keyword = :keyword";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("keyword", keyword);

		return jdbc.query(query, paramMap, new TweetRowMapper());
	}
	
	/**
	 * This class serves as a utility to create Tweet Objects out
	 * of a ResultSet that is received from a database query.
	 */
	private class TweetRowMapper implements RowMapper<Tweet> {

		@Override
		public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
			Tweet tweet = new Tweet();

			try {
				tweet.setTweetId(rs.getLong("tweetId"));
				tweet.setAuthorId(rs.getLong("authorId"));
				tweet.setText(rs.getString("text"));
				tweet.setCreatedAt(rs.getDate("createdAt"));
				tweet.setPlace(rs.getString("place"));
				tweet.setFavoriteCount(rs.getInt("favoriteCount"));
				tweet.setRetweetCount(rs.getInt("retweetCount"));
				tweet.setUrls(getUrlsOfTweet(rs.getLong("tweetId")));
			} catch (TweetException e) {
				e.printStackTrace();
			}
			return tweet;
		}		
	}
}