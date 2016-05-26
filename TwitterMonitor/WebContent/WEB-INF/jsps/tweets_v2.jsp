<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Focus on</title>

<link rel="stylesheet" type="text/css" href="CSS/tweets_v2.css">
<script type="text/javascript" src="JS/tweets.js"></script>
<script type="text/javascript" src="JS/jquery-2.1.4.js"></script>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>



</head>
<body style="margin: 0 auto;">

	<header>
	<div id="logo">
		<img src="Picture/hummingbird.png" alt="logo">
	</div>
	</header>

	<nav>
	<ul>
		<li><a href="home">Home</a></li>
		<li><a href="#">Tweets</a></li>
		<li><a href="#">Kategorien</a></li>
		<li><a href="#">Profil</a></li>
		<li><a href="#">Einstellungen</a></li>
	</ul>
	</nav>
	<div id="page">
		<div id="sidebar">

			<label for="search">Search</label> <input type="search"> <label
				for="sort">Sort</label> <select>
				<option>Name</option>
				<option>Zeit</option>
			</select>

		</div>
		<c:forEach var="row" items="${tweets}">
			<div id="tweet_panel">

				

				<div id="tweet_time">
					<c:out value="${row.getCreatedAt().toString()}" />
				</div>
				<div id="tweet_rating"></div>
				<div id="tweet_text">
					<c:out value="${row.getText()}" />
				</div>
			</div>
		</c:forEach>
	</div>

</body>
</html>


