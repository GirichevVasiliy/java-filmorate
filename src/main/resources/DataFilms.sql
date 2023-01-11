INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-1', 'film1', '1987-02-01', 50, 1 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-2', 'film2', '1988-03-07', 150, 2 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-3', 'film3', '1989-04-09', 134, 3 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-4', 'film4', '1990-05-12', 12, 4 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-5', 'film5', '1991-06-21', 76, 5 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-6', 'film6', '1992-07-03', 103, 1 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-7', 'film7', '1993-07-03', 103, 1 );

INSERT INTO films_genre (film_id, genre_id) VALUES (1, 1);
INSERT INTO films_genre (film_id, genre_id) VALUES (1, 2);

INSERT INTO model_user (email, login, name, birthday) VALUES ('fg@ya.ru', 'Bravo', 'Nikolay', '1993-07-03');
INSERT INTO model_user (email, login, name, birthday) VALUES ('gtr@ya.ru', 'PetrVi', 'petr', '1995-02-11');
INSERT INTO model_user (email, login, name, birthday) VALUES ('trt@ya.ru', 'Vika', 'Viktoria', '1991-03-12');
INSERT INTO model_user (email, login, name, birthday) VALUES ('eee@ya.ru', 'An', 'Anna', '1999-01-19');

INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 1, 2, false );
INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 2, 3, true );

INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 1, 1 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 1, 2 );


