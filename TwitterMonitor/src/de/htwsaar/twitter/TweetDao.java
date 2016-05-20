package de.htwsaar.twitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class TweetDao {
	
	private NamedParameterJdbcTemplate jdbc;
			
	public TweetDao() {}
	
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}
	
	public List<Tweet> getTweets() {
		
		return jdbc.query("select * from tweets", new RowMapper<Tweet>() {

			public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Tweet tweet = new Tweet();
				
				tweet.setTweetId(rs.getInt("tweet_id"));
				tweet.setAuthorId(rs.getInt("author_id"));
				tweet.setText(rs.getString("text"));
				tweet.setCreatedAt(rs.getDate("erstellt_am"));
				tweet.setPlace(rs.getString("standort"));
				tweet.setFavoriteCount(rs.getInt("anzahl_likes"));
				tweet.setRetweetCount(rs.getInt("anzahl_retweets"));
				
				return tweet;				
			}
			
		});
		
	}
	
//	public boolean update(Tweet tweet) {
//		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(tweet);
//		
//		return jdbc.update("update tweets set name=:name, text=:text, email=:email where id=:id", params) == 1;
//	}
//	
	public boolean insert(Tweet tweet) {
		
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(tweet);
		
//		return jdbc.update("insert into tweets (idtweets, text, createdAt, place, favoriteCount, retweetCount) values (:id, :text, :createdAt, :place, :favoriteCount, :retweetCount)", params) == 1;
//		return jdbc.update("insert into test (text) values (:text)", params) == 1; // so klappts einwandfrei
		return jdbc.update("insert into tweets (tweet_id, autor_id, text, anzahl_likes, anzahl_retweets, standort, erstellt_am) values (:tweetId, :authorID, :text, :favoriteCount, :retweetCount, :place, :createdAt)", params) == 1;
	}
//	
//	@Transactional
//	public int[] create(List<Offer> offers) {
//		
//		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(offers.toArray());
//		
//		return jdbc.batchUpdate("insert into offers (id, name, text, email) values (:id, :name, :text, :email)", params);
//	}
//	
//	public boolean delete(int id) {
//		MapSqlParameterSource params = new MapSqlParameterSource("id", id);
//		
//		return jdbc.update("delete from offers where id=:id", params) == 1;
//	}
//
//	public Offer getOffer(int id) {
//
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("id", id);
//
//		return jdbc.queryForObject("select * from offers where id=:id", params,
//				new RowMapper<Offer>() {
//
//					public Offer mapRow(ResultSet rs, int rowNum)
//							throws SQLException {
//						Offer offer = new Offer();
//
//						offer.setId(rs.getInt("id"));
//						offer.setName(rs.getString("name"));
//						offer.setText(rs.getString("text"));
//						offer.setEmail(rs.getString("email"));
//
//						return offer;
//					}
//
//				});
//	}

}
