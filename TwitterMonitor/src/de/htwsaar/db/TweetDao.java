package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import de.htwsaar.exception.model.TweetException;
import de.htwsaar.model.OutputTweet;
import de.htwsaar.model.Tweet;

/**
 * The TweetDao Class encapsulates the database access for the tweet entity. It
 * provides methods for loading and storing Tweet Objects.
 * 
 * @author Philipp Schaefer, Marek Kohn
 *
 */

@Component("tweetDao")
public class TweetDao {

	private NamedParameterJdbcTemplate jdbc;

	public TweetDao() {
	}

	@Autowired
	public TweetDao(DataSource jdbc, KeywordDao keywordDao) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	/**
	 * This method loads all tweets of a certain user from the database.
	 * 
	 * @param username
	 * @return
	 */
	public List<OutputTweet> getTweets(String username, int limit) {
		
		String query = "select *, get_personal_prio(tweets.tweetId, :username) prio "
				+ "from tweets, tweetAuthors, tweets_x_keywords, keywords "
				+ "where tweets.authorId = tweetAuthors.authorId and tweets.tweetId = tweets_x_keywords.tweetId "
				+ "and tweets_x_keywords.keyword = keywords.keyword and keywords.username = :username "
				+ "and positive = 1	and active = 1 " + "and tweets.tweetId not in ("
				+ "select tweets.tweetId from tweets, tweets_x_keywords, keywords "
				+ "where tweets.tweetId = tweets_x_keywords.tweetId and tweets_x_keywords.keyword = keywords.keyword "
				+ "and keywords.username = :username and positive = 0 and active = 1) "
				+ "order by prio desc";
		
		if (limit > 0)
			query += " limit :limit";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("username", username);
		paramSource.addValue("limit", limit);

		// This query will get all Tweets for the user including one keyword per
		// row.
		// So if there are multiple Keywords associated with a Tweet, there will
		// be multiple identical rows
		// only with different keywords.
		List<OutputTweet> tweetList = null;
		try {
			tweetList = jdbc.query(query, paramSource, new TweetKeywordRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			tweetList = new ArrayList<OutputTweet>();
		}

		// Here the tweetList of the query is worked through. If a tweet shows
		// up
		// a second, third ... time, the keyword will be added to the first
		// tweets-list.
		// The first appearances are stored in a HashMap.
		HashMap<Long, OutputTweet> tweetMap = new HashMap<Long, OutputTweet>();
		for (OutputTweet tweet : tweetList) {
			if (tweetMap.containsKey(tweet.getTweetId()))
				tweetMap.get(tweet.getTweetId()).addKeyword(tweet.getKeywords().get(0));
			else
				tweetMap.put(tweet.getTweetId(), tweet);
		}

		return new ArrayList<OutputTweet>(tweetMap.values());
	}
	
//	public int getTweetCount(String username) {
//		
//		String query = "select count(*) "
//				+ "from tweets, tweetAuthors, tweets_x_keywords, keywords "
//				+ "where tweets.authorId = tweetAuthors.authorId and tweets.tweetId = tweets_x_keywords.tweetId "
//				+ "and tweets_x_keywords.keyword = keywords.keyword and keywords.username = :username "
//				+ "and positive = 1	and active = 1 " + "and tweets.tweetId not in ("
//				+ "select tweets.tweetId from tweets, tweets_x_keywords, keywords "
//				+ "where tweets.tweetId = tweets_x_keywords.tweetId and tweets_x_keywords.keyword = keywords.keyword "
//				+ "and keywords.username = :username and positive = 0 and active = 1)";
//
//		MapSqlParameterSource paramSource = new MapSqlParameterSource();
//		paramSource.addValue("username", username);
//		
//		int count = 0;
//		try {
//			count = (int) jdbc.queryForObject(query, paramSource, new RowMapper<Integer>() {
//				@Override
//				public Integer mapRow(ResultSet rs,int rowNum) throws SQLException {
//					return rs.getInt(0);
//				}				
//			});
//		} catch (DataAccessException e) {
//			e.printStackTrace();
//		}
//		
//		return count;
//	}

	/**
	 * This method inserts a list of multiple tweets at once into the database.
	 * Duplicates are handled as in the insertTweet method.
	 * 
	 * @param tweets
	 *            - a list of tweets that should be stored.
	 */
	public void insertTweets(List<Tweet> tweets) {

		String insert = "insert into tweets (tweetId, authorId, text, favoriteCount, retweetCount, place, image, createdAt) "
				+ "values (:tweetId, :authorId, :text, :favoriteCount, :retweetCount, :place, :image, :createdAt) "
				+ "on duplicate key update favoriteCount=:favoriteCount, retweetCount=:retweetCount;";

		SqlParameterSource[] paramSources = new MapSqlParameterSource[tweets.size()];
		for (int i = 0; i < tweets.size(); i++) {

			MapSqlParameterSource paramSource = getTweetParamSource(tweets.get(i));
			paramSources[i] = paramSource;
		}

		boolean updateComplete = true;
		do {			
			try {
				jdbc.batchUpdate(insert, paramSources);
			} catch (DeadlockLoserDataAccessException e) {
				updateComplete = false;
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		} while (!updateComplete);
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
	 * This class serves as a utility to create Tweet Objects out of a ResultSet
	 * that is received from a database query.
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

				// List<Keyword> keywords = new ArrayList<Keyword>();
				// Keyword keyword = new Keyword();
				// keyword.setKeyword(rs.getString("keyword"));
				// keyword.setUsername(rs.getString("username"));
				// keyword.setPriority(rs.getInt("priority"));
				// keyword.setPositive(rs.getBoolean("positive"));
				// keyword.setActive(rs.getBoolean("active"));
				// keywords.add(keyword);
				// tweet.setKeywords(keywords);

			} catch (TweetException e) {
				e.printStackTrace();
			}
			return tweet;
		}
	}
}