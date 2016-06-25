package de.htwsaar.model;

import de.htwsaar.exception.model.AuthorException;
import de.htwsaar.validator.model.AuthorValidator;
import twitter4j.Status;

public class Author {
	
	private long authorId;
	private String name;
	private String screenName;
	private int followerCount;
	private String pictureUrl;
		
	/**
	 * Generates Author objects based on status params received in the stream
	 * @usedIn TweetListener [onStatus()]
	 * @param status
	 * @throws AuthorException 
	 */
	public Author(Status status) throws AuthorException {
		
		AuthorValidator.checkStatus(status);		
		
		twitter4j.User user = status.getUser();
		
		setAuthorId(user.getId());
		setName(user.getName());
		setScreenName(user.getScreenName());
		setFollowerCount(user.getFollowersCount());
		setPictureUrl(user.getProfileImageURL());	
	}
	
	/**
	 * Generates Author objects e.g. for adding just essential attributes
	 * when an author is loaded from the database
	 * @usedIn TweetDao [getAuthor()]
	 */
	public Author() {}
	
	public long getAuthorId() {
		return authorId;		
	}
	public void setAuthorId(long authorId) throws AuthorException {
		AuthorValidator.checkId(authorId);
		this.authorId = authorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		name = AuthorValidator.checkName(name);
		this.name = name;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		screenName = AuthorValidator.checkScreenName(screenName);
		this.screenName = screenName;
	}
	public int getFollowerCount() {
		return followerCount;
	}
	
	public void setFollowerCount(int followerCount) {
		followerCount = AuthorValidator.checkFollowerCount(followerCount);
		this.followerCount = followerCount;
	}

	public String getPictureUrl() {		
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		pictureUrl = AuthorValidator.checkPictureUrl(pictureUrl);
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String toString() {
		return "Author [authorId=" + authorId + ", name=" + name + ", screenName=" + screenName + ", followerCount="
				+ followerCount + ", pictureUrl=" + pictureUrl + "]";
	}
}