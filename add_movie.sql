
DELIMITER $$ 

CREATE PROCEDURE add_movie (IN m_id varchar(10), IN m_title varchar(100), IN m_year int,IN m_director varchar(100), IN s_id varchar(10), IN star varchar(100), IN g_id int(11), IN genre varchar(32))
BEGIN
declare sid varchar(10);
declare gid varchar(10);
if(m_title in (select m.title from movies as m where m.title = m_title and m.director = m_director and m.year = m_year)) then
	select CONCAT(m_title, " didnot add to the database because it already existed please try another one") as answer;
else
	insert into movies values (m_id, m_title, m_year, m_director);
    insert into ratings values(m_id, 0,0);
	BEGIN
	if(star not in (select s.name from stars as s)) then
		insert into stars values(s_id, star, null);
	end if;
	END;
    BEGIN
    if(genre not in (select g.name from genres as g)) then
		insert into genres values (g_id, genre);
	end if;
    END;
    set sid = (select max(s.id) from stars as s where s.name = star);
	set gid = (select g.id from genres as g where g.name = genre);
	insert into stars_in_movies values(sid,m_id);
	insert into genres_in_movies values(gid,m_id);
	select CONCAT(m_title, " successfully be added to the database. Thank you.") as answer;
end if;
END
$$
DELIMITER ; 
