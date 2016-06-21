package de.htwsaar.service.twitter;

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
			
	private TweetService tweetService;
					
	public TweetListener(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	/* (non-Javadoc)
	 * Triggers the tweetService to insert Objects into the
	 * database whenever a status object is received
	 * @see twitter4j.StatusListener#onStatus(twitter4j.Status)
	 */
	@Override
	public void onStatus(Status status) {
		tweetService.insertStatus(status);
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
