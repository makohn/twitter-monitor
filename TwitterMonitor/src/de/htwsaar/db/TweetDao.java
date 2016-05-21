package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import de.htwsaar.twitter.Tweet;

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

		return jdbc.query("select * from tweets", new RowMapper<Tweet>() {

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

	/**
	 * This method inserts tweets into the database.
	 * 
	 * @usedIn TweetListener
	 * @return
	 */
	public boolean insert(Tweet tweet) {

		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(tweet);

		return jdbc.update("insert into tweets (tweet_id, autor_id, text) values (:tweetId, :authorId, :text)",
				params) == 1;
	}
}
