package de.htwsaar.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.htwsaar.twitter.Tweet;
import de.htwsaar.twitter.TweetService;

@Controller
public class TweetController {

	private TweetService tweetService;

	@Autowired
	public void setTweetService(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	@RequestMapping("/showTweets")
	 public String showAdd(Model model)
	 {
	  int tweetListSize = 2;
	  
	  ArrayList<Tweet> tweets = (ArrayList<Tweet>) tweetService.getTweets();
	  ArrayList<Tweet> tweets1 = new ArrayList<Tweet>();
	  for (int i=tweetListSize-1;i > 0;i--)
	  {
	   tweets1.add(tweets.get(i));
	  }
	  
	  
	  model.addAttribute("tweets", tweets);
	        
	  return "showTweets";
	 }
}