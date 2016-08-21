
package de.htwsaar.controller;

import javax.naming.Context;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.model.Keyword;
import de.htwsaar.model.Tweet;
import de.htwsaar.model.User;
import de.htwsaar.service.twitter.StreamService;
import de.htwsaar.service.user.UserService;

/**
 * The HomeController Class is the communication interface
 * between user-related frontend / backend functionality such as:
 * 
 * - Login
 * - Registration
 * 
 * @author Philipp Schaefer, Marek Kohn, Stefan Schloesser
 *  
 * */
@Controller
public class HomeController {

	private UserService userService;
	private StreamService streamService;
	
	@Autowired
	public void serUsersService(UserService userService, StreamService streamService) {
		this.userService = userService;
		this.streamService = streamService;
	}
	
	/**
	 * This method displays the home.jsp when the root
	 * path of the website is called.
	 * @param model - the User Model representing a
	 * user that is either singning in or up
	 */
	@RequestMapping("/")
	public String showHome(Model model){
		model.addAttribute("user", new User());
		return "home";
	}
	
	// Debug
	@RequestMapping("/test")
	public String doTest(Model model){
	
		streamService.restartStream();
		
//		System.out.println("Fehler wird verursacht");
//		
//		try {
//			Keyword invalidKeyword = new Keyword(null, null, -1);
//		} catch (KeywordException e) {
//			e.printStackTrace();
//		}
		
		model.addAttribute("user", new User());
		return "home";
	}
	
	/**
	 * This method controlls the registration process by 
	 * evaluating the user input data and forwarding the
	 * created User Object to the UserService
	 * @param user - the newly registered user
	 * @param result - the response received from the frontend
	 * @return the webpage that is called after the process is
	 * finished.
	 * 
	 */
	@RequestMapping(value="/newAccount", method=RequestMethod.POST) 
	public String newAccount(@Valid User user, BindingResult result){
		
		if(result.hasErrors()) {
			System.out.println(result);
			return "home";
		}
		
		user.setAuthority("user");
		user.setEnabled(true);	
		
		try {
			userService.insertUser(user);
		} catch (DuplicateKeyException e) {
			result.rejectValue("username",  "DuplicateKey.username", "Dieser Benutzername existiert bereits.");
			return "home";
		}
		return "showTweets";	
	}
}
