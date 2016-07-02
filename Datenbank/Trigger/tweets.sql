/* Trigger setzt die Priorität vor dem Einfügen eines neuen Tweets */
DELIMITER $$
drop trigger if exists tweets_before_insert;
create trigger tweets_before_insert
before insert
	on tweets for each row
begin
	declare l_followerCount int;

	select followerCount into l_followerCount
			from tweetAuthors
			where authorId = new.authorId;

	set new.prio = get_prio(new.favoriteCount, new.retweetCount, l_followerCount);
end;$$

/* Trigger gleicht nach dem Einfügen eines neuen Tweets
   	die Keywords mit dem neuen Tweet ab
*/
DELIMITER $$
drop trigger if exists tweets_after_insert;
create trigger tweets_after_insert
before insert
	on tweets for each row
begin
	insert into tweets_x_keywords (keyword, tweetId)
		(select distinct k.keyword, new.tweetId from keywords k
			where new.text regexp k.keyword);
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
	declare l_followerCount int;

	if new.favoriteCount <=> old.favoriteCount or new.retweetCount <=> old.retweetCount then
		select followerCount into l_followerCount
			from tweetAuthors
			where authorId = new.authorId;

		set new.prio = get_prio(new.favoriteCount, new.retweetCount, l_followerCount);
	end if;
end;$$

/*
	Braucht man vielleicht noch einen Trigger für nach dem Löschen eines Tweets, der die Einträge dafür wieder löscht?
	Oder macht das der Foreign Key mit Cascade?
*/
