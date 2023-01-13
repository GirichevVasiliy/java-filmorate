INSERT INTO model_user (email, login, name, birthday) VALUES ('petrov@yandex.ru', 'Petr', 'Stanislav Petrov', '1961-05-21');
INSERT INTO model_user (email, login, name, birthday) VALUES ('girichev@yandex.ru', 'VasiliyGir', 'Vasiliy Girichev', '1987-02-26');
INSERT INTO model_user (email, login, name, birthday) VALUES ('ivanov@yandex.ru', 'VladIvanov', 'Vlad Ivanov', '1989-01-01');
INSERT INTO model_user (email, login, name, birthday) VALUES ('sidorov@yandex.ru', 'VovaSidorov', 'Sidorov Vova', '1966-03-02');

INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 1, 2, false );
INSERT INTO users_friends(user_id, friend_id, status) VALUES ( 2, 3, true );

INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 1, 1 );
INSERT INTO FILM_LIKES (film_id, user_id) VALUES ( 1, 2 );