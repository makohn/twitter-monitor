/* Implementiert die Formel zur Priorisieren von Tweets
	Rückgabe: Prio als float
*/
DELIMITER $$
drop function if exists get_prio;
create function get_prio(
	p_likes int, p_retweets int, p_follower int
) returns float
begin
	declare l_prio float default 0;

	if p_follower is not null then
		set l_prio = round((((ifnull(p_likes,0) / 2) + ifnull(p_retweets,0)) / (sqrt(ifnull(p_follower,1))+100)), 2);
	end if;

	return l_prio;
end;$$

/* Errechnet die Priorität für einen Tweet anhand dessen Retweets, Likes und Anzahl Follower
   Rückgabe: Prio als float
*/
DELIMITER $$
drop function if exists calc_tweet_prio;
create function calc_tweet_prio(
	p_tweet_id bigint
) returns float
begin
	declare l_prio float default 0;

	select get_prio(t.anzahl_likes, t.anzahl_retweets, a.anzahl_follower)
		into l_prio
		from tweets t, tweet_autor a
		where t.autor_id = a.autor_id
		and t.tweet_id = p_tweet_id;

	return l_prio;
end;$$

/* Setzt die Tweet-Priorität neu
*/
DELIMITER $$
drop procedure if exists recalc_tweet_prio;
create procedure recalc_tweet_prio(
	p_tweet_id bigint
)
begin
	declare l_prio float default 0;
	set l_prio = calc_tweet_prio(p_tweet_id);

	update tweets set prio = l_prio where tweet_id = p_tweet_id;
end;$$
