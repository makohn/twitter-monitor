<!-- IMPORTS -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>


<!------------------------- META TAGS ---------------------------------------------->
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="stylesheet" type="text/css" href="Resources/CSS/setting.css">
	<link rel="stylesheet" type="text/css" href="Resources/CSS/header.css">
	
	<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="Resources/JS/header.js"></script>
	<script type="text/javascript" src="Resources/JS/settings.js"></script>
	
	<title>TwitterMonitor - Einstellungen</title>
</head>

<body style="margin: 0 auto;">
<!-------------------------- HEADER ------------------------------------------------->
	<div id="header"></div>

	<nav>
		<div id="logo"></div>
		<ul>
			<li><a href="${pageContext.request.contextPath}/showTweets">Tweets</a></li>
			<li><a href="${pageContext.request.contextPath}/keywords">Keywords</a></li>
			<li><a href="${pageContext.request.contextPath}/settings">Einstellungen</a></li>
			<li><a href="${pageContext.request.contextPath}/instructions">Anleitung</a></li>
			<li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
		</ul>
	
	
		<ul2 id="ul2" onclick="showMenu()">
			<li><a href="${pageContext.request.contextPath}/showTweets">Tweets</a></li>
			<li><a href="${pageContext.request.contextPath}/keywords">Keywords</a></li>
			<li><a href="${pageContext.request.contextPath}/settings">Einstellungen</a></li>
			<li><a href="${pageContext.request.contextPath}/instructions">Anleitung</a></li>
			<li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
		</ul2>
	</nav>
	
<!-------------------------- BODY --------------------------------------------------->
<div id="page">
	
	<div id="title">Einstellungen</div>
	<br>
		<div id="subtitle">Accountdaten</div>
		<div id="username">Username: ${username}</div>
		<div id="email">Email: ${email}</div>
<<<<<<< HEAD
		<div id="change_email" onclick="showNewEmail()"> ändern</div>
		
=======
	<br>	
	<div>
		<input id="newPassword">
		<button id="changePasswordButton" onClick="changePassword()">Passwort ändern</button>
	</div>
	<br>
	<div>
		<input id="newEmail">
		<button id="changeEmailButton" onClick="changeEmail()">Email ändern</button>
	</div>
	<br><br>
	<div>
	<button id="enableNotifications" onClick="enableNotifications()">
								Benachrichtigungen aktivieren / deaktivieren</button>
	</div>
	<br><br>
	<button id="deleteAccountButton" onClick="deleteAccount()">Account löschen</button>
>>>>>>> branch 'master' of https://github.com/makohn/twitter-monitor.git
	
	<div id="newEmail_wrapper">
	<label for="new_email">Neue Email</label>
	<input  name="new_email" type="text"/>
	<label for="new_email2">Bestätigen</label>
	<input  name="new_email2" type="text"/>
	<input value="Email ändern" type="submit" onClick="changeEmail()"/>
	</div>
	
	
	
	

</div>
<!----------------------------------------------------------------------------------->		
</body>
</html>