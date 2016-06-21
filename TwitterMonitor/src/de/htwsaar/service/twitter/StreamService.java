package de.htwsaar.service.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.TweetDao;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author philipp
 * 
 *         Der StreamService ist dafuer verantwortlich einen einen Stream und einen
 *         Filter zu erstellen, zu konfigurieren und den Stream zu initialisieren, indem der Filter
 *         darauf angewendet wird.
 *
 */

@Service
public class StreamService {
	
	private static final String CONSUMER_KEY = "4y9HVRAg43m3dfWoDKCWOzf9x";
	private static final String CONSUMER_SECRET = "GdZVRXMaGYn2b4PTXficnQVztCbE8eSlBJPT2zIIY5xn45zZRt";
	private static final String ACCESS_TOKEN = "712907200507850753-BNhxmqynkH6R7LyxG4GUsOf6pGP9i2L";
	private static final String ACCESS_TOKEN_SECRET = "xPLvu603NO1l1GJzZtmUNNokKqsdj1obVhrVHsHNAa0l8";

	private TwitterStream stream;
	private TweetListener tweetListener;
	private TweetDao dao;

	@Autowired
	public StreamService(TweetListener tweetListener, TweetDao dao) {
		this.tweetListener = tweetListener;
		this.dao = dao;

		startStream();
	}

	/**
	 *  Erzeugt einen Stream mithilfe der OAuth Zugangsdaten, welche dieser
	 *  App zugeordnet sind.
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
	 * Initialisiert den Stream mit den Keywords aus der Datenbank im Filter.
	 * 
	 */
	private void initStream() {

		// TODO: Keywords aus Datenbank laden.
		// List<String> keywords = dao.getKeywords();
		// String[] keywordsArray = (String[]) keywords.toArray(); -> zu
		// aufwendig!!!

		String[] keywordsArray = { "Wasser", "Deutschland", "Hamburg", "Berlin", "Paris", "America", "Trump",
				"Clinton" };

		// Erstelle Filter ..
		FilterQuery filter = new FilterQuery();
		filter.track(keywordsArray);
		filter.language("de", "en");

		// weitere Konfigurationen ...

		stream.filter(filter);

	}

	public void startStream() {
		createStream();
		initStream();
	}

	public void stopStream() {
		stream.shutdown();
	}

	public void restartStream() {
		stopStream();
		startStream();
	}
}
