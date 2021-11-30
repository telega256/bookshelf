CREATE SEQUENCE hibernate_sequence  INCREMENT 1  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE bs_author_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  MAXVALUE 2147483647
  CACHE 1;

CREATE TABLE bs_author (
  id int8 NOT NULL DEFAULT nextval('bs_author_id_seq'),
  name VARCHAR(255),
  surname VARCHAR(255),
  image BYTEA,
  PRIMARY KEY (id));

CREATE SEQUENCE bs_book_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  MAXVALUE 2147483647
  CACHE 1;

CREATE TABLE bs_book (
  id int8 NOT NULL DEFAULT nextval('bs_book_id_seq'),
  title VARCHAR(255),
  year int4,
  PRIMARY KEY (id));

CREATE SEQUENCE bs_author_book_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  MAXVALUE 2147483647
  CACHE 1;

CREATE TABLE bs_author_book (
  id int8 NOT NULL DEFAULT nextval('bs_author_book_id_seq'),
  id_author int8 NOT NULL,
  id_book int8 NOT NULL,
  PRIMARY KEY (id_author, id_book));

CREATE SEQUENCE bs_author_aud_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  MAXVALUE 2147483647
  CACHE 1;

CREATE TABLE bs_author_aud(
  id int8 NOT NULL DEFAULT nextval('bs_author_aud_id_seq'),
  rev     INTEGER NOT NULL,
  revtype smallint,
  name VARCHAR(255),
  surname VARCHAR(255),
  PRIMARY KEY (id, rev));

CREATE SEQUENCE bs_book_aud_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  MAXVALUE 2147483647
  CACHE 1;

CREATE TABLE bs_book_aud(
  id int8 NOT NULL DEFAULT nextval('bs_book_aud_id_seq'),
  rev     INTEGER NOT NULL,
  revtype smallint,
  title   VARCHAR(255),
  year int4,
  PRIMARY KEY ( id, rev ));

CREATE SEQUENCE bs_author_book_aud_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  MAXVALUE 2147483647
  CACHE 1;

CREATE TABLE bs_author_book_aud(
  id int8 NOT NULL DEFAULT nextval('bs_author_book_aud_id_seq'),
  rev     INTEGER NOT NULL,
  revtype smallint,
  id_author int8 NOT NULL,
  id_book int8 NOT NULL,
  PRIMARY KEY ( id, rev, id_author, id_book));

CREATE TABLE revinfo(
  rev INTEGER GENERATED BY DEFAULT
      AS IDENTITY ( START WITH 1 ),
  revtstmp BIGINT,
  PRIMARY KEY (rev));

ALTER TABLE  if EXISTS bs_book_aud
ADD CONSTRAINT bs_book_aud_fk
FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE  if EXISTS bs_author_aud
ADD CONSTRAINT bs_author_aud_fk
FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE  if EXISTS bs_author_book_aud
ADD CONSTRAINT bs_author_book_aud_fk
FOREIGN KEY (rev) REFERENCES revinfo;

ALTER TABLE if EXISTS bs_author_book
ADD CONSTRAINT  bs_author_fk
FOREIGN KEY (id_author) REFERENCES bs_author;

ALTER TABLE if EXISTS bs_author_book
ADD CONSTRAINT  bs_book_fk
FOREIGN KEY (id_book) REFERENCES bs_book;


