package de.htwsaar.model;

import java.util.ArrayList;
import java.util.List;

public class OutputTweet extends Tweet {

	// Final priority of the tweets = global Prio * private Prio
	private float priority;
	
	// Keywords associated with the Tweet
	private List<String> keywords;
	
	// TweetAuthor-Info
	private String name;
	private String screenName;
	private int followerCount;
	private String pictureUrl;
		
	public OutputTweet() {
		
		keywords = new ArrayList<String>();
	}
	
	public float getPriority() {
		return priority;
	}
	public void setPriority(float priority) {
		this.priority = priority;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	public void addKeyword(String keyword) {
		keywords.add(keyword);
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
