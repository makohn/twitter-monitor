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
