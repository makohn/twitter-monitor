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
	
		Anleitung<br>
		<br>
		<br>
		KEYWORDS<br>
		<br>
		1)	Wählen Sie bis zu 10 Keywords aus<br>
		<br>
			- es werden für Sie die Tweets gesammelt, die diese Keywords enthalten<br>
			<br>
			- von den gesammelten Tweets werden Ihnen bis zu 100 angezeigt<br>
			<br>
			- dabei handelt es sich um eine sehr weite Suche<br>
			<br>
				z.B.: "trump"		findet alle Tweets mit	"trump"<br>
															"Trump"<br>
															"tRuMp"<br>
															"#trump"<br>
															"Strumpf"<br>
			<br>
			- Sie können auch mehrere Keywords eingeben<br>
			<br>
				z.B.: "fußball bundesliga"<br>
			<br>
		2) Geben Sie jedem Keyword eine Priorität von 1 bis 5<br>
		<br>
			- jeder angezeigte Tweet erhält eine Priorität, die sich aus<br>
			<br>
					a)	allgemeinen Faktoren (Retweets, Likes, Follower)<br>
					<br>
				und	b)	Ihrer privaten Priorität 				ergibt<br>
				<br>
			- Tweets mit mehreren Ihrer Keywords erhalten eine höhere Priorität<br>
			<br>
		3) Setzen Sie unliebsame Themen auf Ihre Blacklist<br>
		<br>
			- Sie können beliebig viele Keywords auf die Blacklist setzen<br>
		<br>
			- Tweets mit Keywords von der Blacklist werden nicht angezeigt<br>
			<br>
			- die Blacklist-Keywords sind case-sensitive<br> 
			<br>
				z.B.:	"trump"			filtert nur		"trump"<br>
										aber nicht		"Trump"<br>
				<br>
		TWEETS<br>
					<br>					
		4) Sortieren Sie die angezeigten Tweets<br>
		<br>
				a) nach der Priorität der Tweets<br>
				<br>
				b) nach dem Entstehungszeitpunkt der Tweets<br>
				<br>
		5) Filtern Sie die angezeigten Tweets hinsichtlich eines beliebigen Stichworts<br>
<br>
		
</div>
<!----------------------------------------------------------------------------------->		
</body>
</html>