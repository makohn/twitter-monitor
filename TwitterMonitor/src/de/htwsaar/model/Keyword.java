package de.htwsaar.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.htwsaar.exception.model.KeywordException;
import de.htwsaar.validator.model.KeywordValidator;

public class Keyword {
	
	@NotNull
	@NotBlank(message="Das Keyword darf nicht leer sein.")
	@Size(min=5, max=49, message="Das Keyword muss zwischen 5 und 49 Zeichen lang sein.")
	private String keyword;
	
	@NotNull
	@NotBlank(message="Der Benutzername darf nicht leer sein.")
	private String username;
	
	@Min(value=1, message="Die Prioritaet muss mindestens 1 sein.")
	@Max(value=5, message="Die Prioritaet darf hoechstens 5 sein.")
	private int priority;
	
	private boolean active = true;
	
	public Keyword() {}
	
	public Keyword(String keyword, String username, int priority) throws KeywordException {		
		setKeyword(keyword);
		setUsername(username);
		setPriority(priority);
	}
	
	public Keyword(String keyword, String username, int priority, boolean active) throws KeywordException {		
		setKeyword(keyword);
		setUsername(username);
		setPriority(priority);
		setActive(active);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) throws KeywordException {		
		KeywordValidator.checkKeyword(keyword);		
		this.keyword = keyword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws KeywordException {
		KeywordValidator.checkUsername(username);
		this.username = username;
	}	

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) throws KeywordException {
		KeywordValidator.checkPriority(priority);
		this.priority = priority;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Keyword [keyword=" + keyword + ", username=" + username + ", priority=" + priority + ", active="
				+ active + "]";
	}

	
}
