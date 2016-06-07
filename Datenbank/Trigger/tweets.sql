/* Trigger gleicht nach dem Einfügen eines neuen Tweets
   	die Keywords mit dem neuen Tweet ab
*/
DELIMITER $$
drop trigger if exists tweets_after_insert;
create trigger tweets_after_insert
after insert
	on tweets for each row
begin
	insert into tweets_x_keywords (keyword, tweet_id)
		(select distinct k.keyword, t.tweet_id from tweets t, keywords k
			where new.text regexp k.keyword
			and t.tweet_id = new.tweet_id);
end;$$
