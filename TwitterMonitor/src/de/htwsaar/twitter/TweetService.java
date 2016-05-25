package de.htwsaar.twitter;

import java.util.ArrayList;
import java.util.List;

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
 *         Der TweetService ist dafuer verantwortlich einen Filter zu erstellen,
 *         zu konfigurieren und den Stream zu initialisieren, indem der Filter
 *         darauf angewendet wird.
 *
 */

@Service
public class TweetService {

	private TwitterStream stream;
	private TweetListener tweetListener;
	private TweetDao dao;

	@Autowired
	public TweetService(TweetListener tweetListener, TweetDao dao) {
		this.tweetListener = tweetListener;
		this.dao = dao;

		startStream();
	}

	/**
	 *  
	 */
	private void createStream() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("4y9HVRAg43m3dfWoDKCWOzf9x");
		cb.setOAuthConsumerSecret("GdZVRXMaGYn2b4PTXficnQVztCbE8eSlBJPT2zIIY5xn45zZRt");
		cb.setOAuthAccessToken("712907200507850753-BNhxmqynkH6R7LyxG4GUsOf6pGP9i2L");
		cb.setOAuthAccessTokenSecret("xPLvu603NO1l1GJzZtmUNNokKqsdj1obVhrVHsHNAa0l8");

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

	public List<Tweet> getTweets() {
		return dao.getTweets();
	}
	
	public void handleKeyWords(ArrayList<String> keyWords, int userId){
		// Hier wird dao.insertKeys(keyWords, Id) oder etwas dergleichen stehen, muss noch getestet werden
		// danach evtl. restartStream()
	}
}
