package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import de.htwsaar.twitter.Author;
import de.htwsaar.twitter.Tweet;

@Component("tweetDao")
public class TweetDao {

	private NamedParameterJdbcTemplate jdbc;

	public TweetDao() {}

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

				tweet.setTweetId(rs.getLong("tweet_id"));
				tweet.setAuthorId(rs.getLong("autor_id"));
				tweet.setText(rs.getString("text"));
				tweet.setCreatedAt(rs.getDate("erstellt_am"));
				tweet.setPlace(rs.getString("standort"));
				tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
				tweet.setRetweetCount(rs.getInt("anzahl_retweets"));
				
				tweet.setUrls(getUrlsOfTweet(rs.getLong("tweet_id")));

				return tweet;
			}

		});
	}
	
	/**
	 * Returns a list of all URLs that are mentioned in a singular tweet
	 * 
	 * @param tweetId, the ID of the tweet 
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
	 * Returns a List of all the authors stored in the database table
	 * 
	 * @return
	 */
	public List<Author> getAuthors() {
		 
        String query = "select * from tweet_autor";
         
        return jdbc.query(query, new RowMapper<Author>() {
 
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
 
                Author author = new Author();
 
                author.setId(rs.getLong("autor_id"));
                author.setName(rs.getString("name"));
                author.setScreen_name(rs.getString("screen_name"));
                 
                return author;
            }
 
        });
    }
	
	
	/**
	 * Returns an Author object if the authorID is found in the database.
	 * 
	 * @usedIn TweetService, when checking if an author already exists
	 * @param authorId
	 * @return
	 */
	public Author getAuthor(long authorId) {

		String query = "select * from tweet_autor where autor_id = :authorId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("authorId", authorId);

		List<Author> authors = jdbc.query(query, paramMap, new RowMapper<Author>() {

			public Author mapRow(ResultSet rs, int rowNum) throws SQLException {

				Author author = new Author();

				author.setId(rs.getLong("autor_id"));
				author.setName("name");
				author.setScreen_name("screen_name");

				return author;
			}

		});

		if (!authors.isEmpty())
			return authors.get(0);
		else
			return null;
	}

	/**
	 * This method inserts tweets into the database.
	 * 
	 * @usedIn TweetListener
	 * @return
	 */
	public void insertTweet(Tweet tweet) {

		// eigentlichen Tweet hinzufügen 
        
        String insert = "insert into tweets (tweet_id, autor_id, text) values (:tweetId, :autorId, :text)";
         
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tweetId", tweet.getTweetId());
        paramMap.put("autorId", tweet.getAuthorId());
        paramMap.put("text", tweet.getText());
         
        jdbc.update(insert, paramMap);
         
        //das koennte man mit einem Batch-update effektiver machen
        for (String url : tweet.getUrls())              
            insertUrlOfTweet(tweet.getTweetId(), url);
        
        //TODO: tweets mit keywords verbinden
	}
	
	/**
	 * This method inserts authors into the database.
	 * 
	 * @param author
	 */
	public void insertAuthor(Author author)	{
		
		String insert = "insert into tweet_autor (autor_id, name, screen_name) values (:autorId, :name, :screenName)";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("autorId", author.getId());
        paramMap.put("name", author.getName());
        paramMap.put("screenName", author.getScreen_name());
         
        jdbc.update(insert, paramMap);
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
}
