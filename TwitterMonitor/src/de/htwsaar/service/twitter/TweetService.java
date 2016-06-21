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
import de.htwsaar.model.IncomingTweet;
import de.htwsaar.model.Tweet;
import twitter4j.Status;

/**
 * The TweetService Class is a facade for I/O activities of Tweet Objects.
 * It can be seen as the superior layer to the DAO layer, providing methods
 * for storing and loading Tweet Objects.
 * 
 * Incoming tweets are buffered before they get passed to the DAO layer. 
 * 
 * @author Philipp Schaefer 
 */
@Service
public class TweetService {

	private static final long MAXIMUM_AGE = 48 * 60 * 60 * 1000; // 2 days in milliseconds
	private static final long MAXIMUM_RECEIVED_AGE = 2 * 60 * 60 * 1000; // 2 Hours in milliseconds
	
	private static final int RETWEET_REFRESH_LIMIT = 25;
	private static final int MAXIMUM_BUFFER_SIZE = 100;
	private static final long FIFTEEN_MINUTES = 15 * 60 * 1000;
	

	private volatile HashMap<IncomingTweet, Integer> retweetMap;
//	private boolean isLocked;

	private TweetDao tweetDao;
	private AuthorDao authorDao;


	@Autowired
	public TweetService(TweetDao tweetDao, AuthorDao authorDao) {
		this.tweetDao = tweetDao;
		this.authorDao = authorDao;

		retweetMap = new HashMap<IncomingTweet, Integer>();
//		isLocked = false;
	}

	public List<Tweet> getTweets() {
		return tweetDao.getTweets();
	}
	
	public Tweet getTweet(long id) {
		return tweetDao.getTweet(id);
	}
	
	public void insertTweet(Tweet tweet) {
		tweetDao.insertTweet(tweet);
	}

	public void insertAuthor(Author author) {
		authorDao.insertAuthor(author);
	}

	/**
	 * This method converts a stream-received Tweet Status into a Tweet Object
	 * Depending on the tweet being an original or a Retweet, the Tweet Object is
	 * either added to the buffer or updated if already existing.
	 * @throws TwitterMonitorException
	 * @param status - the tweet information object
	 */
	public /*synchronized*/ void insertStatus(Status status) {
		//TODO: "synchronized" hat mir Olbertz empfohlen, weil ja evtl. mehrere Tweets die Methode gleichzeitig aufrufen
		// so m¸sste sie theoretisch vom Multithreading her gesperrt sein, solange sie l‰uft.
		// Ich hatte mir dafuer nur dieses Boolean-Schloss ¸berlegt. (Olbertz war sich nicht so ganz sicher)
				
//		if ( !isLocked && (retweetMap.size() > MAXIMUM_BUFFER_SIZE) ) {
//			isLocked = true;
//			clearMap();
//			isLocked = false;
//		}
		
		try {
			// If the status is a retweet then load the original status and
			// forget the retweet.
			if (status.isRetweet()) {
				status = status.getRetweetedStatus();
			}
			
			// Create Author and Tweet from status
			Author author = new Author(status);
			IncomingTweet tweet = new IncomingTweet(status);

			// Check if the status is older than the maximum age.
			if (tweet.getAge() > MAXIMUM_AGE) {
				// If so then remove the entry in the buffer
				retweetMap.remove(tweet);
				// and leave the method.
				return;
			}

			// Check if the map already has an entry for the tweet.
			if (retweetMap.containsKey(tweet)) {
				// If so then increase the retweet-counter of the tweet.
				int retweetCount = retweetMap.get(tweet) + 1;
				retweetMap.put(tweet, retweetCount);
				// If the retweet-counter doesn't surpass the refresh limit
				if (retweetMap.get(tweet) <= RETWEET_REFRESH_LIMIT) {
					// then leave the method.
					return;
				}
				// Otherwise remove the entry in the hash-map and don't leave.
				// The tweet will be updated in the database.
				else {
					retweetMap.remove(tweet);					
				}
				// If there is no entry
			} else {
				// create a new entry and set its retweet-counter to zero.
				retweetMap.put(tweet, 0);
				// The tweet will be inserted or updated into/in the database.
			}

			// Insert or Update Author and Tweet.
			authorDao.insertAuthor(author);
			tweetDao.insertTweet(tweet);

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
	@Scheduled(fixedDelay = FIFTEEN_MINUTES)
	private void clearMap() {
				
		HashMap<IncomingTweet, Integer> newMap = new HashMap<IncomingTweet, Integer>();		
		ArrayList<Tweet> toBeUpdated = new ArrayList<Tweet>();
						
		// Check all entry and remove, keep or update them accordingly.
		for (IncomingTweet t : retweetMap.keySet()) {
			
			// If the original tweet is too old anyway,
			if ( t.getAge() > MAXIMUM_AGE ) {
				// do nothing. (so the tweet is removed from the map)
			}
			// If the tweet hasn't been retweeted since the last cleanup,
			else if ( retweetMap.get(t) == 0 )  {
				// do nothing either.
			}
			// If the tweet hasn't been retweeted within a certain time,
			else if ( t.getReceivedElapsed() > MAXIMUM_RECEIVED_AGE ) {
				// update Database.
				toBeUpdated.add(t);
			}			
			// If the tweet was retweeted more than a certain amount compared to the maximum limit.
			if ( retweetMap.get(t) > RETWEET_REFRESH_LIMIT / 2 ) {
				// update the tweets to the database. (The tweet is removed from the map afterwards)
				toBeUpdated.add(t);
			}
			// If the tweet is not too old, has recently been retweeted, was retweeted more than once, but not more than a certain amount,
			else {
				// keep them in the buffer.
				newMap.put(t, retweetMap.get(t));
			}		
		}
						
		retweetMap = new HashMap<IncomingTweet, Integer>();
		
		// Additional:
		// If the map is still too large,
		if ( newMap.size() > MAXIMUM_BUFFER_SIZE / 2 ) {
			// remove tweets in a arbitrary pattern.
			int tweetsToRemove = newMap.size() - MAXIMUM_BUFFER_SIZE/2;
			int i=0;
			for (IncomingTweet t : newMap.keySet()) {
				i++;
				if ( (i % (newMap.size()/tweetsToRemove) != 0 ) )
						retweetMap.put(t, newMap.get(t));
				else
					toBeUpdated.add(t);
			}
		}
		
		tweetDao.insertTweets(toBeUpdated);
		
		// Anmerkung: Alternativ kˆnnte man in dieser Methode auch einfach alle tweets die kein 0-retweet-count haben updaten
		// Habe erst nachdem ich das alles fertig hatte mich nochmal an das Batch-Update gesetzt...
		System.out.println("Grˆﬂe des Buffers: " + retweetMap.size());
	}
	
}