<!-- IMPORTS -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>

<html>


<!------------------------- META TAGS ---------------------------------------------->
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="stylesheet" type="text/css" href="Resources/CSS/keywords.css">
	<link rel="stylesheet" type="text/css" href="Resources/CSS/header.css">
	
	<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="Resources/JS/keywords.js"></script>
	<script type="text/javascript" src="Resources/JS/header.js"></script>
	
	<title>TwitterMonitor - Keywords</title>
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
<div id="switch"></div>
	<div id="page">
	<div id="title_wrapper">
		<div id="title_keyword">Keywords</div>
		<div id="title_blacklist">Blacklist</div>
	</div>
	<!--############# POSITIVE KEYWORDS ####################-->
		<div id="keyword_wrapper">
		<!--############# Load Keywords ####################-->
		<div id="keywords_div"></div>
			<script type="text/javascript">
				function onLoad() {
					$.getJSON("<c:url value="/getKeywords"/>", updateKeywords);
				} onLoad();
			</script>
			
		<!--############# Display Keyword Textfield ########-->	
		
		<div id="newKeyword">
			<input type="text"  id="newKeyword_text">
			<div id="newKeyword_button" onclick="createNewKeyword()" ></div>
		</div>
		</div>
		
		
		
	<!--############# BLACKLIST ####################-->
	<div id="blacklist_wrapper">
		<!--############# Load Keywords ####################-->
		<div id="blacklist_div"></div>
			<script type="text/javascript">
				function loadBlacklist() {
					$.getJSON("<c:url value="/getNegKeywords"/>", updateBlacklist);
				} loadBlacklist();
			</script>
			
		<!--############# Display Keyword Textfield ########-->	
		<div id="newBlacklistItem">
			<input type="text"  id="newBlacklistItem_text">
			<div id="newBlacklistItem_button" onclick="createNewBlacklistItem()" ></div>
		</div>
		</div>
</div>
<!----------------------------------------------------------------------------------->		
</body>
</html>
