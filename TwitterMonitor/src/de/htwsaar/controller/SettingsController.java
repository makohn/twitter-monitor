package de.htwsaar.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.htwsaar.model.User;
import de.htwsaar.service.user.UserService;

@Controller
public class SettingsController {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/settings")
	public String showSettings(Model model, Principal principal){
		
		model.addAttribute("username", principal.getName());
		model.addAttribute("email", userService.getEmail(principal.getName()));
		model.addAttribute("enableNotifications", userService.isNotificationEnabled(principal.getName()));
		
		return "settings";
	}
	
	@RequestMapping("/deleteUser")
	public String deleteUser(Model model, Principal principal){
		userService.deleteUser(principal.getName());
		model.addAttribute("user", new User());
		return "home";
	}
	
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(@RequestBody String newPassword, Principal principal) {
		
		userService.changePassword(principal.getName(), newPassword);
		return newPassword;
	}
	
	@RequestMapping(value = "changeEmail", method = RequestMethod.POST)
	@ResponseBody
	public String changeEmail(@RequestBody String newEmail, Principal principal) {
		
		userService.changeEmail(principal.getName(), newEmail);
		return newEmail;
	}
	
	@RequestMapping(value = "enableNotifications", method = RequestMethod.POST)
	@ResponseBody
	public String enableNotifications(@RequestBody boolean enabled, Principal principal) {
		System.out.println(enabled);
		userService.enableNotifications(principal.getName(), enabled);
		return null;
	}

}