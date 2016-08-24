DELIMITER $$
drop function if exists get_tweet_html;
create function get_tweet_html(
	p_tweet_text text, p_author_name varchar(15), p_author_picture_url varchar(100)
) returns text
begin
	declare l_body text default '';
  	
  	set l_body = concat('
	  <div style="
	  		margin-top: 25px; 
			clear: left;
			position: relative;
			float: left;
			display: inline-block;
			border-top-left-radius: 20px;
			border-top-right-radius: 20px;
			min-width: 500px;
			max-width: 500px;">
			<div style="
				border-top-right-radius: 0px;
				border-top-left-radius: 0px;
				float: left;
				background-color: #f9f9f9;
				color: #4266b2;
				padding-top: 20px;
				padding-left: 20px;
				min-height: 40px;
				min-width: 510px;
				box-shadow: 10px 12px 12px 0px rgba(0, 0, 0, 0.50);
				border-top-left-radius: 28px;
				border-top-right-radius: 28px;">
			<div style="
					background-image:url(', p_author_picture_url, ');
					float: left;
					background-size: contain;
					background-repeat: no-repeat;
					min-width: 30px;
					min-height: 30px;
					padding-top: 0px;
					padding-bottom: 3px;
					padding-left: 10px;"></div>
			<div style="
				float: left;
				color: black;
				font-weight: bold;
				margin-left: 5px;
				padding-top: 10px;
				min-height: 40px;">', p_author_name, '</div>
			<div style="
				float: right;
				color: inherit;
				margin-right: 15px;
				padding-top: 10px;
				padding-left: 10px;
				min-height: 40px;">2016-08-24 09:07:50</div>
		</div>	
		<div style="
			clear: left;
			font-size: 16px;
			float: left;
			min-width: 500px;
			max-width: 500px;
			min-height: 50px;
			line-height: 25px;
			background-color: #f9f9f9;
			padding-top: 0px;
		   padding-left: 20px;
		   padding-right: 10px;
		   padding-bottom: 10px;
			box-shadow: 10px 10px 12px 0px rgba(0, 0, 0, 0.50);
			border-bottom-left-radius: 28px;">', p_tweet_text, '
		</div>
	</div>');
  	return l_body;
end;$$

DELIMITER $$
drop function if exists get_top_tweets;
create function get_top_tweets(
	p_username varchar(60)
) returns text
begin
	/*
		Ermittelt die Top-Tweets für den User aus den letzten 12 Stunden
		und gibt sie als HTML-Text zurueck
	*/
	declare l_personal_prio float;
	declare l_tweet_text varchar(200);
	declare l_author_name varchar(15);
	declare l_author_picture_url varchar(100);
	declare l_body text default '';
  	declare done int default 0;
	declare cur cursor for 
		select get_personal_prio(t.tweetId, p_username) personal_prio, t.text, a.name, a.pictureUrl
		   from tweets t, tweets_x_keywords x, keywords k, tweetauthors a
		   where t.tweetId = x.tweetId
			   and x.keyword = k.keyword
			   and t.authorId = a.authorId
			   and k.username = p_username
			   and date_add(t.createdAt, interval 720 minute) > sysdate()
		   order by personal_prio desc
			limit 5;
	declare continue handler for not found set done = 1;

  	open cur;
		repeat
			fetch cur into l_personal_prio, l_tweet_text, l_author_name, l_author_picture_url;
			if not done then
				set l_body = concat(l_body, get_tweet_html(l_tweet_text, l_author_name, l_author_picture_url));
			end if;
		until done end repeat;
	close cur;
	
	if l_body = '' then
		set l_body = 'Es wurden keine Tweets für dich aus den letzten 12 Stunden gefunden';
	end if;
		
	return l_body;
end;$$

DELIMITER $$
drop procedure if exists notify_users;
create procedure notify_users()
begin
	# Legt Benachrichtigungen an mit Top-Tweets für jeden User aus den letzten 12 Stunden
   declare l_subject varchar(60);
   declare l_body text;
   declare l_username varchar(60);
   
   declare done int default 0;
	declare cur cursor for 
		select username from users where enabled = 1;
	declare continue handler for not found set done = 1;
	
	set l_subject = 'Top-Tweets der letzten 12 Stunden';
	
	open cur;
		repeat
			fetch cur into l_username;
			if not done then
				set l_body = get_top_tweets(l_username);
				insert into notifications (username, type, subject, body)
					values (l_username, 'ema', l_subject, l_body);
			end if;
		until done end repeat;
	close cur;
end;$$
