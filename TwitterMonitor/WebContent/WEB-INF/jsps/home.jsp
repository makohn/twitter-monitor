<!-- IMPORTS -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>

<html>

<!------------------------- META TAGS ---------------------------------------------->
<head>
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>	
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="stylesheet" type="text/css" href="Resources/CSS/home.css">
	<link rel="stylesheet" type="text/css" href="Resources/CSS/Animate.css">
	
	<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="Resources/JS/homeAnimation.js"></script>
	<script type="text/javascript" src="Resources/JS/home.js"></script>
	
	<title>TwitterMonitor - Home</title>
</head>

<body style="margin: 0 auto;">
<div id="header_deko"></div>
<!-------------------------- HEADER ------------------------------------------------->
<header>

	<div id="logo"></div>
	
	<div id="login_panel">
		<div id="login_title">Login</div>
		<form 	name='f'
				action='${pageContext.request.contextPath}/j_spring_security_check'
				method='POST'>
			<input 	type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
			<label 	for="login_user">Username</label> 
			<input 	id="textfield_benutzer" name='j_username' type=text> 
			<label	for="login_password">Password</label> 
			<input id="textfield_password"	type=password name='j_password'>
			<input id="loginButton" type="submit">
			<div id="new_account" onclick="newUser()">Neues Benutzerkonto erstellen</div>
		</form>
	</div>
	
</header>
<!-------------------------- BODY --------------------------------------------------->
	
<div id="page">
	
		<!-- ################# Animation ################### -->
		<div id="animation">
			<div id="bird_pic"></div>	
			<div id="text_comic"> Haben Sie die Übersicht<p></p>verloren ?</div>
			<div id="runningMail_pic"></div>
		</div>
			
		<!-- ################# Registration ################# -->
		<div id="createAccount">
		
			<sf:form method="post" action="${pageContext.request.contextPath}/newAccount" commandName="user">
				<label for="user_name">Username</label>
				<sf:input path="username" name="username" type="text"/>
				<label for="user_email">Email</label>
				<sf:input path="email" name="email" type="email"/>
				<label for="user_password">Password</label>
				<sf:input path="password" name="password" id="new_passwd" onkeyup="evalPasswd()" type="password"/> 
				<div id="passwd_line"></div>
				<input value="Erstelle Benutzerkonto" type="submit" />
			</sf:form>
		</div>
		
</div> <!-- id="page" -->

<!----------------------------------------------------------------------------------->	
</body>
</html>
