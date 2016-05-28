/* Überprüft, ob es bereits einen Benutzer mit dieser Email-Adresse existiert
   Rückgabe: 1:true/0:false
*/
DELIMITER $$
drop function if exists user_exists;
create function user_exists(
	p_email varchar(60)
) returns bool
begin
	declare l_anz int default 0;
	declare l_exists bool default false;

	select count(*) into l_anz from benutzer
		where email = p_email;

	if l_anz > 0 then
		set l_exists = true;
	end if;

	return l_exists;
end;$$

/* Legt einen neuen Benutzer an
   Rückgabe: Neue Benutzer-ID bei Erfolg, ansonsten 0
*/
DELIMITER $$
drop function if exists user_register;
create function user_register(
	p_email varchar(60),
	p_passwort text
) returns int(11)
begin
	declare l_benutzer_id int;

	# Abfrage, ob der Benutzer bereits existiert
	if user_exists(p_email) then
		set l_benutzer_id = 0;
	else
		# Benutzer anlegen
		insert into benutzer (email,passwort)
			values (p_email,p_passwort);
		select last_insert_id() into l_benutzer_id;
	end if;

	return l_benutzer_id;
end;$$

/* Überprüft, ob die Login-Daten korrekt sind
   Rückgabe: 1:true/0:false
*/
DELIMITER $$
drop function if exists user_login;
create function user_login(
	p_email varchar(60),
	p_passwort text
) returns bool
begin
	declare l_anz int default 0;
	declare l_login_ok bool default false;

	select count(*) into l_anz from benutzer
		where email = p_email
		and passwort = p_passwort;

	if l_anz > 0 then
		set l_login_ok = true;
	end if;

	return l_login_ok;
end;$$
