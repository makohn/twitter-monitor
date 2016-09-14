var deleteCross ="background-image: url(Resources/Picture/Delete_Cross.png);";
var currentPrio = [];
var keywordsfield = [];
var stars = [ ];

function updateKeywords(data)
{
	deleteKeywordList();
	createKeywordList(data);
}

function deleteKeywordList()
{
	for(var i=0;i<keywordfield.length;i++) 
	{		
		 var keyword_id = "#key_".concat(i);
		 $(keyword_id).remove();
	}
}

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
function createKeywordList(data)
{
		keywordsfield = data.keywords;
		
		$("#Keywords_div").html("");
        
        //for each keyword
		for(var id=0; id<data.keywords.length; id++) {
		
			var keyword = data.keywords[id];	
			 			
			currentPrio[id] = keyword.priority;
			stars[id] = [ ];
			
	        createPrioDiv(keyword, id);
		}
}

function createPrioDiv(keyword, id)
{
//	stars[id] = [ ]; 
	var keyword_div = document.createElement("div");
    keyword_div.setAttribute("id", "key_"+id);
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
		  prio_star.setAttribute("onClick","changePrio(".concat("\"").concat(keyword.keyword).concat("\"").concat(",").concat(i+1).concat(",").concat(id).concat("\)"));
		  stars[id].push(prio_star);
		  prio_div.appendChild(prio_star);   
		  attachStarEvents(prio_star, id);
	 }
	 //add listeners to each priority star
	 function attachStarEvents(star, id) {
	      starMouseOver(star, id);
	      starMouseOut(star, id);
	 }
   
	 //add a mouse over listener
	 function starMouseOver(star, id) {
	      star.addEventListener('mouseover', function(e) {
	        for (i = 0; i < stars[id].length; i++) {
	          if (i <= star.getAttribute('data-index')) {
	            stars[id][i].classList.add('prio_star_filled');
	          } else {
	            stars[id][i].classList.remove('prio_star_filled');
	          }
	        }
	      });
	  }
	 
	 //add mouse out listener
	 function starMouseOut(star, id) {
	      star.addEventListener('mouseout', function(e) {
			for (i = 0; i < stars[id].length; i++) {
				if (i < currentPrio[id]) {
					stars[id][i].classList.add('prio_star_filled');
				} else {
					stars[id][i].classList.remove('prio_star_filled');
				}
			}
	      });
	 }
 	        
    //create a delete_cross
    var delete_cross = document.createElement("div");
    delete_cross.setAttribute("class","delete_cross");
    delete_cross.setAttribute("style", deleteCross);
    delete_cross.setAttribute("onClick","deleteKeyword(".concat(id).concat("\)"));   
	keyword_div.appendChild(delete_cross);
}


/*
 * @description		This function removes a keyword from the view
 * @param			keyword The keyword that should be removed.
 */
function deleteKeyword(keyword_index){
	
	var keyword_name = keywordsfield[keyword_index].keyword;
	
	// remove from DB
	var keywordToDelete = { "keyword" : keyword_name }
		    
		$.ajax({
		       type: "POST",
		       contentType : 'application/json; charset=utf-8',
		       dataType : 'json',
		       url: "/TwitterMonitor/deleteKeyword",
		       data: JSON.stringify(keywordToDelete), 
		       success :function (result) {}
			});
	
    // remove from array
    newkeywordsfield = jQuery.grep(keywordsfield, function(value) {
        return value != keywordsfield[keyword_index];
    });
     
    updateKeywords(newkeywordsfield);
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
function changePrio(keywordName,prio,id) {
	
	currentPrio[id] = prio;
	
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
    	  if (isNewKeyword(result.keyword))
    		  {
    		  	$.getJSON("/TwitterMonitor/getKeywords/", updateKeywords);
    		  }    	       	   
       }
   });  
}



//function setLastKeyword(result)
//{
//	createPrioDiv(result,keyword_count,currentPrio.length-1);
//	keywordsfield.push(result);
//	$('#newKeyword').css('display','block');
//	$('#newKeyword_text').val("");
//}


function createNewKeyword() {
	
	var newKey = $('#newKeyword_text').val();
	
	if (isNewKeyword(newKey) && (newKey.trim() != "") && (keywordfield.length < 10))
		{
			changePrio(newKey, 1, currentPrio.length);
			// Keyword-Textfeld ausblenden
			$('#newKeyword').css('display','none');
		}
	
	$('#newKeyword').css('display','block');
	$('#newKeyword_text').val("");
	
}

function isNewKeyword(newKey)
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
    delete_cross.setAttribute("onClick","deleteBlacklistItem(".concat(count).concat("\)"));	// sollte hier und oben nicht einfach nur count stehen statt keyword_count   
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
			blacklist_count++;
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
    	  if (isNewKeyword(result.keyword))
    		  {
    		  	setLastBlacklistItem(result);
    		  }
       }
   });
   
 }

function setLastBlacklistItem(result)
{
	createBlacklistDiv(result,blacklist_count);
	blacklistfield.push(result);
	$('#newBlacklistItem').css('display','block');
	$('#newBlacklistItem_text').val("");
}
		