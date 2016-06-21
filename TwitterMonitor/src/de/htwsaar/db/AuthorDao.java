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

import de.htwsaar.exception.model.AuthorException;
import de.htwsaar.model.Author;

@Component("authorDao")
public class AuthorDao {

	private NamedParameterJdbcTemplate jdbc;

	public AuthorDao() {
	}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
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

				try {
					author.setId(rs.getLong("autor_id"));
					author.setName(rs.getString("name"));
					author.setName(rs.getString("screen_name"));
					author.setFollowerCount(rs.getInt("anzahl_follower"));
					author.setFavoriteCount(rs.getInt("anzahl_tweets"));
					author.setPictureUrl(rs.getString("profilbild_url"));
				} catch (AuthorException e) {
					e.printStackTrace();
				}
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

		// return jdbc.query(query, paramMap, new ResultSetExtractor<Author>() {
		//
		// public Author extractData(ResultSet rs) throws SQLException {
		//
		// Author author = new Author();
		//
		// author.setId(rs.getLong("autor_id"));
		// author.setName(rs.getString("name"));
		// author.setScreen_name(rs.getString("screen_name"));
		// author.setFollowerCount(rs.getInt("anzahl_follower"));
		// author.setFavoriteCount(rs.getInt("anzahl_tweets"));
		// author.setPictureUrl(rs.getString("profilbild_url"));
		//
		// return author;
		// }
		// });

		List<Author> list = jdbc.query(query, paramMap, new RowMapper<Author>() {

			public Author mapRow(ResultSet rs, int rowNum) throws SQLException {

				Author author = new Author();

				try {
					author.setId(rs.getLong("autor_id"));
					author.setName(rs.getString("name"));
					author.setName(rs.getString("screen_name"));
					author.setFollowerCount(rs.getInt("anzahl_follower"));
					author.setFavoriteCount(rs.getInt("anzahl_tweets"));
					author.setPictureUrl(rs.getString("profilbild_url"));
				} catch (AuthorException e) {
					e.printStackTrace();
				}
				return author;
			}
		});

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	public boolean checkForAuthor(long authorId) {
		if (getAuthor(authorId) == null) return false;
		else return true;
	}

	/**
	 * This method inserts authors into the database.
	 * 
	 * @param author
	 */
	public void insertAuthor(Author author) {

		String insert = "insert into tweet_autor (autor_id, name, screen_name, anzahl_follower, anzahl_tweets, profilbild_url) values (:autorId, :name, :screenName, :followerCount, :favoriteCount, :pictureUrl)"
					  + " on duplicate key update name = :name, screen_name = :screenName, anzahl_follower = :followerCount, anzahl_tweets = :favoriteCount, profilbild_url = :pictureUrl";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("autorId", author.getId());
		paramMap.put("name", author.getName());
		paramMap.put("screenName", author.getScreenName());
		paramMap.put("followerCount", author.getFollowerCount());
		paramMap.put("favoriteCount", author.getFavoriteCount());
		paramMap.put("pictureUrl", author.getPictureUrl());

		jdbc.update(insert, paramMap);
	}

	public void deleteAuthor(long authorId) {
		String delete = "delete from tweet_autor where autor_id = :authorId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("authorId", authorId);

		jdbc.update(delete, paramMap);
	}

	public void updateAuthor(Author author) {

		deleteAuthor(author.getId());
		insertAuthor(author);
	}
}
