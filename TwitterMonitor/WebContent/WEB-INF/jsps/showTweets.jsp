<!-- IMPORTS -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>

<!------------------------- META TAGS ---------------------------------------------->
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="stylesheet" type="text/css" href="Resources/CSS/showTweets.css">
	
	<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="Resources/JS/tweets.js"></script>
	
	<title>Focus on</title>
</head>

<body style="margin: 0 auto;">
<!-------------------------- HEADER ------------------------------------------------->

<div id="header"></div>

	<nav>
		<div id="logo"></div>
		<ul>
			<li><a href="home.php">Home</a></li>
			<li><a href="${pageContext.request.contextPath}/showTweets">Tweets</a></li>
			<li><a href="#">Kategorien</a></li>
			<li><a href="${pageContext.request.contextPath}/keywords">Profil</a></li>
			<li><a href="#">Einstellungen</a></li>
		</ul>
	</nav>
	
<!-------------------------- BODY --------------------------------------------------->
<div id="page">
		<!--############# Side Bar ####################-->
		<div id="sidebar">

			<div id="username">Hallo ${pageContext.request.userPrincipal.name}</div>
			<div id="welcome_text">Seit dem letzten Login haben wir 230 Tweets für dich gesammelt</div>
			<label for="search">Search</label> <input id="search" onkeyup="search()" type="search">
			<label for="sort">Sort</label> 
			<select id="sortOption" onchange="sort()">
				<option>-Bitte wählen-</option>
				<option>Priorität</option>
				<option>Name</option>
				<option>Zeit</option>
				
			</select>
			
		</div>
		
		<!--############# Tweet Panel ##################-->
		<div id="tweet_panel"></div>
			<script type="text/javascript">
				function onLoad() {
					$.getJSON("<c:url value="/getTweets"/>", updateTweets);
				} onLoad();
			</script>
	   </div>
	   
<!----------------------------------------------------------------------------------->
</body>
</html>
