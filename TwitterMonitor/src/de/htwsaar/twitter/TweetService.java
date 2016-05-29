package de.htwsaar.twitter;

import java.util.List;

import de.htwsaar.db.TweetDao;

public class TweetService {

	private TweetDao dao;

	public TweetService(TweetDao dao) {
		this.dao = dao;
	}

	public List<Tweet> getTweets() {
		return dao.getTweets();
	}
}
