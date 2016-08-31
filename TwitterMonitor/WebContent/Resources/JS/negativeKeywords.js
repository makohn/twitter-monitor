var deleteCross ="background-image: url(Resources/Picture/Delete_Cross.png);";
var keyword_count = 0;
var keywordsfield = [];

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
			  
	        keyword_count++;
			
	        createPrioDiv(keyword,keyword_count);
		}
}

function createPrioDiv(keyword,count)
{
	var keyword_div = document.createElement("div");
    keyword_div.setAttribute("id",count);
	keyword_div.setAttribute("class","keyword_div");
	$('#keywords_div').append(keyword_div);
	
    //create a keyword label
    var keyword_label = document.createElement("div");
	 keyword_label.setAttribute("class","keyword_label");
     keyword_label.innerHTML=keyword.keyword;
	 keyword_div.appendChild(keyword_label);
 	        
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
		       success :function (result) {}
		   });
    var keyword_id = "#".concat(keyword);
    
    $(keyword_id).remove();
    
    keywordsfield = jQuery.grep(keywordsfield, function(value) {
      return value != keywordsfield[keywordcount];
    });
     
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
function changePrio(keywordName,prio) {
	
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
    	  if (isNewKeyWord(result.keyword))
    		  {
    		  	setLastKeyword(result);
    		  }
       }
   });
   
 }

function setLastKeyword(result)
{
	createPrioDiv(result,keyword_count);
	keywordsfield.push(result);
	$('#newKeyword').css('display','block');
	$('#newKeyword_text').val("");
}


function createNewKeyword() {
	
	var newKey = $('#newKeyword_text').val();
	
	if (isNewKeyWord(newKey))
		{
			keyword_count++;
			changePrio(newKey, 1);
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
	return true;
}