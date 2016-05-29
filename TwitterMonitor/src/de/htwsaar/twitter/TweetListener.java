package de.htwsaar.twitter;

import de.htwsaar.db.TweetDao;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * @author philipp
 *
 * Der TweetListener hoert den Stream nach neuen Tweets ab und schreibt (zunaechst ausnahmslos alle) Tweets in
 * die Datenbank.
 */

public class TweetListener implements StatusListener {
			
	private TweetDao dao;
					
	public TweetListener(TweetDao dao) {
		this.dao = dao;
	}

	@Override
	public void onStatus(Status status) {
		
		Tweet latestTweet = new Tweet(status);
		Author author = new Author(status);
		
		System.out.println(latestTweet.toString());
		
		dao.insertAuthor(author);
		dao.insertTweet(latestTweet);
		
	}

	@Override
	public void onException(Exception arg0) {
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
	}	

}
