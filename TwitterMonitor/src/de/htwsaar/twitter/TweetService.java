package de.htwsaar.twitter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.TweetDao;
import twitter4j.Status;

@Service
public class TweetService {

	private TweetDao dao;

	@Autowired
	public TweetService(TweetDao dao) {
		this.dao = dao;
	}

	public List<Tweet> getTweets() {
		return dao.getTweets();
	}
	
	/**
	 * Uploads a single tweet into the database.
	 * 
	 * @param tweety
	 */
	public void uploadTweet(Tweet tweety) {
        dao.insertTweet(tweety);
    }
 
	/**
	 * Uploads a single author into the database.
	 * 
	 * @param authory
	 */
    public void uploadAutor(Author authory) {
        dao.insertAuthor(authory);
    }

	/**
	 * First inserts the author, then inserts a tweet in 
	 * the database. This is necessary b.o. author_id being a FK 
	 * in the tweet table.
	 * 
	 * @param status, the tweet information object 
	 */
	public void insertStatus(Status status) {

		// evaluates if the author already exists in the database
		if (dao.getAuthor(status.getUser().getId()) == null) {
			Author author = new Author(status);
			dao.insertAuthor(author);
		}

		Tweet latestTweet = new Tweet(status);
		//TODO: remove the sysout, exists only for debugging issues
		System.out.println(latestTweet.toString());
		dao.insertTweet(latestTweet);
	}
}
