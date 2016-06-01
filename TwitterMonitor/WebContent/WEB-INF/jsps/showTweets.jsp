<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<html>

<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Focus on</title>

<link rel="stylesheet" type="text/css"
	href="Resources/CSS/showTweets.css">

<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>



</head>
<body style="margin: 0 auto;">

	<div id="header"></div>

	<nav>
	<div id="logo"></div>

	<ul>
		<li><a href="home.php">Home</a></li>
		<li><a href="#">Tweets</a></li>
		<li><a href="#">Kategorien</a></li>
		<li><a href="${pageContext.request.contextPath}/profil">Profil</a></li>
		<li><a href="#">Einstellungen</a></li>
	</ul>
	<ul2>
	<li><a href="home.php">Home</a></li>
	<li><a href="#">Tweets</a></li>
	<li><a href="#">Kategorien</a></li>
	<li><a href="#">Profil</a></li>
	<li><a href="#">Einstellungen</a></li>
	</ul2> </nav>
	<div id="page">
		<div id="sidebar">

			<div id="username">Hallo Username</div>
			<div id="welcome_text">Seit dem letzten Login haben wir 230
				Tweets für dich gesammelt</div>
			<label for="search">Search</label> <input type="search"> <label
				for="sort">Sort</label> <select>
				<option>Name</option>
				<option>Zeit</option>
			</select>

		</div>
		<c:forEach var="row" items="${tweets}">
			<div id="tweet_panel">

				<c:if test="${row.getUrls().size() > 0}">
					<c:set value="${row.getUrls().get(0)}" var="pic"></c:set>

				</c:if>
				<div id="tweet_pic" style="background-image:url(${pic})"></div>
				<c:set value="" var="pic"></c:set>
				<div id="tweet_time">
					
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
