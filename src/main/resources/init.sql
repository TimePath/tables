CREATE TABLE groups (
    id int NOT NULL PRIMARY KEY,
    name varchar(20) NOT NULL
)
$$
CREATE TABLE users (
    id int NOT NULL PRIMARY KEY,
    name varchar(20) NOT NULL
)
$$
CREATE TABLE group_users (
    PRIMARY KEY ( gid, uid ),
    gid int NOT NULL REFERENCES groups ( id ),
    uid int NOT NULL REFERENCES users ( id )
)
$$
CREATE TABLE descriptors (
    id int NOT NULL PRIMARY KEY,
    uri varchar(20) NOT NULL UNIQUE,
    gid int NOT NULL REFERENCES groups ( id ),
    created timestamp NOT NULL
)
$$
CREATE TABLE pathnames (
    id int NOT NULL PRIMARY KEY,
    parent int REFERENCES pathnames ( id ),
    name varchar(20) NOT NULL,
    fd int REFERENCES descriptors ( id )
)
$$
insert into users (id, name) values (1, 'test')
$$
insert into groups (id, name) values (1, 'test')
$$
insert into group_users (gid, uid) values (1, 1)
$$
insert into descriptors (id, uri, gid, created) values (1, 'file:///etc/passwd', 1, CURRENT_TIMESTAMP)
$$
insert into pathnames (id, parent, name, fd) values (1, null, 'test.txt', 1)
