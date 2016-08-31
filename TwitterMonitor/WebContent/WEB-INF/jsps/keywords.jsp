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
	
	<script type="text/javascript" src="Resources/JS/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="Resources/JS/keywords.js"></script>
	
	<title>Focus on</title>
</head>

<body style="margin: 0 auto;">
<!-------------------------- HEADER ------------------------------------------------->
<div id="header"></div>

	<nav>
		<div id="logo"></div>
		<ul>
			
			<li><a href="${pageContext.request.contextPath}/showTweets">Tweets</a></li>
			
			<li><a href="${pageContext.request.contextPath}/profil">Profil</a></li>
			<li><a href="#">Einstellungen</a></li>
		</ul>
	</nav>
	
<!-------------------------- BODY --------------------------------------------------->
<div id="page">
	
		<div id="sidebar"></div>
		
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
<!----------------------------------------------------------------------------------->		
</body>
</html>
