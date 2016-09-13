package de.htwsaar.db;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

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

	public void insertAuthors(List<Author> authors) {
		String insert = "insert into tweetAuthors (authorId, name, screenName, followerCount, pictureUrl) "
				+ "values (:authorId, :name, :screenName, :followerCount, :pictureUrl) "
				+ "on duplicate key update name=:name, screenName = :screenName, followerCount = :followerCount, pictureUrl = :pictureUrl";

		SqlParameterSource[] paramSources = new MapSqlParameterSource[authors.size()];
		for (int i = 0; i < authors.size(); i++) {
			MapSqlParameterSource paramSource = getAuthorParameterSource(authors.get(i));
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

	private MapSqlParameterSource getAuthorParameterSource(Author author) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("authorId", author.getAuthorId());
		paramSource.addValue("name", author.getName());
		paramSource.addValue("screenName", author.getScreenName());
		paramSource.addValue("followerCount", author.getFollowerCount());
		paramSource.addValue("pictureUrl", author.getPictureUrl());

		return paramSource;
	}
}