/*
Event: Löscht Tweets aus der Datenbank, die aelter als 5 Tage sind
*/
DELIMITER $$
drop event if exists delete_old_tweets;
create event delete_old_tweets
on schedule every 720 minute
    starts current_timestamp
    on completion preserve
    do
      begin
      delete from tweetsMedia where tweetId in (
      	select t.tweetId from tweets t where date_add(t.createdAt, interval 5 day) < sysdate());
      delete from tweets_x_keywords where tweetId in (
      	select t.tweetId from tweets t where date_add(t.createdAt, interval 5 day) < sysdate());
      delete from tweets where date_add(createdAt, interval 5 day) < sysdate();
end;$$
