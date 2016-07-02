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

import de.htwsaar.model.Keyword;
import de.htwsaar.service.user.UserService;

@Controller
public class KeywordController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/keywords")
	public String loadKeywords(Model model) {
		//model.addAttribute("keyword", new Keyword());
		return "keywords";
	}

	@RequestMapping(value = "getKeywords", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> getKeywords(Principal principal) {
		
		String username = principal.getName();
		
		List<Keyword> keywords = userService.getKeywords(username);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("keywords", keywords);

		return data;
	}
	
	@RequestMapping(value = "changePriority", method = RequestMethod.POST) 
    public @ResponseBody String changePriority(@RequestBody Keyword ukeyword, HttpServletRequest request) {
        String keyword = ukeyword.getKeyword();
        int priority = ukeyword.getPriority();
        
        return keyword + " " + priority;
    }
}
