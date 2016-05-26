package de.htwsaar.twitter;

import java.util.ArrayList;
import java.util.Date;

import twitter4j.MediaEntity;
import twitter4j.Status;

public class Tweet {
	
	private int tweetId; 
	private int authorId;
	private String text; 
	private Date createdAt; 
	private String place;
	private int favoriteCount;
	private int retweetCount;
	private ArrayList<String> urls;
	
	public Tweet() {}

	public Tweet(Status status) {
		this.tweetId = (int) status.getId();
		this.authorId = (int) status.getUser().getId();
		this.text = status.getText();
		this.createdAt = status.getCreatedAt();
		this.place = status.getPlace().getCountry();
		this.favoriteCount = status.getFavoriteCount();
		this.retweetCount = status.getRetweetCount();
		
		this.urls = new ArrayList<String>();
		MediaEntity[] me = status.getMediaEntities();
		for (MediaEntity m : me)
			urls.add(m.getURL());	// hier gibt es mehrere Methoden gibt es eine spezielle ??? @stefan
		
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
	
	public ArrayList<String> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", authorId=" + authorId + ", text=" + text + ", createdAt=" + createdAt
				+ ", place=" + place + ", favoriteCount=" + favoriteCount + ", retweetCount=" + retweetCount + ", urls="
				+ urls + "]";
	}

	

}
