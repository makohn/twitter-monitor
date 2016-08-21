package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author Philipp Schaefer, Marek Kohn, Niko Kleer, Martin Feick
 *
 */

@Component("tweetDao")
public class TweetDao {

	private NamedParameterJdbcTemplate jdbc;
		
	public TweetDao() {}
	
	@Autowired
	public TweetDao(DataSource jdbc, KeywordDao keywordDao) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	/**
	 * This method returns a list of all tweets that are stored in
	 * the database.
	 * @return a list of Tweet Objects
	 */
	public List<OutputTweet> getTweets() {

		String query = "select *, get_general_prio(tweets.tweetId) as prio from tweets, tweetauthors where tweets.`authorId` = tweetauthors.`authorId` limit 20";		
															// TODO: wenn die Methode ALLE tweets liefern soll
															// ist das limit doch irgendwie nicht so sinnvoll
															// oder geht es hier nur um testzwecke
		
		List<OutputTweet> tweets = jdbc.query(query, new TweetRowMapper());
		
		// hier wei� ich noch nicht genau wie wir die keywords in die (limitierte) liste reinkriegen sollen
		
		return tweets;
	}

	/**
	 * This method loads a single tweet from the database.
	 * @param tweetId - the unique id of a tweet
	 * @return a Tweet Object, if the tweet exists.
	 */
	public OutputTweet getTweet(long tweetId) {

		String query = "select *, calc_tweet_prio(tweets.tweetId) prio from tweets, tweetAuthors where tweets.authorId = tweetAuthors.authorId and tweetId = :tweetId";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);
		
		OutputTweet tweet = null;
		try {
			tweet = (OutputTweet) jdbc.queryForObject(query, paramSource, new TweetRowMapper()); 		
		}
		catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		
		query = "select * from tweets_x_keywords where tweetId = :tweetId";		
		tweet.setKeywords(jdbc.query(query, paramSource, new StringRowMapper()));
		
		return tweet;
	}
	
	/**
	 * This method loads all tweets of a certain user from the database.
	 * 
	 * @param username
	 * @return
	 */
	public List<OutputTweet> getTweets(String username) {
		
		String query = "select *, get_personal_prio(tweets.tweetId, :username) prio from tweets, tweetAuthors, tweets_x_keywords, keywords "
				+ "where tweets.authorId = tweetAuthors.authorId "
				+ "and tweets.tweetId = tweets_x_keywords.tweetId "
				+ "and tweets_x_keywords.keyword = keywords.keyword "
				+ "and keywords.username = :username "
				+ "order by prio desc limit 20";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		
		// This query will get all Tweets for the user including one keyword per row.
		// So if there are multiple Keywords associated with a Tweet, there will be multiple identical rows
		// only with different keywords.
		List<OutputTweet> tweetList = null;
		try {																				// TODO: ist hier der try-catch notwendig
			tweetList = jdbc.query(query, paramSource, new TweetKeywordRowMapper()); 		// bei leerem Ergebnis wird ja eigentlich nur
		} catch (EmptyResultDataAccessException e) {										// eine leere liste zur�ckgegeben.
			e.printStackTrace();
		}
		
//		System.out.println("TweetListe1");
//		for (OutputTweet t : tweetList)
//			System.out.println(t);
		
		// Here the tweetList of the query is worked through. If a tweet shows up
		// a second, third ... time, the keyword will be added to the first tweets-list.
		// The first appearances are stored in a HashMap.
		HashMap<Long, OutputTweet> tweetMap = new HashMap<Long, OutputTweet>();
		for (OutputTweet tweet : tweetList) {
			if (tweetMap.containsKey(tweet.getTweetId()))
				tweetMap.get(tweet.getTweetId()).addKeyword(tweet.getKeywords().get(0));
			else
				tweetMap.put(tweet.getTweetId(), tweet);
		}
		
//		System.out.println("TweetListe2");
//		for (OutputTweet t : tweetMap.values())
//			System.out.println(t);
//		
//		System.out.println("TweetListe3");
//		for (OutputTweet t : new ArrayList<OutputTweet>(tweetMap.values()))
//			System.out.println(t);
		
		return new ArrayList<OutputTweet>(tweetMap.values());
	}

	/**
	 * This method inserts tweets into the database. 
	 * If the tweet already exists, its  retweet/favorite count
	 * are being updated.
	 * @param tweet - the Tweet Object that should be stored.
	 */
	public void insertTweet(Tweet tweet) {

		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, image, createdAt) "
							+ "values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :image, :createdAt) "
							+ "on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";

		MapSqlParameterSource paramSource = getTweetParamSource(tweet);

		jdbc.update(insert, paramSource);
	}
	
	/**
	 * This method inserts a list of multiple tweets at once into
	 * the database. Duplicates are handled as in the insertTweet method.
	 * @param tweets - a list of tweets that should be stored.
	 */
	public void insertTweets(List<Tweet> tweets) {
		
		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, image, createdAt) "
							+ "values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :image, :createdAt) "
							+ "on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";
		
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

		String query = "select *, calc_tweet_prio(tweets.tweetId) prio from tweets, tweets_x_keywords where keyword = :keyword";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("keyword", keyword);

		return jdbc.query(query, paramMap, new TweetRowMapper());
	}
	
	/**
	 * This method returns all the keywords that are associated with
	 * a tweet. 
	 * @param tweetId - the unique identifier of a Tweet
	 */
	public List<String> getKeywordsOfTweet(long tweetId) {

		String query = "select * from tweets_x_keywords where tweetId = :tweetId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("tweetId", tweetId);

		return jdbc.query(query, paramSource, new StringRowMapper());
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
				tweet.setCreatedAt(rs.getTimestamp("createdAt"));
				tweet.setPlace(rs.getString("place"));
				tweet.setImage(rs.getString("image"));
				tweet.setFavoriteCount(rs.getInt("favoriteCount"));
				tweet.setRetweetCount(rs.getInt("retweetCount"));				
				tweet.setFollowerCount(rs.getInt("followerCount"));
				tweet.setPriority(rs.getFloat("prio"));
				tweet.setName(rs.getString("name"));
				tweet.setScreenName(rs.getString("screenName"));
				tweet.setPictureUrl(rs.getString("pictureUrl"));
				
			} catch (TweetException e) {
				e.printStackTrace();
			}
			return tweet;
		}		
	}
	
	/**
	 * This class serves as a utility to create Tweet Objects out
	 * of a ResultSet that is received from a database query.
	 */
	private class TweetKeywordRowMapper implements RowMapper<OutputTweet> {

		@Override
		public OutputTweet mapRow(ResultSet rs, int rowNum) throws SQLException {
			OutputTweet tweet = new OutputTweet();

			try {
				tweet.setTweetId(rs.getLong("tweetId"));
				tweet.setAuthorId(rs.getLong("authorId"));
				tweet.setText(rs.getString("text"));
				tweet.setCreatedAt(rs.getTimestamp("createdAt"));
				tweet.setPlace(rs.getString("place"));
				tweet.setImage(rs.getString("image"));
				tweet.setFavoriteCount(rs.getInt("favoriteCount"));
				tweet.setRetweetCount(rs.getInt("retweetCount"));				
				tweet.setFollowerCount(rs.getInt("followerCount"));
				tweet.setPriority(rs.getFloat("prio"));
				tweet.setName(rs.getString("name"));
				tweet.setScreenName(rs.getString("screenName"));
				tweet.setPictureUrl(rs.getString("pictureUrl"));
				
				List<String> keywords = new ArrayList<String>();
				keywords.add(rs.getString("keyword"));				
				tweet.setKeywords(keywords);
				
			} catch (TweetException e) {
				e.printStackTrace();
			}
			return tweet;
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