package de.htwsaar.twitter;

import twitter4j.Status;

public class Author {
	
	private long id;
	private String name;
	private String screenName;
	private int followerCount;
	private int favoriteCount;	
	
	
	/**
	 * Generates Author objects based on status params received in the stream
	 * @usedIn TweetListener [onStatus()]
	 * @param status
	 */
	public Author(Status status) {
		this.id = status.getUser().getId();
		this.name = status.getUser().getName();
		this.screenName = status.getUser().getScreenName();
		this.followerCount = status.getUser().getFollowersCount();
		this.favoriteCount = status.getUser().getFavouritesCount();
	}
	
	/**
	 * Generates Author objects e.g. for adding just essential attributes
	 * when an author is loaded from the database
	 * @usedIn TweetDao [getAuthor()]
	 */
	public Author() {}
	
	public long getId() {
		return id;		
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScreen_name() {
		return screenName;
	}
	public void setScreen_name(String screen_name) {
		this.screenName = screen_name;
	}
	public int getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	
	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + ", screen_name=" + screenName + ", followerCount="
				+ followerCount + ", favoriteCount=" + favoriteCount + "]";
	}	

}
