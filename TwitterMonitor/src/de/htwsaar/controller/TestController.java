/*
 * 
 * Dieser Kontroller dient reinen Testzwecken und soll lediglich die Arbeitsweise verdeutlichen
 * 
 */


package de.htwsaar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping("/")
	public String showHome(Model testModel){
		
		testModel.addAttribute("name", "John Purcell");
		
		return "TestFile";
	}
}
