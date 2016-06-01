package de.htwsaar.twitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * Initializes a tweet object with information gained from the
	 * received status object.
	 * @param status
	 */
	public Tweet(Status status) {
		this.tweetId = status.getId();
		this.authorId = status.getUser().getId();
		this.text = status.getText();
		this.createdAt = status.getCreatedAt();
		this.place = status.getPlace().getCountry();
		this.favoriteCount = status.getFavoriteCount();
		this.retweetCount = status.getRetweetCount();
		
		this.urls = new ArrayList<String>();
		MediaEntity[] me = status.getMediaEntities();
		for (MediaEntity m : me)
			urls.add(m.getMediaURL());	
		
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}
	
	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> list) {
		this.urls = list;
	}

	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", authorId=" + authorId + ", text=" + text + ", createdAt=" + createdAt
				+ ", place=" + place + ", favoriteCount=" + favoriteCount + ", retweetCount=" + retweetCount + ", urls="
				+ urls + "]";
	}

	

}
