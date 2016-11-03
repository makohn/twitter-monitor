-- Datenbank Struktur für twitter-monitor
CREATE DATABASE IF NOT EXISTS `twitter_monitor` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `twitter_monitor`;

-- Struktur von Tabelle twitter-monitor.users
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(60) NOT NULL,
  `password` varchar(80) NOT NULL,
  `enabled` tinyint default 1 NOT NULL,
  `email` varchar(60) NOT NULL,
  `registeredAt` datetime default current_timestamp,
  PRIMARY KEY (`username`)
);

CREATE TABLE IF NOT EXISTS `authorities` (
  `username` varchar(60) NOT NULL,
  `authority` varchar(60) NOT NULL,
  PRIMARY KEY (`username`, `authority`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

-- Struktur von Tabelle twitter-monitor.notificationType
CREATE TABLE IF NOT EXISTS `notificationType` (
  `type` char(3) NOT NULL,
  `descr` varchar(60) NOT NULL,
  PRIMARY KEY (`type`)
);

-- Struktur von Tabelle twitter-monitor.notifications
CREATE TABLE IF NOT EXISTS `notifications` (
  `notificationId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(60) NOT NULL,
  `type` char(3) NOT NULL,
  `subject` varchar(60) NOT NULL,
  `body` text NOT NULL,
  `createdAt` datetime default current_timestamp,
  `sentAt` datetime,
  PRIMARY KEY (`notificationId`),
  FOREIGN KEY (`type`) REFERENCES `notificationType` (`type`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

-- Struktur von Tabelle twitter-monitor.preferences
CREATE TABLE IF NOT EXISTS `preferences` (
  `preferenceType` char(3) NOT NULL,
  `descr` varchar(60) NOT NULL,
  `defaultValue` int(11) NOT NULL,
  PRIMARY KEY (`preferenceType`)
);

-- Struktur von Tabelle twitter-monitor.user_x_preferences
CREATE TABLE IF NOT EXISTS `user_x_preferences` (
  `username` varchar(60) NOT NULL,
  `preferenceType` char(3) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`username`,`preferenceType`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  FOREIGN KEY (`preferenceType`) REFERENCES `preferences` (`preferenceType`)
);

-- Struktur von Tabelle twitter-monitor.keywords
CREATE TABLE IF NOT EXISTS `keywords` (
  `keyword` varchar(50) NOT NULL,
  `username` varchar(60) NOT NULL,
  `priority` int(1) DEFAULT 1 NOT NULL,
  `active` int(1) DEFAULT 1 NOT NULL,
  `createdAt` datetime default current_timestamp,
  PRIMARY KEY (`keyword`,`username`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

-- Struktur von Tabelle twitter-monitor.tweetAuthors
CREATE TABLE IF NOT EXISTS `tweetAuthors` (
  `authorId` bigint NOT NULL,
  `name` varchar(20) NOT NULL,
  `screenName` varchar(20),
  `followerCount` int(11),
  `pictureUrl` varchar(100),
  PRIMARY KEY (`authorId`)
);

-- Struktur von Tabelle twitter-monitor.tweets
CREATE TABLE IF NOT EXISTS `tweets` (
  `tweetId` bigint NOT NULL,
  `authorId` bigint NOT NULL,
  `text` varchar(200) NOT NULL,
  `favoriteCount` int(11) NOT NULL,
  `retweetCount` int(11) NOT NULL,
  `place` varchar(60),
  `image` varchar(100),
  `createdAt` datetime NOT NULL,
  `lastUpdate` datetime default current_timestamp,
  PRIMARY KEY (`tweetId`),
  FOREIGN KEY (`authorId`) REFERENCES `tweetAuthors` (`authorId`)
);

-- Struktur von Tabelle twitter-monitor.tweets_x_keywords
CREATE TABLE IF NOT EXISTS `tweets_x_keywords` (
  `tweetId` bigint NOT NULL,
  `keyword` varchar(50) NOT NULL,
  PRIMARY KEY (`tweetId`, `keyword`),
  FOREIGN KEY (`keyword`) REFERENCES `keywords` (`keyword`) ON DELETE CASCADE,
  FOREIGN KEY (`tweetId`) REFERENCES `tweets` (`tweetId`) ON DELETE CASCADE
);

INSERT INTO `notificationType` (`type`, `descr`) VALUES ('ema', 'E-Mail');
INSERT INTO `preferences` (`preferenceType`, `descr`, `defaultValue`) VALUES ('not', 'Schalter um Benutzer über Top-Tweets zu informieren', 0);

create user 'twitteruser' identified by 'Pikachu#25';
create user 'twitteradmin' identified by 'RoschdwurschdBud4Ever';
grant select on twitter_monitor.* to 'twitteruser';
grant update, insert on twitter_monitor.users to 'twitteruser';
grant update, insert on twitter_monitor.authorities to 'twitteruser';
grant update, insert on twitter_monitor.user_x_preferences to 'twitteruser';
grant update, insert, delete on twitter_monitor.keywords to 'twitteruser';
grant update, insert on twitter_monitor.tweetAuthors to 'twitteruser';
grant update, insert on twitter_monitor.tweets to 'twitteruser';
grant execute on twitter_monitor.* to 'twitteruser';
grant all privileges on twitter_monitor.* to 'twitteradmin';

DELIMITER $$
drop function if exists calc_tweet_prio;
create function calc_tweet_prio(
	p_favoriteCount int, p_retweetCount int, p_followerCount int
) returns float
begin
	/*
		Errechnet die Priorität für einen Tweet anhand dessen Retweets, Likes und Anzahl Follower
		Implementiert die Formel zur Priorisieren von Tweets
		Rückgabe: Prio als float
	*/
	declare l_prio float default 0;

	if p_followerCount is not null then
		set l_prio = round((((ifnull(p_favoriteCount,0) / 2) + ifnull(p_retweetCount,0)) / (sqrt(ifnull(p_followerCount,1))+100)), 2);
	end if;

	return l_prio;
end;$$

DELIMITER $$
drop function if exists get_general_prio;
create function get_general_prio(
	p_tweetId bigint
) returns float
begin
	/*
		Errechnet die Priorität für einen Tweet anhand dessen TweetId
		 Rückgabe: Prio als float
	*/
	declare l_prio float default 0;

	select calc_tweet_prio(t.favoriteCount, t.retweetCount, a.followerCount)
		into l_prio
		from tweets t, tweetAuthors a
		where t.authorId = a.authorId
		and t.tweetId = p_tweetId;

	return l_prio;
end;$$

DELIMITER $$
drop function if exists get_personal_prio;
create function get_personal_prio(
	p_tweetId bigint, p_username varchar(60)
) returns float
begin
	/*
		Errechnet die persönliche Priorität für einen Tweet und einen Nutzer.
		Dafür wird die allgmeine Tweet-Prio und die vom Benutzer eingestellte Keyword-Prio mit einbezogen
		Rückgabe: Prio als float
	*/
	declare l_general_prio float default 0;
  declare l_keyword_prio float default 0;

  set l_general_prio = get_general_prio(p_tweetId);

  # Wenn mehrere Keywords zu einem Tweet passen, sollen diese Keyword-Prioritaeten addiert werden
  select ifnull(sum(k.priority),1) into l_keyword_prio
    from keywords k, tweets_x_keywords x
  	where k.username = p_username
    	and x.tweetId = p_tweetId
    	and x.keyword = k.keyword
      and k.active = 1;

	return l_keyword_prio * l_general_prio;
end;$$

DELIMITER $$
drop function if exists user_exists;
create function user_exists(
	p_username varchar(60)
) returns bool
begin
	/*
		Überprüft, ob es bereits einen Benutzer mit diesem Namen existiert
	 	Rückgabe: 1:true/0:false
	*/
	declare l_anz int default 0;
	declare l_exists bool default false;

	select count(*) into l_anz from users
		where username = p_username;

	if l_anz > 0 then
		set l_exists = true;
	end if;

	return l_exists;
end;$$

DELIMITER $$
drop function if exists check_preference;
create function check_preference(
	p_preferenceType varchar(3), p_username varchar(60)
) returns int
begin
	/*
		Überprüft, ob die Einstellung vom Benutzer gesetzt wurde. Wenn nicht wird der Standard-Wert genommen
	 	Rückgabe: 1:true/0:false
	*/
	declare l_value int default 0;
	declare l_anz int;

	select ifnull(value,0), count(*) into l_value, l_anz from user_x_preferences
		where username = p_username
		and preferenceType = p_preferenceType;

	if l_anz < 1 then
		select defaultValue into l_value from preferences
			where preferenceType = p_preferenceType;
	end if;

	return l_value;
end;$$

DELIMITER $$
drop function if exists get_tweet_html;
create function get_tweet_html(
	p_tweet_text text, p_author_name varchar(20), p_author_picture_url varchar(100), p_tweet_datum datetime, p_tweet_image varchar(100)
) returns text
begin
	declare l_body text default '';
  	declare l_body_image text default '';
  	
  	if length(p_tweet_image) > 3 then
  		set l_body_image = concat('
		  <div style="
				display:block;
				background-image:url(', p_tweet_image, ');
				float: left;
				min-height: 400px;
				min-width: 530px;
				max-width: 530px;
				border-top-left-radius: 20px;
				border-top-right-radius: 20px;
				box-shadow: 10px 12px 12px 0px rgba(0, 0, 0, 0.50);
				background-size: cover;
				background-repeat: no-repeat;">
			</div>');
  	end if;
  	
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
			max-width: 500px;">', l_body_image, '
			<div style="
				border-top-right-radius: 0px !important;
				border-top-left-radius: 0px !important;
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
				min-height: 40px;">', DATE_FORMAT(p_tweet_datum, '%d.%m.%Y %H:%m'), '</div>
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
	declare l_tweet_image varchar(100);
	declare l_tweet_text varchar(200);
	declare l_tweet_datum datetime;
	declare l_author_name varchar(20);
	declare l_author_picture_url varchar(100);
	declare l_body text default '';
  	declare done int default 0;
	declare cur cursor for 
		select get_personal_prio(t.tweetId, p_username) personal_prio, t.text, a.name, a.pictureUrl, t.createdAt, t.image
		   from tweets t, tweets_x_keywords x, keywords k, tweetauthors a
		   where t.tweetId = x.tweetId
			   and x.keyword = k.keyword
			   and t.authorId = a.authorId
			   and k.username = p_username
			   and k.active = 1
			   and k.positive = 1
			   and date_add(t.createdAt, interval 720 minute) > sysdate()
		   order by personal_prio desc
			limit 5;
	declare continue handler for not found set done = 1;

  	open cur;
		repeat
			fetch cur into l_personal_prio, l_tweet_text, l_author_name, l_author_picture_url, l_tweet_datum, l_tweet_image;
			if not done then
				set l_body = concat(l_body, get_tweet_html(l_tweet_text, l_author_name, l_author_picture_url, l_tweet_datum, l_tweet_image));
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
		select username from users where enabled = 1 and check_preference('not', username) = 1;
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

DELIMITER $$
drop trigger if exists tweets_after_insert;
create trigger tweets_after_insert
after insert
	on tweets for each row
begin
	# Trigger gleicht nach dem Einfügen eines neuen Tweets die Keywords mit dem neuen Tweet ab
	insert into tweets_x_keywords (keyword, tweetId)
		(select distinct k.keyword, new.tweetId from keywords k
			where new.text regexp k.keyword);
end;$$

DELIMITER $$
drop event if exists delete_old_tweets;
create event delete_old_tweets
on schedule every 720 minute
    starts current_timestamp
    on completion preserve
    do
      begin
      # Löscht Tweets aus der Datenbank, die aelter als 5 Tage sind
      delete from tweets_x_keywords where tweetId in (
      	select t.tweetId from tweets t where date_add(t.createdAt, interval 5 day) < sysdate());
      delete from tweets where date_add(createdAt, interval 5 day) < sysdate();
end;$$

DELIMITER $$
drop event if exists notify_users_about_tweets;
create event notify_users_about_tweets
on schedule every 720 minute
    starts '2016-08-24 08:00:00'
    on completion preserve
    do
      begin
      call notify_users();
end;$$
