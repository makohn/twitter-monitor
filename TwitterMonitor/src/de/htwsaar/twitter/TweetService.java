package de.htwsaar.twitter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.TweetDao;

@Service
public class TweetService {

	private TweetDao dao;

	@Autowired
	public TweetService(TweetDao dao) {
		this.dao = dao;
	}

	public List<Tweet> getTweets() {
		return dao.getTweets();
	}
}
