var keyword_count = 0;
var yellowStar = "background-image: url(Resources/Picture/Star_Yellow.png);";
var greyStar = "background-image: url(Resources/Picture/Star_Grey.png);";
var deleteCross = "background-image: url(Resources/Picture/Delete_Cross.png);";

function updateKeywords(data)
	{
	

		$("#Keywords_div").html("");
        
        //for each keyword
        
		for(var i=0;i<data.keywords.length;i++){
		var keyword = data.keywords[i];	
		
        //do 
            
        keyword_count++;
		//create a keyword container 
            
		var keyword_div = document.createElement("div");
        keyword_div.setAttribute("id",keyword_count);
		keyword_div.setAttribute("class","keyword_div");
		$('#keywords_div').append(keyword_div);
		
        //create a keyword label
        
        var keyword_label = document.createElement("div");
		 keyword_label.setAttribute("class","keyword_label");
         keyword_label.innerHTML=keyword.keyword;
		 keyword_div.appendChild(keyword_label);
		
         //create a keyword priority div
        
        var prio_div = document.createElement("div");
		 prio_div.setAttribute("class","prio_div");
		 keyword_div.appendChild(prio_div);    
        
        //create a keyword prioritystar 1
        
        var prio_star1 = document.createElement("div");
		 prio_star1.setAttribute("class","prio_star1");
         if (keyword.prio > 0)
             {
                 prio_star1.setAttribute("style",yellowStar);
             }
            else{
                 prio_star1.setAttribute("style",greyStar);
            }
		  prio_div.appendChild(prio_star1);    
         
        //create a keyword prioritystar 2
        
        var prio_star2 = document.createElement("div");
		 prio_star2.setAttribute("class","prio_star2");
         if (keyword.prio > 1)
             {
                 prio_star2.setAttribute("style",yellowStar);
             }
            else{
                 prio_star2.setAttribute("style",greyStar);
            }
		  prio_div.appendChild(prio_star2);
            
        //create a keyword prioritystar 3
        
        var prio_star3 = document.createElement("div");
		 prio_star3.setAttribute("class","prio_star3");
         if (keyword.prio > 2)
             {
                 prio_star3.setAttribute("style",yellowStar);
             }
            else{
                 prio_star3.setAttribute("style",greyStar);
            }
		  prio_div.appendChild(prio_star3);
            
        //create a keyword prioritystar 4
        
        var prio_star4 = document.createElement("div");
		 prio_star4.setAttribute("class","prio_star4");
         if (keyword.prio > 3)
             {
                 prio_star4.setAttribute("style",yellowStar);
             }
            else{
                 prio_star4.setAttribute("style",greyStar);
            }
		  prio_div.appendChild(prio_star4);
            
        //create a keyword prioritystar 5
        
        var prio_star5 = document.createElement("div");
		 prio_star5.setAttribute("class","prio_star5");
         if (keyword.prio > 4)
             {
                 prio_star5.setAttribute("style",yellowStar);
             }
            else{
                 prio_star5.setAttribute("style",greyStar);
            }
		  prio_div.appendChild(prio_star5);
            
        
        
        
        //create a delete_cross
        var delete_part1
        var delete_cross = document.createElement("div");
		delete_cross.setAttribute("class","delete_cross");
		delete_cross.setAttribute("style",deleteCross);
        delete_cross.setAttribute("onClick","deleteKeyword(".concat(keyword_count).concat("\)"));
            
		  keyword_div.appendChild(delete_cross);
            
        
		}
	}

function deleteKeyword(keyword){
    var keyword_id = "#".concat(keyword);
    $(keyword_id).remove();
}
        
        
        
		