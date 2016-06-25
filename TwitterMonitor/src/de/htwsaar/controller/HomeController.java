
package de.htwsaar.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.htwsaar.model.User;
import de.htwsaar.service.user.UserService;

@Controller
public class HomeController {

	private UserService userService;
	
	@Autowired
	public void serUsersService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/")
	public String showHome(Model model){
		model.addAttribute("user", new User());
		return "home";
	}
	
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
