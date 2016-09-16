var deleteCross ="background-image: url(Resources/Picture/Delete_Cross.png);";

var keywords_field = [];
var stars = [];

var blacklist_field = [];

function loadKeywords(data) {
	keywords = data.keywords;
	updateKeywords(keywords);
}

function updateKeywords(keywords)
{
	deleteKeywordList();
	createKeywordList(keywords);
}

function deleteKeywordList()
{
	for(var i=0; i<keywords_field.length; i++) 
	{		
		 var keyword_id = "#key_"+i;
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
function createKeywordList(keywords)
{
		keywords_field = keywords;
		
		$("#keywords_div").html("");
        
        //for each keyword
		for(var id=0; id<keywords.length; id++) {			
			$('#keywords_div').append(createPrioDiv(id));
		}
}

function createPrioDiv(key_id)
{	
	// keyword is the actual keyword-object
	// id is the index of the keyword and its stars
	// id is used for the element-id, e.g. id=1 => "key_1"

	// create a star-array for the keyword
	stars[key_id] = [];
	
	var keyword_div = document.createElement("div");
    keyword_div.setAttribute("id", "key_"+key_id);
	keyword_div.setAttribute("class","keyword_div");
	
	// append a keyword label
	keyword_div.appendChild(createKeywordLabel(key_id));
	
	// append a keyword priority div
	keyword_div.appendChild(createPriorityDiv(key_id));
	
    // append a delete cross
	keyword_div.appendChild(createDeleteCross(key_id));
 	        
	return keyword_div;
}

function createKeywordLabel(key_id) {
	
	var keyword_label = document.createElement("div");
	keyword_label.setAttribute("class","keyword_label");
    keyword_label.innerHTML = keywords_field[key_id].keyword;
	
    return keyword_label;
}

function createPriorityDiv(key_id) {
	
	var prio_div = document.createElement("div");
	prio_div.setAttribute("class","prio_div");
	
	// append 5 priority stars for this keyword	        
	for (var i = 0; i < 5; i++) {	
		var prio_star = createPriorityStar(i, key_id);
		stars[key_id].push(prio_star);
		prio_div.appendChild(prio_star);	
	}	 
	
	return prio_div;
}

function createPriorityStar(starIndex, key_id) {
	
	var prio_star = document.createElement("div");
	prio_star.setAttribute('data-index', starIndex);
	
	prio_star.classList.add('prio_star');	
	if(starIndex < keywords_field[key_id].priority) {
		prio_star.classList.add('prio_star_filled');
	}
	
//	prio_star.setAttribute("onClick","changePrio(" + (starIndex+1) + "," + key_id + "\)");  
	attachStarEvents(prio_star, key_id);
	  
	return prio_star;
	
}

//add listeners to each priority star
// star is star-index
// id is keyword-index
function attachStarEvents(star, id) {
	
	starClick(star, id);
    starMouseOver(star, id);
    starMouseOut(star, id);
}

function starClick(star, id) {
	star.addEventListener('click', function() {
		var prio = parseInt(star.getAttribute('data-index'))+1;
		changePrio(prio, id);
	})
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
			if (i < keywords_field[id].priority) {
				stars[id][i].classList.add('prio_star_filled');
			} else {
				stars[id][i].classList.remove('prio_star_filled');
			}
		}
     });
}

function pauseKeyword(key_id) {
	keywords_field[key_id].active == false;
	
}

function createDeleteCross(key_id) {
	
    var delete_cross = document.createElement("div");
    delete_cross.setAttribute("class","delete_cross");
    delete_cross.setAttribute("style", deleteCross);
    delete_cross.setAttribute("onClick","deleteKeyword("+key_id+"\)");   
    
    return delete_cross;
}

/*
 * @description		This function removes a keyword from the view
 * @param			keyword The keyword that should be removed.
 */
function deleteKeyword(key_id){
	
	var keyword_name = keywords_field[key_id].keyword;
	
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

    // remove from array [sollte eigentlich nur im Erfolgsfall gemacht werden !!!]
    keywords_field = jQuery.grep(keywords_field, function(value) {
        return value != keywords_field[key_id];
    });     
    updateKeywords(keywords_field);
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
function changePrio(prio, key_id) {
	
	keywords_field[key_id].priority = prio;
	keywordName = keywords_field[key_id].keyword;
	
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
       success :function (result) {}
   }); 
    
    updateKeywords(keywords_field);
    
}


function createNewKeyword() {
	
	var newKey = $('#newKeyword_text').val();
	
	if (isNewKeyword(newKey) && (newKey.trim() != "") && (keywords_field.length < 10) && (newKey.length < 20))
		{
			// Keyword-Textfeld ausblenden
			$('#newKeyword').css('display','none');
		
			var keyword = {
					"keyword" : newKey,
					"priority" : 1,
					"active" : true
			}
			
			$.ajax({
			       type: "POST",
			       contentType : 'application/json; charset=utf-8',
			       dataType : 'json',
			       url: "/TwitterMonitor/changePriority",
			       data: JSON.stringify(keyword), 
			       success :function (result) {}
			   }); 
			
			keywords_field.push(keyword);
			updateKeywords(keywords_field);
		}
	
	$('#newKeyword_text').val("");
	$('#newKeyword').css('display','block');	
}

function isNewKeyword(newKey)
{	
	for(var i=0;i<keywords_field.length;i++)	{		
		if (newKey == keywords_field[i].keyword) {				
			    return false;
		}
	}	
	
	for(var i=0;i<blacklist_field.length;i++) {		
		if (newKey == blacklist_field[i].keyword) {				
			    return false;
			}
	}
	
	return true;
}

//###############

function loadBlacklist(data) {
	blacklist = data.keywords;
	updateBlacklist(blacklist);
}

function updateBlacklist(blacklist)
{
	deleteBlacklist();
	createBlacklist(blacklist);
}

function deleteBlacklist()
{
	for(var i=0; i<blacklist_field.length; i++) 
	{		
		 var blacklist_id = "#bl_".concat(i);
		 $(blacklist_id).remove();
	}
}
        
function createBlacklist(blacklist)
{
	// update blacklistfield
	blacklist_field = blacklist;
	
	// empty blacklist
	$("#blacklist_div").html("");
    
    //for each keyword append panel
	for(var id=0;id<blacklist.length;id++) {
        $('#blacklist_div').append(createBlacklistDiv(id));
    }
}

function createBlacklistDiv(bl_id)
{
	var keyword_div = document.createElement("div");
    keyword_div.setAttribute("id","bl_"+bl_id);
	keyword_div.setAttribute("class","keyword_div");
	
	// append a keyword label
	keyword_div.appendChild(createBlacklistLabel(bl_id));
    
    // append a delete cross
	keyword_div.appendChild(createBLDeleteCross(bl_id));
	
	return keyword_div;
}

function createBlacklistLabel(bl_id) {
	
	var blacklist_label = document.createElement("div");
	blacklist_label.setAttribute("class","keyword_label");
	blacklist_label.innerHTML = blacklist_field[bl_id].keyword;
	
    return blacklist_label;
}

function createBLDeleteCross(bl_id) {
	
    var delete_cross = document.createElement("div");
    delete_cross.setAttribute("class","delete_cross");
    delete_cross.setAttribute("style", deleteCross);
    delete_cross.setAttribute("onClick","deleteBlacklistItem("+bl_id+"\)");   
    
    return delete_cross;
}

function deleteBlacklistItem(bl_id){
	
	var keyword_name = blacklist_field[bl_id].keyword;
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
	
//	$.getJSON("/TwitterMonitor/getNegKeywords/", updateBlacklist);
	
	// remove from array [sollte eigentlich nur im Erfolgsfall gemacht werden !!!]
	blacklist_field = jQuery.grep(blacklist_field, function(value) {
        return value != blacklist_field[bl_id];
    });     
    updateBlacklist(blacklist_field);
     
}

function createNewBlacklistItem() {
	
	var newKey = $('#newBlacklistItem_text').val();
	
	if (isNewKeyword(newKey) && (newKey.trim() != ""))
		{
			// BlacklistItem-Textfeld ausblenden
			$('#newBlacklistItem').css('display','none');
		
			updateBlacklistItem(newKey);
		}	
	
	$('#newBlacklistItem').css('display','block');
	$('#newBlacklistItem_text').val("");
}

function updateBlacklistItem(keywordName) {
	
    var keyword = {
       "keyword" : keywordName,
       "priority" : 1,
       "positive" : false
    }
    $.ajax({
       type: "POST",
       contentType : 'application/json; charset=utf-8',
       dataType : 'json',
       url: "/TwitterMonitor/changePriority",
       data: JSON.stringify(keyword), 
       success : function (result) {}
    });
   
    blacklist_field.push(keyword);
    updateBlacklist(blacklist_field);
    
 }

//function setLastBlacklistItem(result)
//{
//	createBlacklistDiv(result,blacklist_count);
//	blacklistfield.push(result);
//	$('#newBlacklistItem').css('display','block');
//	$('#newBlacklistItem_text').val("");
//}
		