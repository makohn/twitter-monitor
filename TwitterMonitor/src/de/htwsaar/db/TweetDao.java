package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import de.htwsaar.exceptions.model.TweetException;
import de.htwsaar.model.Author;
import de.htwsaar.model.Tweet;

@Component("tweetDao")
public class TweetDao {

	private NamedParameterJdbcTemplate jdbc;

	public TweetDao() {
	}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	/**
	 * This method loads tweets from the database.
	 * 
	 * @usedIn TweetController
	 * @return
	 */
	public List<Tweet> getTweets() {

		String query = "select * from tweets";

		return jdbc.query(query, new RowMapper<Tweet>() {

			public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {

				Tweet tweet = new Tweet();

				try {
					tweet.setTweetId(rs.getLong("tweet_id"));
					tweet.setAuthorId(rs.getLong("autor_id"));
					tweet.setText(rs.getString("text"));
					tweet.setCreatedAt(rs.getDate("tweet_datum"));
					tweet.setPlace(rs.getString("standort"));
					tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
					tweet.setRetweetCount(rs.getInt("anzahl_retweets"));
					tweet.setUrls(getUrlsOfTweet(rs.getLong("tweet_id")));
				} catch (TweetException e) {
					e.printStackTrace();
				}
				return tweet;
			}

		});
	}

	public Tweet getTweet(long tweetId) {

		String query = "select * from tweets where tweet_id = :tweetId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tweetId", tweetId);

		// return jdbc.query(query, paramMap, new ResultSetExtractor<Tweet>() {
		//
		// public Tweet extractData(ResultSet rs) throws SQLException {
		//
		// Tweet tweet = new Tweet();
		//
		// tweet.setTweetId(rs.getLong("tweet_id"));
		// tweet.setAuthorId(rs.getLong("autor_id"));
		// tweet.setText(rs.getString("text"));
		// tweet.setCreatedAt(rs.getDate("tweet_datum"));
		// tweet.setPlace(rs.getString("standort"));
		// tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
		// tweet.setRetweetCount(rs.getInt("anzahl_retweets"));
		//
		// tweet.setUrls(getUrlsOfTweet(rs.getLong("tweet_id")));
		//
		// return tweet;
		// }
		// });

		List<Tweet> list = jdbc.query(query, paramMap, new RowMapper<Tweet>() {

			public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {

				Tweet tweet = new Tweet();

				try {
					tweet.setTweetId(rs.getLong("tweet_id"));
					tweet.setAuthorId(rs.getLong("autor_id"));
					tweet.setText(rs.getString("text"));
					tweet.setCreatedAt(rs.getDate("tweet_datum"));
					tweet.setPlace(rs.getString("standort"));
					tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
					tweet.setRetweetCount(rs.getInt("anzahl_retweets"));
					tweet.setUrls(getUrlsOfTweet(rs.getLong("tweet_id")));
				} catch (TweetException e) {
					e.printStackTrace();
				}
				return tweet;
			}
		});

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	public boolean checkForTweet(long tweetId) {
		if (getTweet(tweetId) == null) return false;
		else return true;		
	}

	/**
	 * Returns a list of all URLs that are mentioned in a singular tweet
	 * 
	 * @param tweetId,
	 *            the ID of the tweet
	 * @return
	 */
	public List<String> getUrlsOfTweet(long tweetId) {

		String query = "select * from tweet_bilder where tweet_id = :tweetId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tweetId", tweetId);

		return jdbc.query(query, paramMap, new RowMapper<String>() {

			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("url");
			}
		});
	}

	/**
	 * This method inserts tweets into the database.
	 * 
	 * @usedIn TweetListener
	 * @return
	 */
	public void insertTweet(Tweet tweet) {

		String insert = "insert into tweets (tweet_id, autor_id, text, anzahl_likes, anzahl_retweets, standort, tweet_datum) values (:tweetId, :autorId, :text, :favoriteCount, :retweetCount, :place, :createdAt)"
							+ " on duplicate key update anzahl_likes=:favoriteCount, anzahl_retweets=:retweetCount;";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tweetId", tweet.getTweetId());
		paramMap.put("autorId", tweet.getAuthorId());
		paramMap.put("text", tweet.getText());
		paramMap.put("favoriteCount", tweet.getFavoriteCount());
		paramMap.put("retweetCount", tweet.getRetweetCount());
		paramMap.put("place", tweet.getPlace());
		paramMap.put("createdAt", tweet.getCreatedAt());

		jdbc.update(insert, paramMap);

		insertUrlsOfTweet(tweet.getTweetId(), tweet.getUrls());
		
		// TODO: tweets mit keywords verbinden, das macht aber glaube ich die DB von selbst
	}
	
	public void insertTweets(List<Tweet> tweets) {
		
		String insert = "insert into tweets (tweet_id, autor_id, text, anzahl_likes, anzahl_retweets, standort, tweet_datum) values (:tweetId, :autorId, :text, :favoriteCount, :retweetCount, :place, :createdAt)"
				+ " on duplicate key update anzahl_likes=:favoriteCount, anzahl_retweets=:retweetCount;";
		
		SqlParameterSource[] paramSources = new MapSqlParameterSource[tweets.size()];		
		HashMap<Long, String> urls = new HashMap<Long, String>();
		
		for (int i=0; i<tweets.size(); i++) {
			
			Tweet tweet = tweets.get(i);
			
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			
			paramSource.addValue("tweetId", tweet.getTweetId());
			paramSource.addValue("autorId", tweet.getAuthorId());
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
	 * This method inserts URLs into the database.
	 * 
	 * @param tweetId
	 * @param url
	 */
	public void insertUrlOfTweet(long tweetId, String url) {

		String insert = "insert into tweet_bilder (tweet_id, url) values (:tweetId, :url)";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tweetId", tweetId);
		paramMap.put("url", url);

		jdbc.update(insert, paramMap);
	}
	
	public void insertUrlsOfTweet(long tweetId, List<String> urls) {
		
		String insert = "insert into tweet_bilder (tweet_id, url) values (:tweetId, :url)";
		
		SqlParameterSource[] paramSources = new MapSqlParameterSource[urls.size()];
						
		for (int i=0; i<urls.size(); i++) {
			
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			
			paramSource.addValue("tweetId", tweetId);
			paramSource.addValue("url", urls.get(i));
			
			paramSources[i] = paramSource;
		}
		
		jdbc.batchUpdate(insert, paramSources);
		
	}
	
	public void insertUrlsOfTweet(HashMap<Long, String> urls) {
		
		String insert = "insert into tweet_bilder (tweet_id, url) values (:tweetId, :url)";
		
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
	

	public void deleteTweet(long tweetId) {
		String delete = "delete from tweets where tweet_id = :tweetId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tweetId", tweetId);

		deleteUrlsOfTweet(tweetId);
		jdbc.update(delete, paramMap);
	}

	private void deleteUrlsOfTweet(long tweetId) {
		String delete = "delete from tweet_bilder where tweet_id = :tweetId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tweetId", tweetId);

		jdbc.update(delete, paramMap);
	}

	public void updateTweet(Tweet tweet) {

		deleteTweet(tweet.getTweetId());
		insertTweet(tweet);
	}

	public List<Tweet> getTweetsWithKeyword(String keyword) {

		String query = "select * from tweets_x_keywords where keyword = :keyword";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("keyword", keyword);

		return jdbc.query(query, paramMap, new RowMapper<Tweet>() {

			@Override
			public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {

				Tweet tweet = new Tweet();

				try {
					tweet.setTweetId(rs.getLong("tweet_id"));
					tweet.setAuthorId(rs.getLong("autor_id"));
					tweet.setText(rs.getString("text"));
					tweet.setCreatedAt(rs.getDate("tweet_datum"));
					tweet.setPlace(rs.getString("standort"));
					tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
					tweet.setRetweetCount(rs.getInt("anzahl_retweets"));
					tweet.setUrls(getUrlsOfTweet(rs.getLong("tweet_id")));
				} catch (TweetException e) {
					e.printStackTrace();
				}
				return tweet;
			}

		});

	}
}
