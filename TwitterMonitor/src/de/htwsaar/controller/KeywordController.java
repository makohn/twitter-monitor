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
import org.springframework.web.bind.annotation.ResponseBody;

import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.model.Keyword;
import de.htwsaar.service.user.UserService;

/**
 * The KeywordController Class is the communication interface between
 * keyword-related frontend / backend functionality such as:
 * 
 * - Displaying user-related keywords - Adding new keywords - Changing the
 * priority of existing keywords
 * 
 * @author Philipp Schaefer, Marek Kohn
 * 
 */
@Controller
public class KeywordController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * This method displays the keywords.jsp when the keyword path of the
	 * website is called.
	 * 
	 * @param model
	 *            - the Keyword Model representing a keyword that is either
	 *            displayed or added
	 */
	@RequestMapping("/keywords")
	public String loadKeywords() {
		return "keywords";
	}

	@RequestMapping("/negKeywords")
	public String loadNegKeywords() {
		return "negKeywords";
	}

	/**
	 * This method loads all the user-related keywords from the database sends
	 * them to the frontend as a JSON Object
	 * 
	 * @param principal
	 *            - the currently logged in user
	 * @returns a keyword Map that is interpreted as a JSON Array by the
	 *          frontend
	 */
	@RequestMapping(value = "getKeywords", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getKeywords(Principal principal) {

		String username = principal.getName();
		List<Keyword> keywords = userService.getKeywords(username, true);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("keywords", keywords);

		return data;
	}
	
	@RequestMapping(value = "getNegKeywords", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getNegKeywords(Principal principal) {
				
		String username = principal.getName();
		List<Keyword> keywords = userService.getKeywords(username, false);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("keywords", keywords);

		return data;
}

	/**
	 * This method provides a Keyword Bean, which can be manipulated by frontend
	 * functions, where it gets either initialized or updated.
	 * 
	 * @param keyword - a keyword bean, that is sended empty as a request and
	 *            received initialized as a response
	 * @param request - the Java representation of the REST request
	 * @param principal - the currently logged in user
	 * @returns the keyword that is inserted or updated
	 */
	@RequestMapping(value = "changePriority", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Keyword changePriority(@RequestBody Keyword keyword, HttpServletRequest request, Principal principal) throws KeywordException {
		
		String username = principal.getName();
		keyword.setUsername(username);
		userService.insertKeyword(keyword);

		return keyword;
	}
	
	/**
	 * This method launches the deletion of a user's keyword, whenever the
	 * deletion is triggered off by a UI event, e.g. if a user clicks on the
	 * 'delete cross'.
	 * 
	 * @param keyword - a keyword bean, created out of the keyword name and the
	 *            user's identity. Represents the 'to-delete' keyword.
	 * @param request - the Java representation of the REST request
	 * @param principal - the currently logged in user
	 */
	@RequestMapping(value = "deleteKeyword", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Keyword deleteKeyword(@RequestBody Keyword keyword, HttpServletRequest request, Principal principal) throws KeywordException {
		
		String username = principal.getName();
		keyword.setUsername(username);
		userService.deleteKeyword(keyword);

		return keyword;
	}
}
