DELIMITER $$
drop event if exists delete_old_tweets;
create event delete_old_tweets
on schedule every 120 minute
    starts current_timestamp
    on completion preserve
    do
      begin
      # Löscht Tweets aus der Datenbank, die aelter als 2 Tage sind
      delete from tweets_x_keywords where tweetId in (
      	select t.tweetId from tweets t where date_add(t.createdAt, interval 2 day) < sysdate());
      delete from tweets where date_add(createdAt, interval 2 day) < sysdate();
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