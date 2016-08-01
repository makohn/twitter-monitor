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
import de.htwsaar.model.OutputTweet;
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

	private KeywordDao keywordDao;
		
	public TweetDao() {}
	
	@Autowired
	public TweetDao(DataSource jdbc, KeywordDao keywordDao) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
		this.keywordDao = keywordDao;
	}

	/**
	 * This method returns a list of all tweets that are stored in
	 * the database.
	 * @return a list of Tweet Objects
	 */
	public List<OutputTweet> getTweets() {

		String query = "select * from tweets, tweetAuthors where tweets.authorId = tweetAuthors.authorId limit 20";		
															// TODO: wenn die Methode ALLE tweets liefern soll
															// ist das limit doch irgendwie nicht so sinnvoll
															// oder geht es hier nur um testzwecke
		
		List<OutputTweet> tweets = jdbc.query(query, new TweetRowMapper());
		
		return tweets;
	}

	/**
	 * This method loads a single tweet from the database.
	 * @param tweetId - the unique id of a tweet
	 * @return a Tweet Object, if the tweet exists.
	 */
	public OutputTweet getTweet(long tweetId) {

		String query = "select * from tweets, tweetAuthors where tweets.authorId = tweetAuthors.authorId and tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);
		
		try {
			return (OutputTweet) jdbc.queryForObject(query, paramSource, new TweetRowMapper()); 		
		}
		catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
			
	}
	
	/**
	 * This method loads all tweets of a certain user from the database.
	 * 
	 * @param username
	 * @return
	 */
	public List<OutputTweet> getTweets(String username) {

//		String query = "select * from keywords, tweets_x_keywords, tweets, tweetauthors "
//						+ "where keywords.keyword = tweets_x_keywords.keyword " 
//						+ "and tweets_x_keywords.tweetId = tweets.tweetId "
//						+ "and tweets.authorId = tweetAuthors.authorId "
//						+ "and keywords.username = :username";
		
		String query = "select * from tweets, tweetauthors where tweets.authorId = tweetauthors.authorId limit 20";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		
		try {																	// TODO: ist hier der try-catch notwendig
			return jdbc.query(query, paramSource, new TweetRowMapper()); 		// bei leerem Ergebnis wird ja eigentlich nur
		} catch (EmptyResultDataAccessException e) {							// eine leere liste zur�ckgegeben.
			e.printStackTrace();
		}
		return null;			
	}

	/**
	 * This method inserts tweets into the database. 
	 * If the tweet already exists, its  retweet/favorite count
	 * are being updated.
	 * @param tweet - the Tweet Object that should be stored.
	 */
	public void insertTweet(Tweet tweet) {

		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, image, createdAt) values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :image, :createdAt)"
							+ " on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";

		MapSqlParameterSource paramSource = getTweetParamSource(tweet);

		jdbc.update(insert, paramSource);
	}
	
	/**
	 * This method inserts a list of multiple tweets at once into
	 * the database. Duplicates are handled as in the insertTweet method.
	 * @param tweets - a list of tweets that should be stored.
	 */
	public void insertTweets(List<Tweet> tweets) {
		
		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, image, createdAt) values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :image, :createdAt)"
				+ " on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";
		
		SqlParameterSource[] paramSources = new MapSqlParameterSource[tweets.size()];		
		for (int i=0; i<tweets.size(); i++) {
				
			MapSqlParameterSource paramSource = getTweetParamSource(tweets.get(i));			
			paramSources[i] = paramSource;
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

		jdbc.update(delete, paramSource);
	}

	/**
	 * This method returns a list of all tweets that contain a certain
	 * passed keyword.
	 * @param keyword - the requested keyword
	 * @return a list of tweets that match with the keyword
	 */
	public List<OutputTweet> getTweetsWithKeyword(String keyword) {

		String query = "select * from tweets_x_keywords where keyword = :keyword";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("keyword", keyword);

		return jdbc.query(query, paramMap, new TweetRowMapper());
	}
	
	private MapSqlParameterSource getTweetParamSource(Tweet tweet) {
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		
		paramSource.addValue("tweetId", tweet.getTweetId());
		paramSource.addValue("authorId", tweet.getAuthorId());
		paramSource.addValue("text", tweet.getText());
		paramSource.addValue("favoriteCount", tweet.getFavoriteCount());
		paramSource.addValue("retweetCount", tweet.getRetweetCount());
		paramSource.addValue("place", tweet.getPlace());
		paramSource.addValue("image", tweet.getImage());
		paramSource.addValue("createdAt", tweet.getCreatedAt());
		
		return paramSource;
	}
	
	/**
	 * This class serves as a utility to create Tweet Objects out
	 * of a ResultSet that is received from a database query.
	 */
	private class TweetRowMapper implements RowMapper<OutputTweet> {

		@Override
		public OutputTweet mapRow(ResultSet rs, int rowNum) throws SQLException {
			OutputTweet tweet = new OutputTweet();

			try {
				tweet.setTweetId(rs.getLong("tweetId"));
				tweet.setAuthorId(rs.getLong("authorId"));
				tweet.setText(rs.getString("text"));
				tweet.setCreatedAt(rs.getDate("createdAt"));
				tweet.setPlace(rs.getString("place"));
				tweet.setImage(rs.getString("image"));
				tweet.setFavoriteCount(rs.getInt("favoriteCount"));
				tweet.setRetweetCount(rs.getInt("retweetCount"));				
				tweet.setFollowerCount(rs.getInt("followerCount"));
				tweet.setPriority(rs.getFloat("prio"));
				tweet.setName(rs.getString("name"));
				tweet.setScreenName(rs.getString("screenName"));
				tweet.setPictureUrl(rs.getString("pictureUrl"));
				
				tweet.setKeywords(keywordDao.getKeywordsOfTweet(rs.getLong("tweetId")));
			} catch (TweetException e) {
				e.printStackTrace();
			}
			return tweet;
		}		
	}
}