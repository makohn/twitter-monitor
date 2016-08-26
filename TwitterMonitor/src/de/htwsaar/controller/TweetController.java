package de.htwsaar.controller;

import java.security.Principal;
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
	public String showTweets(Model model, Principal principal) {
				
		// braucht man nicht mehr
//		ArrayList<OutputTweet> tweets = (ArrayList<OutputTweet>) tweetService.getTweets(principal.getName());		
//		model.addAttribute("tweets", tweets);

		return "showTweets";
	}

	/**
	 * This method converts a list of Tweets into a JSON object.
	 * @return a tweet Map that is interpreted as a JSON Array by
	 * the frontend
	 */
	@RequestMapping(value = "/getTweets", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getTweets(Principal principal) {
		
		System.out.println("Controlleraufruf");
		
		List<OutputTweet> tweets = tweetService.getTweets(principal.getName());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("tweets", tweets);
		
		System.out.println("Daten geladen");
		return data;
	}
}