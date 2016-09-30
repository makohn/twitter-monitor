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

		<div id="change_email" class="button_hover" onclick="showNewEmail()"> Email ändern</div>
		<div id="change_password" class="button_hover" onclick="showNewPassword()"> Password ändern</div>
	
	<div id="newEmail_wrapper">
	<label for="new_email">Neue Email</label>
	<input  name="new_email" type="text"/>
	<label for="new_email2">Bestätigen</label>
	<input  name="new_email2" type="text"/>
	<input value="Email ändern" type="submit" onClick="changeEmail()"/>
	<div class="back" onclick="hideNewEmail()"> zurück</div>
	</div>
	
	<div id="newPassword_wrapper">
	<label for="new_password">Neues Passwort</label>
	<input  name="new_password" type="password"/>
	<label for="new_password2">Bestätigen</label>
	<input  name="new_password2" type="password"/>
	<input value="Password ändern" type="submit" onClick="changePassword()"/>
	<div class="back" onclick="hideNewPassword()"> zurück</div>
	</div>
	<div id="message_wrapper">
	<div id="message_label">Möchten Sie per Email benachrichtigt werden?</div>
	<input type="radio" id="Message_Service" name="Message_Service" value="Ja"  
		${enableNotifications == 'true' ? 'checked' : ''}>
    <label for="Message_Service"> Ja&nbsp;&nbsp;&nbsp;&nbsp;</label><br> 
	<input type="radio" id="Message_Service" name="Message_Service" value="Nein"
		${enableNotifications == 'false' ? 'checked' : ''}>
    <label for="Message_Service"> Nein&nbsp;</label><br> 
    <div id="enable_notifications" class="button_hover" onclick="enableNotifications()"> Bestätigen</div>
	</div>
	
	<div id="delete_account" onclick="deleteAccount()"> Account löschen</div>
	

</div>
<!----------------------------------------------------------------------------------->		
</body>
</html>