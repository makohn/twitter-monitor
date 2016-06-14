package de.htwsaar.validators.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.htwsaar.exceptions.model.TweetException;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

public class TweetValidator {
	
	public static void checkStatus(Status status) throws TweetException {
		if ( status == null )
			throw new TweetException();		
	}
	
	public static void checkTweetId(long tweetId) throws TweetException {
		if ( tweetId < 0 )
			throw new TweetException();
	}

	public static void checkAuthorId(long authorId) throws TweetException {
		if ( authorId < 0 )
			throw new TweetException();		
	}

	public static void checkUser(User user) throws TweetException {
		if ( user == null )
			throw new TweetException();		
	}

	public static String checkText(String text) throws TweetException {
		if ( text == null )
			throw new TweetException();
		if ( text.length() > 160 )
			text = text.substring(0, 159);
		
		return text;
	}

	public static Date checkCreatedAt(Date createdAt) {
		if ( createdAt == null )
			createdAt = new Date();
		return createdAt;
	}

	public static String checkPlace(String place) {
		if ( place == null )
			place = "";
		return place;
	}

	public static int checkFavoriteCount(int favoriteCount) {
		if ( favoriteCount < 0 )
			favoriteCount = 0;
		return favoriteCount;
	}

	public static int checkRetweetCount(int retweetCount) {
		if ( retweetCount < 0 )
			retweetCount = 0;
		return retweetCount;
	}

	public static List<String> checkUrls(List<String> list) {
		if ( list == null )
			list = new ArrayList<String>();
		return list;
	}

	public static MediaEntity[] checkUrls(MediaEntity[] mediaEntities) {
		if ( mediaEntities == null )
			mediaEntities = new MediaEntity[0];
		return mediaEntities;		
	}

	public static Date checkReceivedAt(Date receivedAt) {
		if ( receivedAt == null )
			receivedAt = new Date();
		return receivedAt;
	}
	
}
