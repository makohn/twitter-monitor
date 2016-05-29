/*
 * Dieser Controller soll die Einstellungen der Anwendung konfigurieren 
 * und ggf. auch UPDATEN. 
 * 
 * getKeywords() soll die Keywords folgenderma�en verarbeiten:
 * 
 * 1. Die Keywords werden in der Anwendung eingetragen und als Liste an den Controller gegeben.
 * Zusaetzlich wird die korrespondierende User ID mitgegeben.
 * 
 * 2. Keywords und ID werden �ber den Service an die DAO-Klasse weitergegeben.
 * Weitere Konfigurationen an den Tweets etc. koennen im Service vorgenommen werden.
 * 
 * 3. Die DAO schreibt die Tweets in die Db.
 * Achtung: der Stream muss jetzt noch neugestartet werden, dies
 * wird evtl. ueber restartStream() geloest.
 * 
 */

package de.htwsaar.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.htwsaar.twitter.TweetService;

	
@Controller
public class SettingsController {

	private TweetService tweetService;
	
	@Autowired
	public void setTweetService(TweetService tweetService){
		this.tweetService = tweetService;
	}
	
	@RequestMapping(value="/settings", method=RequestMethod.POST)
	public void getKeywords(Model model, ArrayList<String>keyWords, int userId){

		//tweetService.handleKeyWords(keyWords, userId);
	}		
}
