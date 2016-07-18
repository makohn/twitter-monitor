
var count=0;
var first=true;
var tweetsfield;
var searchfield;
function updateTweets(data)
	{
	 
		if (first)
		{
	     
	     tweetsfield = $.extend(true, [], data);
	     searchfield = $.extend(true, [], data);
	     first = false;
		}else{
			$("#tweet_panel").remove();
			var tweet_panel = document.createElement("div");
			tweet_panel.setAttribute("id","tweet_panel");
			$("#page").append(tweet_panel);
		}
		
		$("#tweet_panel").html("");
		for(var i=0;i<data.tweets.length;i++){
		var tweet = data.tweets[i];	
		
		//create a tweet_panel div 
		var tweet_div = document.createElement("div");
		tweet_div.setAttribute("class","tweet_panel");
		tweet_div.setAttribute("id","tweet".concat(count));
		$("#tweet_panel").append(tweet_div);
		
		//create a tweet_line_div 
		if(i != 0){
		var line = document.createElement("div");
		line.setAttribute("class","line");
		tweet_div.appendChild(line);
		}else{
			tweet_div.setAttribute("style",'margin-top:200px;');
		}
		//create a tweet_pic_div 
		if (tweet.urls.length !=0)
		{	
			var pics = tweet.urls;	
		
			var pic_div = document.createElement("div");
			pic_div.setAttribute("class","tweet_pic ");
			pic_div.setAttribute("id","pic".concat(count));
			pic_div.setAttribute("onClick","slidePic(".concat('\"').concat("pic").concat(count).concat('\"').concat("\)"));
			pic_div.setAttribute("style",'display:block;background-image:url('+pics[0]+')');
			tweet_div.appendChild(pic_div);
			
		}
		
		//create a tweet_data_div 
		var tweet_data = document.createElement("div");
		tweet_data.setAttribute("class","tweet_data");
		tweet_div.appendChild(tweet_data);
		if (tweet.urls.length !=0)
		{	
			tweet_data.setAttribute("style",'border-top-right-radius: 0px;border-top-left-radius: 0px;');
		}
		
		//create a tweet_author-pic_div and passing author_div from JSON
		var tweet_author_pic = document.createElement("div");
		tweet_author_pic.setAttribute("class","tweet_author_pic");
		tweet_author_pic.setAttribute("style",'background-image:url('+tweet.pictureUrl+')');
		tweet_data.appendChild(tweet_author_pic);
		
		//create a tweet_author-name_div and passing author_div from JSON
		var tweet_author_name = document.createElement("div");
		tweet_author_name.setAttribute("class","tweet_author_name");
		tweet_author_name.innerHTML=tweet.name;
		tweet_data.appendChild(tweet_author_name);
		
		if (tweet.place != "")
		{
		//create a tweet_place_div and passing author_div from JSON
		var tweet_place = document.createElement("div");
		tweet_place.setAttribute("class","tweet_place");
		tweet_place.innerHTML="@".concat(tweet.place);
		tweet_author_name.appendChild(tweet_place);
		}
		
		//create a tweet_time_div and passing Tweet_time from JSON
		var tweet_time = document.createElement("div");
		tweet_time.setAttribute("class","tweet_time");
		tweet_time.innerHTML=tweet.createdAt;
		tweet_data.appendChild(tweet_time);
	
		
		
		//create a tweet_text_div and passing Tweet_text from JSON
		var tweet_text = document.createElement("div");
		tweet_text.setAttribute("class","tweet_text");
		tweet_text.innerHTML=tweet.text;
		tweet_div.appendChild(tweet_text);
		count++;
		}
		
		
		
	}
	
	function slidePic(div_id)
	{	
		
		var id = String(div_id).substring(3);
		
		var pics = getPicsUrls(id);
		
		var oldpic;
		for(var i=0;i<pics.length;i++){
			
			if(pics[i] == getPicUrl(div_id))
				{
					
					if(i+1 < pics.length)
					{
						document.getElementById(div_id).setAttribute("style",'display:block;background-image:url('+pics[i+1]+')');
						return;
					}else if(i+1 == pics.length)
						{
						document.getElementById(div_id).setAttribute("style",'display:block;background-image:url('+pics[0]+')');
						return;
						}
				}
			
		}
		
	}
	
	function getPicsUrls(pic_id)
	{
		 
		 return  tweetsfield.tweets[pic_id].urls;
		 
	}
	
	function getPicUrl(pic_id)
	{
		
		var img = document.getElementById(pic_id);
		var style = img.currentStyle || window.getComputedStyle(img, false);
		
		return style.backgroundImage.slice(4, -1).replace(/"/g, "");
		
	}
	
	function search()
	{
		searchfield = $.extend(true, [], tweetsfield);
		var keyString = $('#search').val();
		if(!(keyString == "")){
			
		for(var i=0;i<searchfield.tweets.length;i++)
		{
			var tweet = searchfield.tweets[i];
			var index = [];
			if (!(tweet.text.includes(keyString)))
			{
				
				searchfield.tweets.splice(i,1);
				i=0;
			}
			
		}
		
		updateTweets(searchfield);
	
		}else 
			{
			
			updateTweets(tweetsfield);
			}
		
		
		
		
	}