INSERT INTO bs_author (name, surname)
VALUES
('Ivan','Ivanov'),
('Petr','Petrov');

INSERT INTO bs_book (title, year)
VALUES
('Book1', 2002),
('Book2', 2003),
('Book3', -2000);

INSERT INTO bs_author_book (id_author, id_book)
VALUES
(1,1),
(1,3),
(2,3),
(2,2);