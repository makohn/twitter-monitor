<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">





<html>

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Focus on</title>
        <link rel="stylesheet"  type="text/css" href="CSS/tweets.css">
        <link rel="stylesheet"  type="text/css" href="CSS/Animate.css">
        <link href='https://fonts.googleapis.com/css?family=Rubik' rel='stylesheet' type='text/css'>
        <script type="text/javascript" src="JS/jquery-2.1.4.js"></script>
        <script type="text/javascript" src="JS/slide.js"></script>
        

    </head>
    <body style="margin: 0 auto;">

        <header>
        <div id="logo">
        <img src="Picture/Logo.png" alt="logo">    
        </div>
        </header>
        <input type="button" id="side_bar_north_button" onclick="side_menu_north()"></input>
        <div id="side_bar_north">
          <div id="panel_1" >
        <div id="searchfield_label">Search</div>
        <input style="text" id="searchfield"> 
        <div id="seach_button">LOS</div>
              <div id="sort_label">Sort</div>
        <select id="sort_list" name="sort_list">
        <option value="Name">Name</option>  
        <option value="Zeit">Zeit</option>  
        
        </select>
        </div>
        <div id="panel_2">
         <div id="filter_icon">+</div>   
            <div id="filter_label">add Filter</div>
        </div>
        </div>
        <div id="page">
        
        <div id="sidebar_west">
            
            <div id="sidebar_west_button_pane">
                
                <div id="button_1" class="sidebar_west_button1">Home</div>
                <div id="button_2" class="sidebar_west_button1_actually">Tweets</div>
                <div id="button_3" class="sidebar_west_button1">Kategorien</div>
                <div id="button_4" class="sidebar_west_button2">Archiv</div>
                <div id="button_5" class="sidebar_west_button2">Profil</div>
                <div id="button_6" class="sidebar_west_button2">Einstellungen</div>
                
            </div>
            <div id="change_sidebar_west_button" ></div>
            </div>
         <div id="tweets">
         	
     		 <c:forEach var="row" items="${tweets}">
				<div class="tweet">       
       			 <div class="tweet_text">  
          <c:out value="${row.getText()}" />
        </div>
    </div>     
     
      </c:forEach>
        </div>
        </div>

        <footer>
        </footer>
    </body>
</html> 