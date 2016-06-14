package de.htwsaar.model;

import java.util.Date;

import de.htwsaar.exceptions.model.TweetException;
import de.htwsaar.validators.model.TweetValidator;
import twitter4j.Status;

public class IncomingTweet extends Tweet {
	
	private Date receivedAt;
	
	public IncomingTweet(Status status) throws TweetException {
		super(status);
		
		setReceivedAt(new Date());		
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		receivedAt = TweetValidator.checkReceivedAt(receivedAt);
		this.receivedAt = receivedAt;
	}
	
	public long getReceivedElapsed() {
		return new Date().getTime() - receivedAt.getTime();
	}
}
