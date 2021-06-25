drop table genres;
drop table story_types;
drop table editors;
drop table authors;
drop table stories;
drop table genre_editor_join;

alter sequence genres_id_seq restart with 1;
alter sequence story_types_id_seq restart with 1;
alter sequence editors_id_seq restart with 1;
alter sequence authors_id_seq restart with 1;
alter sequence stories_id_seq restart with 1;
alter sequence genre_editor_join_id_seq restart with 1;
alter sequence logins_id_seq restart with 1;

create table genres (
	id serial primary key,
	name varchar(20)
);

create table story_types (
	id serial primary key,
	name varchar(15),
	points int
);

create table editors (
	id serial primary key,
	first_name varchar(20),
	last_name varchar(20),
	username varchar(20),
	password varchar(20),
	senior bool,
	assistant bool
);

create table authors (
	id serial primary key,
	first_name varchar(20),
	last_name varchar(20),
	bio varchar,
	points int,
	username varchar(20),
	password varchar(20)
);

create table stories (
	id serial primary key,
	title varchar(50),
	genre int references genres(id),
	story_type int references story_types(id),
	author int references authors(id),
	description varchar,
	tag_line varchar,
	completion_date date,
	approval_status varchar,
	reason varchar,
	submission_date date,
	assistant int references editors,
	editor int references editors,
	senior int references editors,
	request varchar,
	response varchar,
	receiver_name varchar(41),
	requestor_name varchar(41),
	draft varchar,
	modified bool,
	draft_approval_count int
);

insert into stories values
(default, 'a', 1, 1, 1, 'b', 'c', '2021-05-03', 'd', 'e', '2021-05-03', null, null, null, null, null, null, null, null, false, 0);

create table genre_editor_join (
	id serial primary key,
	genre int references genres(id),
	editor int references editors(id)
);

alter table editors add column username varchar(20);
alter table editors add column password varchar(20);
alter table authors add column username varchar(20);
alter table authors add column password varchar(20);

alter table stories add column submission_date date;
alter table stories add column assistant int references editors;
alter table stories add column editor int references editors;
alter table stories add column senior int references editors;
alter table stories add column request varchar;
alter table stories add column response varchar;
alter table stories add column receiver_name varchar(41);
alter table stories add column requestor_name varchar(41);
alter table stories add column draft varchar;
alter table stories add column modified bool;
alter table stories add column draft_approval_count int;

alter table editors add column senior bool;
alter table editors add column assistant bool;

update stories set submission_date = '2021-06-18';
update stories set submission_date = '2021-06-04' where id = 3;
update stories set assistant = null, editor = null;
update stories set requestor_name = 'Hisham Haqq' where id = 3;

update stories set modified = true where id = 3;

update stories set draft_approval_count = 0, approval_status = 'approved_senior';

select * from stories where draft notnull;

delete from stories where id = 5;

select * from genres;
select * from editors;

insert into genre_editor_join values
(default, 1, 2),
(default, 2, 2),
(default, 3, 2),
(default, 1, 3),
(default, 2, 4),
(default, 3, 1),
(default, 1, 5),
(default, 2, 5),
(default, 3, 5);

insert into story_types values 
(default, 'novel', 50),
(default, 'novella', 25),
(default, 'short story', 20),
(default, 'article', 10);

create or replace procedure "Project_1".resetTables()
language sql
as $$
	delete from stories;
	delete from authors;
	delete from genre_editor_join;
	delete from genres;
	delete from editors;

	alter sequence editors_id_seq restart with 1;
	alter sequence authors_id_seq restart with 1;
	alter sequence stories_id_seq restart with 1;
	alter sequence genre_editor_join_id_seq restart with 1;
	alter sequence genres_id_seq restart with 1;
$$;

call "Project_1".resetTables();



select gej.id, g.id as g_id, g.name, e.id as e_id, e.first_name, e.last_name, e.senior, e.assistant
from genre_editor_join gej 
full join genres g
on gej.genre = g.id
full join editors e
on gej.editor = e.id;
order by gej.senior desc, gej.assistant desc;



select gej.id, g.id as g_id, g.name, e.id as e_id, e.first_name, e.last_name, gej.senior, gej.assistant
from genre_editor_join gej 
full join genres g
on gej.genre = g.id
full join editors e
on gej.editor = e.id
where gej.id = 5;

update stories set draft_approval_count = 0 where id = 1;

select * from stories where genre = 1 and story_type in (1, 2) and draft notnull;
select * from stories where genre = 2 and story_type in (1, 2) and draft notnull;
select * from stories where genre = 3 and story_type in (1, 2) and draft notnull;

select * from stories where genre = 2 and story_type = 2 or story_type = 3;

select * from stories where assistant = 3 or editor = 3 or senior = 3;

select * from stories where genre = 2;

select * from stories where genre = 1 and approval_status = 'approved_assistant';

delete from editors where id = 6;
drop table genre_editor_join;
