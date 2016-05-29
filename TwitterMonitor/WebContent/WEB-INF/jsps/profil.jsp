<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Focus on</title>

<link rel="stylesheet" type="text/css" href="Resources/CSS/profil.css">

<script type="text/javascript" src="jquery-2.1.4.js"></script>



</head>
<body style="margin: 0 auto;">

	<div id="header"></div>

	<nav>
	<div id="logo"></div>

	<ul>
		<li><a href="home.php">Home</a></li>
		<li><a href="${pageContext.request.contextPath}/tweets_v3">Tweets</a></li>
		<li><a href="#">Kategorien</a></li>
		<li><a href="#">Profil</a></li>
		<li><a href="#">Einstellungen</a></li>
	</ul>
	</nav>
	<div id="page">
		<div id="form">
			<sf:form method="post" action="${pageContext.request.contextPath}/setProfile" commandName="keyword">

				<table>
					<tr>
						<td>Keyword:</td>
						<td><sf:input path="keyword" name="keyword" type="text" /></td>
					</tr>
					
					<tr>
						<td></td>
						<td><input value="Add" type="submit"  /></td>
					</tr>
				</table>

			</sf:form>
		</div>
		<div id="sidebar">

			<div id="info_text">Füge Keywörter hinzu, um die Filterung der
				Tweets zu starten.</div>



		</div>
	</div>

</body>
</html>
