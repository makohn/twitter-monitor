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
			<label for="search">Search</label> <input type="search">
			<label for="sort">Sort</label> 
			<select>
				<option>Name</option>
				<option>Zeit</option>
			</select>
		</div>
		
		<!--############# Tweet Panel ##################-->
		<div id="tweet_panel"></div>
			<script type="text/javascript">
				function onLoad() {
					document.write("onLoad")
// 					updateTweets({"tweets":[{"tweetId":767428813294710784,"authorId":4224729994,"text":"American Blacks support Donald Trump! \nRT if you want to make liberals mad!\uD83D\uDE0F https://t.co/X2A7oqQlIK","place":"","favoriteCount":120,"retweetCount":131,"image":"http://pbs.twimg.com/media/CqRT6f8VMAAMV0P.jpg","createdAt":"2016-08-21 20:31:01","priority":2.16,"keywords":["trump"],"name":"Tennessee GOP","screenName":"TEN_GOP","followerCount":27034,"pictureUrl":"http://pbs.twimg.com/profile_images/673898503047917569/jAwPmxX1_normal.jpg","age":425129180}]});
					$.getJSON("<c:url value="/getTweets"/>", updateTweets);
				} onLoad();
				
// 				var interval = setInterval(updateTimer, 30000);
// 				function updateTimer() {
// 					onLoad();
// 				}
			</script>
	   </div>
	   
<!----------------------------------------------------------------------------------->
</body>
</html>
