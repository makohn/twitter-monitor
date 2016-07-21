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

/*
DELIMITER $$
drop trigger if exists tweets_before_insert;
create trigger tweets_before_insert
before insert
	on tweets for each row
begin
	# Trigger setzt die Priorität vor dem Einfügen eines neuen Tweets
	declare l_followerCount int;

	select followerCount into l_followerCount
			from tweetAuthors
			where authorId = new.authorId;

	set new.prio = get_prio(new.favoriteCount, new.retweetCount, l_followerCount);
end;$$
*/

/*
DELIMITER $$
drop trigger if exists tweets_before_update;
create trigger tweets_before_update
before update
	on tweets for each row
begin
	# Trigger errechnet nach dem Ändern der Spalten Likes oder Retweets eines Tweets die Priorität neu
	declare l_followerCount int;

	if new.favoriteCount <=> old.favoriteCount or new.retweetCount <=> old.retweetCount then
		select followerCount into l_followerCount
			from tweetAuthors
			where authorId = new.authorId;

		set new.prio = get_prio(new.favoriteCount, new.retweetCount, l_followerCount);
	end if;
end;$$
*/
