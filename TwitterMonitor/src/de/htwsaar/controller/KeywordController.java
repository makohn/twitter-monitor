package de.htwsaar.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.model.Keyword;
import de.htwsaar.service.user.UserService;


/**
 * The KeywordController Class is the communication interface
 * between keyword-related frontend / backend functionality such as:
 * 
 * - Displaying user-related keywords 
 * - Adding new keywords 
 * - Changing the priority of existing keywords
 * 
 * @author Philipp Schaefer, Marek Kohn
 * 
 * */
@Controller
public class KeywordController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * This method displays the keywords.jsp when the keyword
	 * path of the website is called.
	 * @param model - the Keyword Model representing a
	 * keyword that is either displayed or added
	 */
	@RequestMapping("/keywords")
	public String loadKeywords(Model model) {
		//model.addAttribute("keyword", new Keyword());
		return "keywords";
	}

	/**
	 * This method loads all the user-related keywords from the database
	 * sends them to the frontend as a JSON Object
	 * @param principal - the currently logged in user
	 * @returns a keyword Map that is interpreted as a JSON Array by
	 * the frontend
	 */
	@RequestMapping(value = "getKeywords", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getKeywords(Principal principal) {
		
		String username = principal.getName();
		
		List<Keyword> keywords = userService.getKeywords(username);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("keywords", keywords);

		return data;
	}
	
	/**
	 * This method provides a Keyword Bean, which can be manipulated 
	 * by frontend functions, where it gets either initialized
	 * or updated.
	 * @param keyword - a keyword bean, that is sended empty as a request and
	 * received initialized as a response
	 * @param request - the Java representation of the request
	 * @param principal - the currently logged in user
	 */
	@RequestMapping(value = "changePriority", method = RequestMethod.POST, headers = "Accept=application/json") 
	@ResponseBody
	public Keyword changePriority(@RequestBody Keyword keyword, HttpServletRequest request, Principal principal)
		throws KeywordException {
		String username = principal.getName();
		
		keyword.setUsername(username);
		
		userService.insertKeyword(keyword);
		
		return keyword;
	}
	
	/**
	 * 
	 * @param keyword
	 * @param request
	 * @param principal
	 * @throws KeywordException
	 */
	@RequestMapping(value ="deleteKeyword", method = RequestMethod.POST, headers = "Accept=application/json")
	public void deleteKeyword(@RequestBody Keyword keyword, HttpServletRequest request, Principal principal) 
		throws KeywordException {
		String username = principal.getName();
		
		keyword.setUsername(username);
		
		userService.deleteKeyword(keyword);
	}
}
