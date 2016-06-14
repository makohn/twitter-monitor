package de.htwsaar.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.htwsaar.model.Tweet;
import de.htwsaar.services.twitter.TweetService;

@Controller
public class TweetController {

	private TweetService tweetService;

	@Autowired
	public void setTweetService(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	@RequestMapping("/showTweets")
	public String showAdd(Model model) {
		int tweetListSize = 2;

		ArrayList<Tweet> tweets = (ArrayList<Tweet>) tweetService.getTweets();
		for (int i = tweetListSize - 1; i > 0; i--) {
			tweets.add(tweets.get(i));
		}

		model.addAttribute("tweets", tweets);

		return "showTweets";
	}

	/**
	 * Converts a list of Tweets into a JSON object...
	 * @usedIn showTweets.jsp 
	 * @return
	 */
	@RequestMapping(value = "getTweets", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getTweets() {
		List<Tweet> tweets = tweetService.getTweets();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("tweets", tweets);

		return data;

	}
}