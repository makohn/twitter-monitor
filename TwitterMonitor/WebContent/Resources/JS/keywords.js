var deleteCross ="background-image: url(Resources/Picture/Delete_Cross.png);";
var keyword_count = 0;
var currentPrio = [];
var keywordsfield = [];
var stars = [ ];

/*
 * @description 	This method renders the keywords received from
 * 					the KeywordController and displays a star rating
 * 					system, which can be adusted by the user to change
 * 					the priority of a keyword
 * 
 * @param {Object} 	data the JSON data object received from the controller
 * 
 * @author			Stefan Schloesser, Marek Kohn
 */

function updateKeywords(data)
	{
		keywordsfield = data.keywords;
		
		$("#Keywords_div").html("");
        
        //for each keyword
		for(var k=0;k<data.keywords.length;k++) {
			var keyword = data.keywords[k];	
			
			//do 			
			currentPrio[k] = keyword.priority;
			stars[k] = [ ];    
	        keyword_count++;
			
	        createPrioDiv(keyword,keyword_count,k);
		}
}

function createPrioDiv(keyword,count,k)
{
	stars[k] = [ ]; 
	var keyword_div = document.createElement("div");
    keyword_div.setAttribute("id",count);
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
    
     //create priority stars for this keyword	        
	 for (var i = 0; i < 5; i++) {
		  var prio_star = document.createElement("div");
		  prio_star.classList.add('prio_star');
		  prio_star.setAttribute('data-index', i);
		  if(i < keyword.priority) {
			  prio_star.classList.add('prio_star_filled');
		  }
		  prio_star.setAttribute("onClick","changePrio(".concat("\"").concat(keyword.keyword).concat("\"").concat(",").concat(i+1).concat(",").concat(k).concat("\)"));
		  stars[k].push(prio_star);
		  prio_div.appendChild(prio_star);   
		  attachStarEvents(prio_star, k);
	 }
	 //add listeners to each priority star
	 function attachStarEvents(star, k) {
	      starMouseOver(star, k);
	      starMouseOut(star, k);
	 }
   
	 //add a mouse over listener
	 function starMouseOver(star, k) {
	      star.addEventListener('mouseover', function(e) {
	        for (i = 0; i < stars[k].length; i++) {
	          if (i <= star.getAttribute('data-index')) {
	            stars[k][i].classList.add('prio_star_filled');
	          } else {
	            stars[k][i].classList.remove('prio_star_filled');
	          }
	        }
	      });
	  }
	 
	 //add mouse out listener
	 function starMouseOut(star, k) {
	      star.addEventListener('mouseout', function(e) {
			for (i = 0; i < stars[k].length; i++) {
				if (i < currentPrio[k]) {
					stars[k][i].classList.add('prio_star_filled');
				} else {
					stars[k][i].classList.remove('prio_star_filled');
				}
			}
	      });
	 }
 	        
    //create a delete_cross
    var delete_cross = document.createElement("div");
    delete_cross.setAttribute("class","delete_cross");
    delete_cross.setAttribute("style", deleteCross);
    delete_cross.setAttribute("onClick","deleteKeyword(".concat(keyword_count).concat("\)"));   
	keyword_div.appendChild(delete_cross);
}


/*
 * @description		This function removes a keyword from the view
 * @param			keyword The keyword that should be removed.
 */
function deleteKeyword(keyword){
	var keyword_name = keywordsfield[keyword-1].keyword;
	var keywordToDelete = {
		       "keyword" : keyword_name
		    }
		    $.ajax({
		       type: "POST",
		       contentType : 'application/json; charset=utf-8',
		       dataType : 'json',
		       url: "/TwitterMonitor/deleteKeyword",
		       data: JSON.stringify(keywordToDelete), 
		       success :function (result) {
		    	  
		       }
		   });
    var keyword_id = "#".concat(keyword);
    
    $(keyword_id).remove();
    
    keywordsfield = jQuery.grep(keywordsfield, function(value) {
//      return value != keywordsfield[keywordcount];
        return value != keywordsfield[keyword-1];	//!!!
    });
    keyword_count--;		//!!!
     
}

/*
 * @description		This function sends the updated priority
 * 					information as a response body to the keyword
 * 					Controller
 * @param			keywordName The unique identifier of the keyword
 * @param			prio The updated priority
 * @param			k an internal keyword id, for rendering the 
 * 					priority stars immediately
 */
function changePrio(keywordName,prio,k) {
	currentPrio[k] = prio;
	
    var keyword = {
       "keyword" : keywordName,
       "priority" : prio
    }
    $.ajax({
       type: "POST",
       contentType : 'application/json; charset=utf-8',
       dataType : 'json',
       url: "/TwitterMonitor/changePriority",
       data: JSON.stringify(keyword), 
       success :function (result) {
    	  if (isNewKeyWord(result.keyword))
    		  {
    	  setLastKeyword(result);
    		  }
       }
   });
   
 }

function setLastKeyword(result)
{
	createPrioDiv(result,keyword_count,currentPrio.length-1);
	keywordsfield.push(result);
	$('#newKeyword').css('display','block');
	$('#newKeyword_text').val("");
}


function createNewKeyword() {
	
	var newKey = $('#newKeyword_text').val();
	
	if (isNewKeyWord(newKey) && (newKey.trim() != "") && (keyword_count <=10))
		{
			keyword_count++;
			changePrio(newKey, 1, currentPrio.length);
			// Keyword-Textfeld ausblenden
			$('#newKeyword').css('display','none');
		}
	
}

function isNewKeyWord(newKey)
{
	for(var i=0;i<keywordsfield.length;i++) 
	{
		
		if (newKey == keywordsfield[i].keyword)
			{
				
			    return false;
			}
	}
	
	for(var i=0;i<blacklistfield.length;i++) 
	{		
		if (newKey == blacklistfield[i].keyword)
			{				
			    return false;
			}
	}
	
	return true;
}

//###############

var blacklist_count = 0;
var blacklistfield = [];
        
function updateBlacklist(data)
{

	// update blacklistfield
	blacklistfield = data.keywords;
	
	// empty blacklist
	$("#blacklist_div").html("");
    
    //for each keyword append panel
	for(var k=0;k<data.keywords.length;k++) {
		var keyword = data.keywords[k];	
		  
        blacklist_count++;
		
        createBlacklistDiv(keyword,blacklist_count);
	}
}

function createBlacklistDiv(keyword,count)
{
	var keyword_div = document.createElement("div");
    keyword_div.setAttribute("id","bl_"+count);
	keyword_div.setAttribute("class","keyword_div");
	$('#blacklist_div').append(keyword_div);
	
    //create a keyword label
    var keyword_label = document.createElement("div");
	 keyword_label.setAttribute("class","keyword_label");
     keyword_label.innerHTML=keyword.keyword;
	 keyword_div.appendChild(keyword_label);
 	        
    //create a delete_cross
    var delete_cross = document.createElement("div");
    delete_cross.setAttribute("class","delete_cross");
    delete_cross.setAttribute("style", deleteCross);
    delete_cross.setAttribute("onClick","deleteBlacklistItem(".concat(blacklist_count).concat("\)"));	// sollte hier und oben nicht einfach nur count stehen statt keyword_count   
	keyword_div.appendChild(delete_cross);
}

function deleteBlacklistItem(blacklistItem){
	
	var keyword_name = blacklistfield[blacklistItem-1].keyword;
	var keywordToDelete = {
		       "keyword" : keyword_name
		    }
		    $.ajax({
		       type: "POST",
		       contentType : 'application/json; charset=utf-8',
		       dataType : 'json',
		       url: "/TwitterMonitor/deleteKeyword",
		       data: JSON.stringify(keywordToDelete), 
		       success :function (result) {}
		   });
	
    var blacklist_id = "#bl_".concat(blacklistItem);
    
    $(blacklist_id).remove();
    
    blacklistfield = jQuery.grep(blacklistfield, function(value) {
      return value != blacklistfield[blacklistItem-1];
    });
    blacklist_count--;
     
}

function createNewBlacklistItem() {
	
	var newKey = $('#newBlacklistItem_text').val();
	
	if (isNewKeyword(newKey))
		{
			keyword_count++;
			updateBlacklistItem(newKey);
			// BlacklistItem-Textfeld ausblenden
			$('#newBlacklistItem').css('display','none');
		}	
}

function updateBlacklistItem(keywordName) {
	
	var prio = 1;
	
    var keyword = {
       "keyword" : keywordName,
       "priority" : prio,
       "positive" : false
    }
    $.ajax({
       type: "POST",
       contentType : 'application/json; charset=utf-8',
       dataType : 'json',
       url: "/TwitterMonitor/changePriority",
       data: JSON.stringify(keyword), 
       success : function (result) {
    	  if (isNewBlacklistItem(result.keyword))
    		  {
    		  	setLastBlacklistItem(result);
    		  }
       }
   });
   
 }

function setLastBlacklistItem(result)
{
	createBlacklistDiv(result,keyword_count);
	blacklistfield.push(result);
	$('#newBlacklistItem').css('display','block');
	$('#newBlacklistItem_text').val("");
}
		