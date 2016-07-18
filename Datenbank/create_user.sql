create user 'twitteruser' identified by 'Pikachu#25';
create user 'twitteradmin' identified by 'RoschdwurschdBud4Ever';

/* In Shell ausführen */
grant select on twitter_monitor.* to 'twitteruser';
grant update, insert on twitter_monitor.users to 'twitteruser';
grant update, insert on twitter_monitor.authorities to 'twitteruser';
grant update, insert on twitter_monitor.user_x_preferences to 'twitteruser';
grant update, insert, delete on twitter_monitor.keywords to 'twitteruser';
grant update, insert on twitter_monitor.tweetAuthors to 'twitteruser';
grant update, insert on twitter_monitor.tweets to 'twitteruser';
grant update, insert on twitter_monitor.tweetMedia to 'twitteruser';
grant execute on twitter_monitor.* to 'twitteruser';
grant all privileges on twitter_monitor.* to 'twitteradmin';
