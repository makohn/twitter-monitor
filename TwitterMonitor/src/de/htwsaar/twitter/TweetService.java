package de.htwsaar.twitter;

import java.util.List;

import de.htwsaar.db.TweetDao;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author philipp
 * 
 * Der TweetService ist daf�r verantwortlich einen Filter zu erstellen, zu konfigurieren und den
 * Stream zu initialisieren, indem der Filter darauf angewendet wird. 
 *
 */

public class TweetService {

	private TwitterStream stream;		
	private TweetListener tweetListener; 	
	private TweetDao dao;
	
	public TweetService(TweetListener tweetListener, TweetDao dao)
	{
		this.tweetListener = tweetListener;
		this.dao = dao;
		
		startStream();
	}	
	
    /**
     *  
     */
    private void createStream()
    {
    	ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("4y9HVRAg43m3dfWoDKCWOzf9x");
		cb.setOAuthConsumerSecret("GdZVRXMaGYn2b4PTXficnQVztCbE8eSlBJPT2zIIY5xn45zZRt");
		cb.setOAuthAccessToken("712907200507850753-BNhxmqynkH6R7LyxG4GUsOf6pGP9i2L");
		cb.setOAuthAccessTokenSecret("xPLvu603NO1l1GJzZtmUNNokKqsdj1obVhrVHsHNAa0l8");
		
		stream = new TwitterStreamFactory(cb.build()).getInstance();
					
		stream.addListener(tweetListener);
    }
       
    
	/**
	 * 	Initialisiert den Stream mit den Keywords aus der Datenbank im Filter.
	 * 
	 */
	private void initStream() {
		

		// Filter erstellen
		FilterQuery filter = new FilterQuery();

		/*
		// aktuelle Keywords aus der Datenbank laden
		List<String> keywords = dao.getKeywords();	 // dao soll das DataBaseAccessObject sein
		String[] keywordsArray = (String[]) keywords.toArray(); // diese Konvertierung ist zu aufwendig.
																// Entweder sollte das dao gleich ein Array zurueckgeben 
																// oder die Konvertierung muss anders ablaufen
		*/
		String[] keywordsArray = { "Wasser", "Deutschland", "Hamburg", "Berlin", "Paris", "America", "Trump", "Clinton" };
		
		// Filter konfigurieren
		filter.track(keywordsArray);
		filter.language("de", "en");
		// hier wuerden dann auch eventuelle andere Einstellungen des Filters durchgefuehrt
		
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
}
