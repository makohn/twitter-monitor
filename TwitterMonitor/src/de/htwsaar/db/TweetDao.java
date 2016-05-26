package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.htwsaar.twitter.Author;
import de.htwsaar.twitter.Tweet;

public class TweetDao {

	private JdbcTemplate jdbc;

	public TweetDao() {}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new JdbcTemplate(jdbc);
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

				tweet.setTweetId(rs.getInt("tweet_id"));
				tweet.setAuthorId(rs.getInt("autor_id"));
				tweet.setText(rs.getString("text"));
				tweet.setCreatedAt(rs.getDate("erstellt_am"));
				tweet.setPlace(rs.getString("standort"));
				tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
				tweet.setRetweetCount(rs.getInt("anzahl_retweets"));

				return tweet;
			}

		});
	}
	
	public List<String> getUrlsOfTweet(long tweetId) {
		
		String query = "select * from tweet_bilder where tweet_id = ?";
		
		Object[] args = new Object[1];
		args[0] = tweetId;
		
		return jdbc.query(query, args, new RowMapper<String>() {
			
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

		String insert = "insert into tweets (tweet_id, autor_id, text) values (?, ?, ?)";
		
		jdbc.update(insert, tweet.getTweetId(), tweet.getAuthorId(), tweet.getText());
		
		for (String url : tweet.getUrls())				// das könnte man mit einem Batch-update effektiver machen
			insertUrlOfTweet(tweet.getTweetId(), url);
	}
	
	/**
	 * This method inserts authors into the database.
	 * 
	 * @param author
	 */
	public void insertAuthor(Author author)	{
		
		String insert = "update or insert into tweet_autor (autor_id, name, screen_name, anzahl_follower, anzahl_tweets) values (?, ?, ?, ?, ?)";
		
		jdbc.update(insert, author.getId(), author.getName(), author.getScreen_name(), author.getFollowerCount(), author.getFavoriteCount());
	}
	
	/**
	 * This method inserts URLs into the database.
	 * 
	 * @param tweetId
	 * @param url
	 */
	public void insertUrlOfTweet(long tweetId, String url) {
		
		String insert = "insert into tweet_bilder (tweet_id, url) values (?, ?)";
		
		jdbc.update(insert, tweetId, url);
	}
}
