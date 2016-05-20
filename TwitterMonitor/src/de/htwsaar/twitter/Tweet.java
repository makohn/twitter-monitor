package de.htwsaar.twitter;

import java.util.Date;

import twitter4j.Status;

public class Tweet {
	
	private int tweetId; 
	private int authorId;
	private String text; 
	private Date createdAt; 
	private String place;
	private int favoriteCount;
	private int retweetCount;
	
	public Tweet() {
		
	}

	public Tweet(Status status) {
		this.tweetId = (int) status.getId();
		this.authorId = (int) status.getUser().getId();
		this.text = status.getText();
		this.createdAt = status.getCreatedAt();
		this.place = status.getPlace().getCountry();
		this.favoriteCount = status.getFavoriteCount();
		this.retweetCount = status.getRetweetCount();
	}

	public int getTweetId() {
		return tweetId;
	}

	public void setTweetId(int tweetId) {
		this.tweetId = tweetId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
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

	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", authorId=" + authorId + ", text=" + text + ", createdAt=" + createdAt
				+ ", place=" + place + ", favoriteCount=" + favoriteCount + ", retweetCount=" + retweetCount + "]";
	}
	
	
	
	
	
}
