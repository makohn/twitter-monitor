

function updateTweets(data)
	{
	

		$("#tweet_panel").html("");
		for(var i=0;i<data.tweets.length;i++){
		var tweet = data.tweets[i];	
		
		//create a tweet_panel div 
		var tweet_div = document.createElement("div");
		tweet_div.setAttribute("class","tweet_panel");
		$("#tweet_panel").append(tweet_div);
		
		//create a tweet_line_div 
		var line = document.createElement("div");
		line.setAttribute("class","line");
		tweet_div.appendChild(line);
		
		//create a tweet_pic_div 
		
		
		if (tweet.urls.length !=0)
		{	
			var pics = tweet.urls;	
		
			var pic_div = document.createElement("div");
			pic_div.setAttribute("class","tweet_pic ");
			pic_div.setAttribute("style",'display:block;background-image:url('+pics[0]+')');
			tweet_div.appendChild(pic_div);
		}
		
		
		
		//create a tweet_time_div and passing Tweet_time from JSON
		var tweet_time = document.createElement("div");
		tweet_time.setAttribute("class","tweet_time");
		
		tweet_div.appendChild(tweet_time);
		
		//create a tweet_text_div and passing Tweet_text from JSON
		var tweet_text = document.createElement("div");
		tweet_text.setAttribute("class","tweet_text");
		tweet_text.innerHTML=tweet.text;
		tweet_div.appendChild(tweet_text);
		}
		
		
		
	}
	function slidePic(element)
	{
		parent =element.getParentNode();
		parent.setAttribute("style",'display:block;background-image:url('+pics[0]+')');
	}