/* Trigger gleicht nach dem Einfügen eines neuen Tweets
   	die Keywords mit dem neuen Tweet ab und
	setzt die Priorität
*/
DELIMITER $$
drop trigger if exists tweets_before_insert;
create trigger tweets_before_insert
before insert
	on tweets for each row
begin
	declare l_anzahl_follower int;
	
	insert into tweets_x_keywords (keyword, tweet_id)
		(select distinct k.keyword, t.tweet_id from tweets t, keywords k
			where new.text regexp k.keyword
			and t.tweet_id = new.tweet_id);
			
	select anzahl_follower into l_anzahl_follower
			from tweet_autor
			where autor_id = new.autor_id;	
	set new.prio = get_prio(new.anzahl_likes, new.anzahl_retweets, l_anzahl_follower);
end;$$

/* Trigger errechnet nach dem Ändern der Spalten Likes oder Retweets eines Tweets
   	die Priorität neu
*/
DELIMITER $$
drop trigger if exists tweets_before_update;
create trigger tweets_before_update
before update
	on tweets for each row
begin
	declare l_anzahl_follower int;
	
	if new.anzahl_likes <=> old.anzahl_likes or new.anzahl_retweets <=> old.anzahl_retweets then
		select anzahl_follower into l_anzahl_follower
			from tweet_autor
			where autor_id = new.autor_id;
			
		set new.prio = get_prio(new.anzahl_likes, new.anzahl_retweets, l_anzahl_follower);
	end if;
end;$$

/*
	Braucht man vielleicht noch einen Trigger für nach dem Löschen eines Tweets, der die Einträge dafür wieder löscht?
	Oder macht das der Foreign Key mit Cascade?
*/
