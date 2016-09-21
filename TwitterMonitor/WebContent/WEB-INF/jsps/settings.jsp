<!-- IMPORTS -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>


<!------------------------- META TAGS ---------------------------------------------->
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="stylesheet" type="text/css" href="Resources/CSS/keywords.css">
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
	
	<h2>Einstellungen</h2>
	<br>
		<h3>Accountdaten</h3>
		<div id="username">Username: ${username}</div>
		<div id="email">Email: ${email}</div>
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
	<button>Benachrichtigungen aktivieren / deaktivieren</button>
	<br><br>
	<button id="deleteAccountButton" onClick="deleteAccount()">Account löschen</button>
	
</div>
<!----------------------------------------------------------------------------------->		
</body>
</html>