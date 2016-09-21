package de.htwsaar.model;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputTweet extends Tweet {

	private static final SimpleDateFormat CREATED_AT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// Final priority of the tweets = global Prio * private Prio
	private float priority;
	
	// TweetAuthor-Info
	private String name;
	private String screenName;
	private int followerCount;
	private String pictureUrl;
		
	public OutputTweet() {}
	
	public float getPriority() {
		return priority;
	}
	public void setPriority(float priority) {
		this.priority = priority;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public int getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	@JsonProperty("createdAt")
	public String getCreatedAtFormatted() {
		return CREATED_AT_FORMAT.format(createdAt);
	}

	@Override
	public String toString() {
		return "OutputTweet [priority=" + priority + ", name=" + name + ", screenName=" + screenName
				+ ", followerCount=" + followerCount + ", pictureUrl=" + pictureUrl + "]";
	}
}
