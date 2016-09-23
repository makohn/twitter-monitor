<!-- IMPORTS -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>


<!------------------------- META TAGS ---------------------------------------------->
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<link rel="stylesheet" type="text/css" href="Resources/CSS/instruction.css">
<link rel="stylesheet" type="text/css" href="Resources/CSS/header.css">

<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
<script type="text/javascript" src="Resources/JS/header.js"></script>
<script type="text/javascript" src="Resources/JS/instruction.js"></script>
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
		
		<div id="title">Anleitung</div>
		<ol>
			<div class="section">KEYWORDS</div>
			
			
			<li  onclick="display(1)">Wählen Sie bis zu 10 Keywords aus
				<ul class="slide">
					<li>es werden für Sie die Tweets gesammelt, die diese Keywords enthalten</li>
					<li>dabei handelt es sich um eine sehr weite Suche
						<ul> z.B. 'trump' findet alle Tweets mit:
							<li>trump</li>
							<li>Trump</li>
							<li>tRuMp</li>
							<li>#trump</li>
							<li>Strumpf</li>
						</ul>
					</li>
					<li>Sie können auch mehrere Keywords eingeben, wenn alle Wörter im Tweet vorkommen sollen
						<ul>z.B. 'fußball bundesliga'</ul>
					</li>					
				</ul>
			</li>
			
			
			
			<li onclick="display(2)">Geben Sie jedem Keyword eine Priorität von 1 bis 5
				<ul class="slide">
					<li ">jeder angezeigte Tweet erhält eine Priorität, die sich ergibt aus
						<ul>
							<li ">allgemeinen Faktoren (Retweets, Likes, Follower)</li>
							<li ">Ihrer privaten Priorität</li>
						</ul>
					</li>
					<li>Tweets mit mehreren Ihrer Keywords erhalten eine höhere Priorität</li>
				</ul>
			</li>
			
			
			
			<li  onclick="display(3)">Setzen Sie unliebsame Themen auf Ihre Blacklist
				<ul class="slide">
					<li>Sie können beliebig viele Keywords auf die Blacklist setzen</li>
					<li>Tweets mit Keywords von der Blacklist werden nicht angezeigt</li>
					<li>die Blacklist-Keywords beachten Groß- und Kleinschreibung
						<ul>z.B. 'trump' filtert nur 'trump', aber nicht 'Trump'</ul>
					</li>
				</ul>
			</li>
			
			<br>
			</ol>
			<ol>
			<div class="section"> TWEETS </div>
			
			
			<li  onclick="display(4)">Es werden Ihnen zunächst bis zu 100 Tweets angezeigt
				<ul class="slide">
					<li>dabei handelt es sich um die interessantesten Tweets (höchste Priorität)</li>
					<li >klicken Sie auf "Tweets aktualisieren", um die neuesten Tweets zu laden</li>
				</ul>
			</li> 
			
			
			
			<li  onclick="display(5)">Sortieren Sie die angezeigten Tweets
				<ul class="slide">
					<li>nach der Priorität der Tweets</li>
					<li>nach dem Entstehungszeitpunkt der Tweets</li>
				</ul>
			</li>
			
			<br>
			
			<li >Filtern Sie die angezeigten Tweets hinsichtlich eines beliebigen Stichworts</li>
			
			
			
			<li  onclick="display(6)" >Stellen Sie eine Sprache ein
				<ul class="slide">
					<li>es können Deutsch, Englisch und Alle gewählt werden</li>
					<li>nicht alle Tweets sind mit einer Sprache markiert</li>
				</ul>
			</li>
			
			<br>
			
			<li  onclick="display(7)" >DeepSearch
				<ul class="slide">
					<li>durchsuchen Sie alle für Sie gesammelten Tweets</li>
					<li>das Ergebnis kann sehr groß werden</li>
					<li>bei der Eingabe von mehreren Stichwörtern müssen diese alle in einem Tweet enthalten sein</li>
					<li>eine DeepSearch mit leerem Suchwort liefert alle vorhandenen Tweets</li>
					<li>das Ergebnis der Suche kann danach weitergefiltert und -sortiert werden</li> 
				</ul>
			</li>
	
		</ol>
		<br>

	</div>
	<!----------------------------------------------------------------------------------->
</body>
</html>