package de.htwsaar.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.htwsaar.service.user.UserService;

@Controller
public class SettingsController {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/settings")
	public String showSettings(){
		return "settings";
	}
	
	@RequestMapping("/deleteUser")
	public String deleteUser(Principal principal){
		userService.deleteUser(principal.getName());
		return "home";
	}
}
