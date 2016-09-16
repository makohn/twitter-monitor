package de.htwsaar.service.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.htwsaar.db.KeywordDao;
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

	private static final long ONE_MINUTE = 1 * 60 * 1000;
	private static final long ONE_DAY = 1 * 24 * 60 * 60 * 1000;

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

		if ((keywordsArray != null) && (keywordsArray.length != 0)) {

			// Erstelle Filter ..
			FilterQuery filter = new FilterQuery();
			filter.track(keywordsArray);
			filter.language("de", "en");

			// weitere Konfigurationen ...

//			stream.filter(filter);
		}
	}

	public void startStream() {
		createStream();
		initStream();
	}

	public void stopStream() {
		stream.removeListener(tweetListener);
		stream.shutdown();
	}

	@Scheduled(fixedDelay = ONE_DAY)
	public void restartStream() {

		stopStream();
		startStream();

	}

	@Scheduled(fixedDelay = ONE_MINUTE)
	public void restartStreamOnKeyword() {

		String[] newKeywordsArray = keywordDao.getKeywords();

		if (keywordsChanged(keywordsArray, newKeywordsArray)) {
			keywordsArray = newKeywordsArray;
			stopStream();
			startStream();		
		}
	}

	private boolean keywordsChanged(String[] oldKeywords, String[] newKeywords) {

		boolean changed = false;
		if (oldKeywords.length != newKeywords.length) {
			changed = true;
		} else {
			for (int i = 0; i < newKeywords.length; i++) {
				if (!newKeywords[i].equals(oldKeywords[i])) {
					changed = true;
					break;
				}
			}
		}
		return changed;
	}
}
