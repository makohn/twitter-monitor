package de.htwsaar.service.twitter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.AuthorDao;
import de.htwsaar.db.TweetDao;
import de.htwsaar.model.OutputTweet;

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
	
	private TweetDao tweetDao;

	@Autowired
	public TweetService(TweetDao tweetDao, AuthorDao authorDao) {
		this.tweetDao = tweetDao;
	}

	public List<OutputTweet> getTweets(String username, String language) {		
		return tweetDao.getTweets(username, 100, language);
	}

	public List<OutputTweet> getTweetsWith(String keyword, String username, String language) {
		List<OutputTweet> tweets = tweetDao.getTweets(username, 0, language);
		
		if (keyword.isEmpty())
			return tweets;
		
		String[] keywords = keyword.split(" ");
		
		List<OutputTweet> filteredTweets = new ArrayList<OutputTweet>();
		for (String k : keywords) {
			for (OutputTweet tweet : tweets) {
				if ( tweet.getText().contains(k) )
					filteredTweets.add(tweet);
			}
			tweets = filteredTweets;
			filteredTweets = new ArrayList<OutputTweet>();
			if (tweets.isEmpty())
				return tweets;
		}
		
		return tweets;
	}
}