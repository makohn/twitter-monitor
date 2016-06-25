/* Trigger gleicht vor dem Einfügen eines neuen Tweets
   	die Keywords mit dem neuen Tweet ab und
	setzt die Priorität
*/
DELIMITER $$
drop trigger if exists tweets_before_insert;
create trigger tweets_before_insert
before insert
	on tweets for each row
begin
	declare l_followerCount int;

	# TODO: Nicht alle Tweets mit dem entsprechenden Keyword werden beachtet
	insert into tweets_x_keywords (keyword, tweetId)
		(select distinct k.keyword, t.tweetId from tweets t, keywords k
			where new.text regexp k.keyword
			and t.tweetId = new.tweetId);

	select followerCount into l_followerCount
			from tweetAuthors
			where authorId = new.authorId;

	set new.prio = get_prio(new.favoriteCount, new.retweetCount, l_followerCount);
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
