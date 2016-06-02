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
public class TweetController
{

	private TweetService tweetService;

	@Autowired
	public void setTweetService(TweetService tweetService)
	{
		this.tweetService = tweetService;
	}

	@RequestMapping("/showTweets")
	public String showAdd(Model model)
	{
		int tweetListSize = 20;
		
		ArrayList<Tweet> tweets = (ArrayList<Tweet>) tweetService.getTweets();
		
//		for (int i=tweetListSize-1;i > 0;i--)
//		{
//			tweets1.add(tweetsRow.get(tweetsRow.size()-i));
//		}
//		for (Tweet tweet : tweets1)
//		{
//			tweets.add(tweet);
//			if (tweetListSize == tweets.size())
//			{
//				break;
//			}
//		}
		
		model.addAttribute("tweets", tweets);

		return "showTweets";
	}

}
