INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-1', 'film1', '1987-02-01', 50, 1 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-2', 'film2', '1988-03-07', 150, 2 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-3', 'film3', '1989-04-09', 134, 3 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-4', 'film4', '1990-05-12', 12, 4 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-5', 'film5', '1991-06-21', 76, 5 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-6', 'film6', '1992-07-03', 103, 1 );
INSERT INTO model_film (name,  description, releaseDate, duration, mpaRating_rating) VALUES ('Фильм-7', 'film7', '1993-07-03', 103, 1 );

INSERT INTO films_genre (film_id, genre_id) VALUES (1, 1);
INSERT INTO films_genre (film_id, genre_id) VALUES (1, 2);
INSERT INTO films_genre (film_id, genre_id) VALUES (2, 2);
INSERT INTO films_genre (film_id, genre_id) VALUES (3, 4);
INSERT INTO films_genre (film_id, genre_id) VALUES (4, 4);
INSERT INTO films_genre (film_id, genre_id) VALUES (5, 5);
INSERT INTO films_genre (film_id, genre_id) VALUES (6, 1);
INSERT INTO films_genre (film_id, genre_id) VALUES (7, 1);

INSERT INTO model_user (email, login, name, birthday) VALUES ('petrov@yandex.ru', 'Petr', 'Stanislav Petrov', '1961-05-21');
INSERT INTO model_user (email, login, name, birthday) VALUES ('girichev@yandex.ru', 'VasiliyGir', 'Vasiliy Girichev', '1987-02-26');
INSERT INTO model_user (email, login, name, birthday) VALUES ('ivanov@yandex.ru', 'VladIvanov', 'Vlad Ivanov', '1989-01-01');
INSERT INTO model_user (email, login, name, birthday) VALUES ('sidorov@yandex.ru', 'VovaSidorov', 'Sidorov Vova', '1966-03-02');

--INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 1, 2, true );
--INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 1, 3, true );
--INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 2, 1, true );
--INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 2, 3, true );

INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 1, 1 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 1, 2 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 2, 3 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 3, 2 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 4, 2 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 5, 2 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 6, 2 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 7, 2 );