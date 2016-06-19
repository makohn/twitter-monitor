package de.htwsaar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.htwsaar.exceptions.model.TweetException;
import de.htwsaar.validators.model.TweetValidator;
import twitter4j.MediaEntity;
import twitter4j.Status;

public class Tweet {
	
	private long tweetId; 
	private long authorId;
	private String text; 
	private Date createdAt; 
	private String place;
	private int favoriteCount;
	private int retweetCount;
	private List<String> urls;
	
	public Tweet() {		
		urls = new ArrayList<String>();
	}

	/**
	 * Initialisiert ein Tweet Objekt mit den Daten aus dem Status
	 * @param status
	 * @throws TweetException 
	 */
	public Tweet(Status status) throws TweetException {
		
		this();
		
		TweetValidator.checkStatus(status);		
		
		setTweetId(status.getId());
		setAuthorId(status.getUser());
		setText(status.getText());				
		setCreatedAt(status.getCreatedAt());
		setPlace(status.getPlace());
		setFavoriteCount(status.getFavoriteCount());
		setRetweetCount(status.getRetweetCount());
		setUrls(status.getMediaEntities());
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) throws TweetException {		
		TweetValidator.checkTweetId(tweetId);		
		this.tweetId = tweetId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) throws TweetException {
		TweetValidator.checkAuthorId(authorId);
		this.authorId = authorId;
	}
	
	public void setAuthorId(twitter4j.User user) throws TweetException {
		TweetValidator.checkUser(user);
		setAuthorId(user.getId());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) throws TweetException {
		text = TweetValidator.checkText(text);
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		createdAt = TweetValidator.checkCreatedAt(createdAt);
		this.createdAt = createdAt;
	}
	
	public long getAge( ) {
		return new Date().getTime() - getCreatedAt().getTime();
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		TweetValidator.checkPlace(place);
		this.place = place;
	}
	
	public void setPlace(twitter4j.Place place) {	
		if (place == null)
			setPlace("");
		else
			setPlace(place.getCountry());
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		favoriteCount = TweetValidator.checkFavoriteCount(favoriteCount);
		this.favoriteCount = favoriteCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		retweetCount = TweetValidator.checkRetweetCount(retweetCount);
		this.retweetCount = retweetCount;
	}
	
	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> list) {
		list = TweetValidator.checkUrls(list);
		this.urls = list;
	}
	
	private void setUrls(MediaEntity[] mediaEntities) {		
		mediaEntities = TweetValidator.checkUrls(mediaEntities);		
		ArrayList<String> urlList = new ArrayList<String>();
		for (MediaEntity m : mediaEntities) {
			urlList.add(m.getMediaURL());
		}
		setUrls(urlList);		
	}
	
	@Override
	public boolean equals(Object object) {
		if ( object instanceof Tweet ) {
			if ( ((Tweet) object).getTweetId() == getTweetId() )
				return true;
		}		
		return false;
			
	}

	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", authorId=" + authorId + ", text=" + text + ", createdAt=" + createdAt
				+ ", place=" + place + ", favoriteCount=" + favoriteCount + ", retweetCount=" + retweetCount + ", urls="
				+ urls + "]";
	}

	

}
