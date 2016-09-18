package de.htwsaar.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.model.Keyword;
import de.htwsaar.model.OutputTweet;
import de.htwsaar.service.twitter.TweetService;

/**
 * The TweetController Class is responsible for
 * displaying user-related tweets 
 *  
 * @author Philipp Schaefer, Marek Kohn, Stefan Schloesser
 *  
 * */
@Controller
public class TweetController {

	private TweetService tweetService;

	@Autowired
	public void setTweetService(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	/**
	 * This method displays all the tweets associated 
	 * with user-specified keywords.
	 * @param model - the keyword model representing keywords that
	 * should be displayed.
	 * @return the showTweets webpage adjusted to the currently 
	 * logged-in user
	 */
	@RequestMapping("/showTweets")
	public String showTweets(Principal principal) {

		return "showTweets";
	}

	/**
	 * This method converts a list of Tweets into a JSON object.
	 * @return a tweet Map that is interpreted as a JSON Array by
	 * the frontend
	 */
	@RequestMapping(value = "getTweets", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getTweets(@RequestParam("lang") String language, Principal principal) {
		
		List<OutputTweet> tweets = tweetService.getTweets(principal.getName(), language);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("tweets", tweets);
		
		return data;
	}
	
//	@RequestMapping(value = "getTweetCount", method = RequestMethod.GET, headers = "Accept=application/json")
//	@ResponseBody
//	public Map<String, Object> getTweetCount(Principal principal) {
//		
//		int count = tweetService.getTweetCount(principal.getName());
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("count", count);
//		
//		return data;
//	}
	
	@RequestMapping(value = "deepSearch", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> deepSearch(@RequestParam("search") String search, @RequestParam("lang") String language, HttpServletRequest request, Principal principal) throws KeywordException {
		
		List<OutputTweet> tweets = tweetService.getTweetsWith(search, principal.getName(), language);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("tweets", tweets);
		
		return data;
	}
}