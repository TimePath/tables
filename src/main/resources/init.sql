CREATE TABLE groups (
    id int NOT NULL PRIMARY KEY,
    name varchar(20) NOT NULL
);

CREATE TABLE users (
    id int NOT NULL PRIMARY KEY,
    name varchar(20) NOT NULL
);

CREATE TABLE group_users (
    PRIMARY KEY ( gid, uid ),
    gid int NOT NULL REFERENCES groups ( id ),
    uid int NOT NULL REFERENCES users ( id )
);


CREATE TABLE descriptors (
    id int NOT NULL PRIMARY KEY,
    uri varchar(20) NOT NULL UNIQUE,
    gid int NOT NULL REFERENCES groups ( id ),
    created timestamp NOT NULL
);

CREATE TABLE pathnames (
    id int NOT NULL PRIMARY KEY,
    parent int REFERENCES pathnames ( id ),
    name varchar(20) NOT NULL,
    fd int REFERENCES descriptors ( id ),
    CONSTRAINT nodupes UNIQUE ( parent, name )
);
