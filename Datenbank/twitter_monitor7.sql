
DROP DATABASE `twitter_monitor`;
CREATE DATABASE `twitter_monitor` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `twitter_monitor`;

CREATE TABLE `users` (
  `username` varchar(60) NOT NULL,
  `password` varchar(80) NOT NULL,
  `enabled` tinyint default 1 NOT NULL,
  `email` varchar(60) NOT NULL,  
  `registeredAt` datetime default current_timestamp,
  PRIMARY KEY (`username`)
);

CREATE TABLE `authorities` (
  `username` varchar(60) NOT NULL,
  `authority` varchar(60) NOT NULL, 
  PRIMARY KEY (`username`, `authority`)
);

CREATE TABLE `keywords` (
  `keyword` varchar(50) NOT NULL,
  `username` varchar(60) NOT NULL,
  `priority` int(1) NOT NULL,
  `positive` tinyint DEFAULT 1 NOT NULL,
  `active` tinyint DEFAULT 1 NOT NULL,
  `createdAt` datetime default current_timestamp,
  PRIMARY KEY (`keyword`,`username`)
);

CREATE TABLE `tweetAuthors` (
  `authorId` bigint NOT NULL,
  `name` varchar(20) NOT NULL,
  `screenName` varchar(20),
  `followerCount` int(11),
  `pictureUrl` varchar(100),
  PRIMARY KEY (`authorId`)
);

CREATE TABLE `tweets` (
  `tweetId` bigint NOT NULL,
  `authorId` bigint NOT NULL,
  `text` varchar(200) NOT NULL,
  `favoriteCount` int(11) NOT NULL,
  `retweetCount` int(11) NOT NULL,
  `place` varchar(60) NOT NULL,
  `image` varchar(100),
  `createdAt` datetime NOT NULL,
  `lastUpdate` datetime default current_timestamp,
  PRIMARY KEY (`tweetId`)
);

CREATE TABLE `tweets_x_keywords` (
  `tweetId` bigint NOT NULL,
  `keyword` varchar(50) NOT NULL,
  PRIMARY KEY (`tweetId`, `keyword`)
);



CREATE TABLE `notifications` (
  `username` varchar(60) NOT NULL,
  `enabled` tinyint Default 1 NOT NULL,
  `threshold` int(11) NOT NULL,
  PRIMARY KEY (`username`)
);

/* Trigger für nach dem einfügen eines Tweets 
		- tweet_x_keyword aktualisieren
*/
DELIMITER $$
create trigger tweets_after_insert after insert
	ON tweets FOR EACH ROW
BEGIN    
	insert into tweets_x_keywords (keyword, tweetId)
		(select distinct k.keyword, new.tweetId
			from keywords k
			where 	new.text regexp k.keyword);
END; $$

DELIMITER $$
create trigger keywords_after_insert after insert
	ON keywords FOR EACH ROW
BEGIN    
	insert ignore into tweets_x_keywords (keyword, tweetId)
		(select new.keyword, tweetId 
			from tweets t
			where t.text regexp new.keyword);
END; $$


DELIMITER $$
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

      delete from authors where authorId not in
		(select distinct authorId from tweets);
end;$$



