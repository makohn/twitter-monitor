package de.htwsaar.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import de.htwsaar.exception.model.UserException;
import de.htwsaar.validator.model.UserValidator;


/**
 * The User Class serves mainly as a data container for User Objects.
 * Each User has several attributes:
 *  - username, a distinct identifier for a user.
 *  - password, a password that authenticates a user.
 *  - email, the contact address of a user.
 *  - enabled, a switch to turn user accounts on and off.
 *  - authority, the user's role, e.g. admin
 *  
 * @author Philipp Schaefer
 *
 */
public class User {
	
	@NotBlank/*(message="Benutzername darf nicht leer sein.")*/
	@Size(min=5, max=59)
	@Pattern(regexp="^\\w{5,}$"/*, message="Benutzername darf nur aus Buchstaben, Zahlen und Unterstrich bestehen."*/)
	private String username;
	
	@NotBlank/*(message="Das Passwort darf nicht leer sein.")*/
	@Pattern(regexp="^\\S+"/*, message="Das Passwort darf keine Leerzeichen enthalten."*/)
	@Size(min=8, max=15/*, message="Das Passwort muss zwischen 8 und 15 Zeichen lang sein."*/)
	private String password;
	
	@NotBlank/*(message="Die Email-Adresse darf nicht leer sein")*/
	@Email/*(message="Sie müssen eine gültige Email-Adresse angeben")*/
	private String email;
	
	private boolean enabled = false;	
	private String authority;
	
	public User() {}
	
	public User(String username, String password, String email, String authority) throws UserException {
		
		setUsername(username);
		setAuthority(authority);
		setEmail(email);
		setPassword(password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		email = UserValidator.checkEmail(email);
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws UserException {
		UserValidator.checkPassword(password);
		this.password = password;
	}	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", enabled=" + enabled + ", email=" + email
				+ ", authority=" + authority + "]";
	}	
}