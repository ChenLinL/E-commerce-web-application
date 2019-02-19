
DELIMITER $$ 

CREATE PROCEDURE add_movie (IN m_title varchar(100), IN m_year int,IN m_director varchar(100), IN star varchar(100), IN genre varchar(32))
BEGIN
declare sid varchar(10);
declare gid varchar(10);
declare m_id varchar(10);
declare m_temp int(10);
declare s_temp int(10);
if(m_title in (select m.title from movies as m where m.title = m_title and m.director = m_director and m.year = m_year)) then
	select CONCAT(m_title, " didnot add to the database because it already existed please try another one") as answer;
else
	select max(id) into m_id from movies;
    select cast(substring(m_id,3) AS unsigned integer)+1 into m_temp;
    select CONCAT(substring(m_id,1,2), cast(m_temp as char)) into m_id;
	insert into movies values (m_id, m_title, m_year, m_director);
    insert into ratings values(m_id, 0,0);
	BEGIN
    select max(id) into sid from stars;
    select cast(substring(sid,3) AS unsigned integer)+1 into s_temp;
    select CONCAT(substring(sid,1,2), cast(s_temp as char)) into sid;
	if(star not in (select s.name from stars as s)) then
		insert into stars values(sid, star, null);
	end if;
	END;
    BEGIN
    set gid = (select max(g.id)+1 from genres as g);
    if(genre not in (select g.name from genres as g)) then
		insert into genres values (gid, genre);
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
