-- Заполнение данными таблицы genre_directory
MERGE INTO genre_directory (id_genre,  genre_name) VALUES ( 1, 'Комедия' );
MERGE INTO genre_directory (id_genre,  genre_name) VALUES ( 2, 'Драма' );
MERGE INTO genre_directory (id_genre,  genre_name) VALUES ( 3, 'Мультфильм' );
MERGE INTO genre_directory (id_genre,  genre_name) VALUES ( 4, 'Триллер' );
MERGE INTO genre_directory (id_genre,  genre_name) VALUES ( 5, 'Документальный' );
MERGE INTO genre_directory (id_genre,  genre_name) VALUES ( 6, 'Боевик' );

-- Заполнение данными таблицы mpa_rating
MERGE INTO mpa_rating (id_mpa_rating, rating_name) VALUES ( 1, 'G' );
MERGE INTO mpa_rating (id_mpa_rating, rating_name) VALUES ( 2, 'PG' );
MERGE INTO mpa_rating (id_mpa_rating, rating_name) VALUES ( 3, 'PG-13' );
MERGE INTO mpa_rating (id_mpa_rating, rating_name) VALUES ( 4, 'R' );
MERGE INTO mpa_rating (id_mpa_rating, rating_name) VALUES ( 5, 'NC-17' );
