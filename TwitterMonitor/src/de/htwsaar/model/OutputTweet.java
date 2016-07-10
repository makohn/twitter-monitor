package de.htwsaar.model;

import java.util.ArrayList;

public class OutputTweet extends Tweet {

	// Final priority of the tweets = global Prio * private Prio
	private float priority;
	
	// Keywords associated with the Tweet
	private ArrayList<String> keywords;
	
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
	public ArrayList<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
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
	
	@Override
	public String toString() {
		return "OutputTweet [priority=" + priority + ", keywords=" + keywords + ", name=" + name + ", screenName="
				+ screenName + ", followerCount=" + followerCount + ", pictureUrl=" + pictureUrl + "]";
	}
}
