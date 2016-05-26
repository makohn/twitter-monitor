
function getPic(url)
{
	cssurl = "\'url(".concat(url).concat(")\'");
	if(url.length >0)
		{
		 $('#tweet_pic').css(' background-image',cssurl.toString());
		}
	else{
		 $('#tweet_pic').css('visibility','hidden');
	}
}