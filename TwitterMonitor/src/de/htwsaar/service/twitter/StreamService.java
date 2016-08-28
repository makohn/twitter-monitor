package de.htwsaar.service.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.htwsaar.db.KeywordDao;
import de.htwsaar.util.TweetLogger;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * The class StreamService is responsible for creating a stream to the Twitter
 * APi as well as a filter, which configures and initializes the stream which
 * defined parameters, such as keywords.
 *
 * @author Philipp Schaefer, Moritz Grill
 */

@Service
public class StreamService {

	private static final String CONSUMER_KEY = "4y9HVRAg43m3dfWoDKCWOzf9x";
	private static final String CONSUMER_SECRET = "GdZVRXMaGYn2b4PTXficnQVztCbE8eSlBJPT2zIIY5xn45zZRt";
	private static final String ACCESS_TOKEN = "712907200507850753-BNhxmqynkH6R7LyxG4GUsOf6pGP9i2L";
	private static final String ACCESS_TOKEN_SECRET = "xPLvu603NO1l1GJzZtmUNNokKqsdj1obVhrVHsHNAa0l8";

	private static final long FIFTEEN_MINUTES = 15 * 60 * 1000;

	private String[] keywordsArray;
	
	private TwitterStream stream;
	private TweetListener tweetListener;
	private KeywordDao keywordDao;

	@Autowired
	public StreamService(TweetListener tweetListener, KeywordDao keywordDao) {
		this.tweetListener = tweetListener;
		this.keywordDao = keywordDao;

		keywordsArray = keywordDao.getKeywords();
		
		startStream();
	}

	/**
	 * This method builds a stream to the twitter API by using the OAuth
	 * Credentials that are connected to this application.
	 */
	private void createStream() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(CONSUMER_KEY);
		cb.setOAuthConsumerSecret(CONSUMER_SECRET);
		cb.setOAuthAccessToken(ACCESS_TOKEN);
		cb.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

		stream = new TwitterStreamFactory(cb.build()).getInstance();

		stream.addListener(tweetListener);
	}

	/**
	 * This method initializes the stream with keywords and other filters, such
	 * as languages.
	 */
	private void initStream() {

		// String[] keywordsArray = { "Wasser", "Deutschland", "Hamburg",
		// "Berlin", "Paris", "America", "Trump",
		// "Clinton" };
		// // Erstelle Filter ..
		// FilterQuery filter = new FilterQuery();
		// filter.track(keywordsArray);
		// filter.language("de", "en");
		// // weitere Konfigurationen ...
		// stream.filter(filter);

		if ((keywordsArray != null) && (keywordsArray.length != 0)) {

			// Erstelle Filter ..
			FilterQuery filter = new FilterQuery();
			filter.track(keywordsArray);
			filter.language("de", "en");

			// weitere Konfigurationen ...

			stream.filter(filter);
		}

	}

	public void startStream() {
		createStream();
		initStream();
	}

	public void stopStream() {
		
//		System.out.println("Stopping Stream");	// DEBUG
//		TweetLogger.archiveLog();
		
		stream.removeListener(tweetListener);
		stream.shutdown();
	}

	// @Scheduled(cron = "0 1 * * * ?")
	@Scheduled(fixedDelay = /* FIFTEEN_MINUTES */2 * 60 * 1000)
	public void restartStream() {
		
		System.out.println("Restart Stream");	// DEBUG
		
		String[] newKeywordsArray = keywordDao.getKeywords();
		
//		System.out.println("newKeywords:");
//		for (String k : newKeywordsArray)
//			System.out.println(k);
//		System.out.println("oldKeywords:");
//		for (String k : keywordsArray)
//			System.out.println(k);
		
		if (keywordsChanged(keywordsArray, newKeywordsArray)) {
//			System.out.println("andere Keywords");
			keywordsArray = newKeywordsArray;
			stopStream();
			startStream();
		}
	}
	
	private boolean keywordsChanged(String[] oldKeywords, String[] newKeywords) {
		
		boolean changed = false;
		
		if (oldKeywords.length != newKeywords.length) {
//			System.out.println("same length");
			changed = true;
		} else {
			for (int i=0; i<newKeywords.length; i++) {
				if (!newKeywords[i].equals(oldKeywords[i])) {
					changed = true;
//					System.out.println("different keyword");
					break;
				}
			}
		}		
		
//		System.out.println("changed " + changed);
		
		return changed;
	}
}
