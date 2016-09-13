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
	
	<script type="text/javascript" src="Resources/JS/header.js"></script>
	<title>TwitterMonitor - Anleitung</title>
</head>

<body style="margin: 0 auto;">
<!-------------------------- HEADER ------------------------------------------------->
	<div id="header"></div>

	<nav>
		<div id="logo"></div>
		<ul>
			<li><a href="${pageContext.request.contextPath}/showTweets">Tweets</a></li>
			<li><a href="${pageContext.request.contextPath}/keywords">Keywords</a></li>
			<li><a href="${pageContext.request.contextPath}/negKeywords">negative
					Keywords</a></li>
			<li><a href="${pageContext.request.contextPath}/settings">Einstellungen</a></li>
			<li><a href="${pageContext.request.contextPath}/instructions">Anleitung</a></li>
			<li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
		</ul>
	
	
		<ul2 id="ul2" onclick="showMenu()">
			<li><a href="${pageContext.request.contextPath}/showTweets">Tweets</a></li>
			<li><a href="${pageContext.request.contextPath}/keywords">Keywords</a></li>
			<li><a href="${pageContext.request.contextPath}/negKeywords">Blacklist</a></li>
			<li><a href="${pageContext.request.contextPath}/settings">Einstellungen</a></li>
			<li><a href="${pageContext.request.contextPath}/instructions">Anleitung</a></li>
			<li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
		</ul2>
	</nav>
	
<!-------------------------- BODY --------------------------------------------------->
<div id="page">
	
		Anleitung
		
</div>
<!----------------------------------------------------------------------------------->		
</body>
</html>