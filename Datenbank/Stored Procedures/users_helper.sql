DELIMITER $$
drop function if exists get_personal_prio;
create function get_personal_prio(
	p_tweetId bigint, p_username varchar(60)
) returns float
begin
	/*
		Errechnet die persönliche Priorität für einen Tweet und einen Nutzer.
		Dafür wird die allgmeine Tweet-Prio und die vom Benutzer eingestellte Keyword-Prio mit einbezogen
		Rückgabe: Prio als float
	*/
	declare l_general_prio float default 0;
  declare l_personal_prio float default 0;

  set l_general_prio = get_general_prio(p_tweetId);

  # Wenn mehrere Keywords zu einem Tweet passen, sollen diese Keyword-Prioritaeten addiert werden
  select ifnull(sum(k.priority),1) into l_personal_prio
    from keywords k, tweets_x_keywords x
  	where k.username = p_username
    	and x.tweetId = p_tweetId
    	and x.keyword = k.keyword
      and k.active = 1;

	return l_personal_prio * l_general_prio;
end;$$

DELIMITER $$
drop function if exists user_exists;
create function user_exists(
	p_username varchar(60)
) returns bool
begin
	/*
		Überprüft, ob es bereits einen Benutzer mit diesem Namen existiert
	 	Rückgabe: 1:true/0:false
	*/
	declare l_anz int default 0;
	declare l_exists bool default false;

	select count(*) into l_anz from users
		where username = p_username;

	if l_anz > 0 then
		set l_exists = true;
	end if;

	return l_exists;
end;$$
