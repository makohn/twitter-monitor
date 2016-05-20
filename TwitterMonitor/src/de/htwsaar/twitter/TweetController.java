package de.htwsaar.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TweetController {
	
	private TweetService tweetService;
	
	@Autowired
	public void setTweetService(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	@RequestMapping("/showTweet")
	public String showAdd(Model model){
					
		Tweet tweet = tweetService.getTweets().get(0);
		
		model.addAttribute("tweet", tweet);
		
		return "showTweet";
	}

}
