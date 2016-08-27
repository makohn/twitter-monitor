package de.htwsaar.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import de.htwsaar.exception.model.AuthorException;
import de.htwsaar.model.Author;

/**
 * The AuthorDao Class encapsulates the database access for the tweet author
 * entity. It provides methods for loading and storing Author Objects.
 * 
 * @author Philipp Schaefer
 * 
 */

@Component("authorDao")
public class AuthorDao {

	private NamedParameterJdbcTemplate jdbc;

	public AuthorDao() {}

	@Autowired
	public AuthorDao(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}
	
	/**
	 * This method returns a list of all tweet authors that are stored in the
	 * database.
	 * 
	 * @return a list of Author Objects
	 */
	public List<Author> getAuthors() {
		
		String query = "select * from tweetAuthors";

		List<Author> authors = null;
		try {
			authors = jdbc.query(query, new AuthorRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			authors = new ArrayList<Author>();
		}
		return authors;
	}

	/**
	 * This method loads a single author from the database.
	 * 
	 * @param authorId
	 *            - the unique id of a author (twitter user)
	 * @return a Author Object, if the author exists. Otherwise returns null.
	 */
	public Author getAuthor(long authorId) {

		String query = "select * from tweetAuthors where authorId = :authorId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("authorId", authorId);

		Author author = null;
		try {
			author = (Author) jdbc.queryForObject(query, paramSource, new AuthorRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return author;
	}

	/**
	 * This method inserts Author Objects into the database.
	 * 
	 * @param author
	 *            - the Author Object that should be stored
	 */
	public void insertAuthor(Author author) {

		String insert = "insert into tweetAuthors (authorId, name, screenName, followerCount, pictureUrl) values (:authorId, :name, :screenName, :followerCount, :pictureUrl)"
				+ " on duplicate key update name = :name, screenName = :screenName, followerCount = :followerCount, pictureUrl = :pictureUrl";

		MapSqlParameterSource paramSource = getAuthorParameterSource(author);
		try {
			jdbc.update(insert, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public void insertAuthors(List<Author> authors) {
		String insert = "insert into tweetAuthors (authorId, name, screenName, followerCount, pictureUrl) "
				+ "values (:authorId, :name, :screenName, :followerCount, :pictureUrl) "
				+ "on duplicate key update name=:name, screenName = :screenName, followerCount = :followerCount, pictureUrl = :pictureUrl";

		SqlParameterSource[] paramSources = new MapSqlParameterSource[authors.size()];
		for (int i = 0; i < authors.size(); i++) {

			MapSqlParameterSource paramSource = getAuthorParameterSource(authors.get(i));
			paramSources[i] = paramSource;
		}
		try {
			jdbc.batchUpdate(insert, paramSources);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method removes Author Objects from the database.
	 * 
	 * @param authorId
	 *            - the unique id of the author.
	 */
	public void deleteAuthor(long authorId) {
		String delete = "delete from tweetAuthors where authorId = :authorId";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("authorId", authorId);

		try {
			jdbc.update(delete, paramSource);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	private MapSqlParameterSource getAuthorParameterSource(Author author) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("authorId", author.getAuthorId());
		paramSource.addValue("name", author.getName());
		paramSource.addValue("screenName", author.getScreenName());
		paramSource.addValue("followerCount", author.getFollowerCount());
		paramSource.addValue("pictureUrl", author.getPictureUrl());

		return paramSource;
	}

	/**
	 * This class serves as a utility to create Author Objects out of a
	 * ResultSet that is received from a database query.
	 */
	private class AuthorRowMapper implements RowMapper<Author> {

		@Override
		public Author mapRow(ResultSet rs, int rowNum) throws SQLException {

			Author author = new Author();

			try {
				author.setAuthorId(rs.getLong("authorId"));
				author.setName(rs.getString("name"));
				author.setName(rs.getString("screenName"));
				author.setFollowerCount(rs.getInt("followerCount"));
				author.setPictureUrl(rs.getString("pictureUrl"));
			} catch (AuthorException e) {
				e.printStackTrace();
			}
			return author;
		}
	}
}