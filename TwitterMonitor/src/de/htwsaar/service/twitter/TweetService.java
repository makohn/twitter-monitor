package de.htwsaar.service.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.htwsaar.db.AuthorDao;
import de.htwsaar.db.TweetDao;
import de.htwsaar.exception.TwitterMonitorException;
import de.htwsaar.exception.model.AuthorException;
import de.htwsaar.exception.model.TweetException;
import de.htwsaar.model.Author;
import de.htwsaar.model.OutputTweet;
import de.htwsaar.model.Tweet;
import twitter4j.Status;

/**
 * The TweetService Class is a facade for I/O activities of Tweet Objects. It
 * can be seen as the superior layer to the DAO layer, providing methods for
 * storing and loading Tweet Objects.
 * 
 * Incoming tweets are buffered before they get passed to the DAO layer.
 * 
 * @author Philipp Schaefer
 */
@Service
public class TweetService {

	private static final long MAXIMUM_AGE = 48 * 60 * 60 * 1000;
	private static final long THIRTY_SECONDS = 30 * 1000;

	private HashMap<Long, Tweet> tweetBuffer;
	private HashMap<Long, Author> authorBuffer;
	
	private TweetDao tweetDao;
	private AuthorDao authorDao;

	@Autowired
	public TweetService(TweetDao tweetDao, AuthorDao authorDao) {
		this.tweetDao = tweetDao;
		this.authorDao = authorDao;
		
		tweetBuffer = new HashMap<Long, Tweet>();
		authorBuffer = new HashMap<Long, Author>();
	}

	public List<OutputTweet> getTweets(String username) {		
		return tweetDao.getTweets(username);
	}
	
//	public int getTweetCount(String username) {
//		return tweetDao.getTweetCount(username);
//	}

	/**
	 * This method converts a stream-received Tweet Status into a Tweet Object
	 * Depending on the tweet being an original or a Retweet, the Tweet Object
	 * is either added to the buffer or updated if already existing.
	 * 
	 * @throws TwitterMonitorException
	 * @param status - the tweet information object
	 */
	public synchronized void insertStatus(Status status) {

		try {
			// If the status is a retweet then load the original status and
			// forget the retweet.
			if (status.isRetweet()) {
				status = status.getRetweetedStatus();
			}

			// Create Author and Tweet from status
			Author author = new Author(status);
			Tweet tweet = new Tweet(status);

			// Check if the status is older than the maximum age.
			if (tweet.getAge() > MAXIMUM_AGE) {
				// If so then
				// leave the method.
				return;
			}

			// Insert or Update Author and Tweet into the buffers.
			authorBuffer.put(author.getAuthorId(), author);
			tweetBuffer.put(tweet.getTweetId(), tweet);

		} catch (AuthorException e) {
			e.printStackTrace();
		} catch (TweetException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method clears and renews the buffer at a given interval.
	 */
	@Scheduled(fixedDelay = THIRTY_SECONDS)
	private synchronized void uploadTweetBuffers() {

		authorDao.insertAuthors(new ArrayList<Author>(authorBuffer.values()));
		authorBuffer.clear();
		tweetDao.insertTweets(new ArrayList<Tweet>(tweetBuffer.values()));
		tweetBuffer.clear();
	}
}