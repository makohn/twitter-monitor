<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Focus on</title>
<link rel="stylesheet" type="text/css" href="Resources/CSS/home.css">
<link rel="stylesheet" type="text/css" href="Resources/CSS/Animate.css">

<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
<script type="text/javascript" src="Resources/JS/homeAnimation.js"></script>
<script type="text/javascript" src="Resources/JS/home.js"></script>

</head>
<body style="margin: 0 auto;">
	<div id="header_deko"></div>
	<header>

	<div id="logo"></div>
	<div id="login_panel">
		<div id="login_title">Log in</div>
		<form name='f'
			action='${pageContext.request.contextPath}/j_spring_security_check'
			method='POST'>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> <label for="login_user">Username</label> <input
				id="textfield_benutzer" name='j_username' type=text> <label
				for="login_password">Password</label> <input id="textfield_password"
				type=password name='j_password'>
			<div id="new_account" onclick="newUser()">Neues Benutzerkonto
				erstellen</div>
			<input id="loginButton" type="submit">
		</form>
	</div>
	</header>

	<div id="page">
		<div id="animation">
			<div id="bird_pic"></div>
			<div id="text_comic">
				Haben Sie die Übersicht
				<p></p>
				verloren ?
			</div>
			<div id="runningMail_pic"></div>
		</div>

		<div id="createAccount">


			<form>

				<label for="user_name">Username</label> <input path="user"
					name="username" type="text"> <label for="user_email">Email</label>
				<input path="email" name="email" type="email"> <label
					for="user_password">Password</label> <input path="password"
					name="password" type="password"> <input
					value="Erstelle Benutzerkonto" type="submit" />

			</form>
		</div>
	</div>
</body>
</html>
