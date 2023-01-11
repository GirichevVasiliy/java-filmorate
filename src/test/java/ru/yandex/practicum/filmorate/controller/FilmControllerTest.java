package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

   /* private FilmController filmController;
    private UserStorage userStorage;
    private Film film0;
    private Film film1;
    private Film film2;
    private User user0;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        //filmController = new FilmController(new FilmService((new InMemoryFilmStorage()), (userStorage)));
    }

    void initFilms() {
        film0 = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("1999-03-28"))
                .duration(300)
                .build();
        filmController.createFilm(film0);
        film1 = Film.builder()
                .name("Harry Potter and the Sorcerer's Stone")
                .description("Harry enters Hogwarts School of Magic and makes friends")
                .releaseDate(LocalDate.parse("2002-08-28"))
                .duration(152)
                .build();
        filmController.createFilm(film1);
        film2 = Film.builder()
                .name("Harry Potter and the Chamber of Secrets")
                .description("House elf, magic diary and ghost.")
                .releaseDate(LocalDate.parse("2003-04-22"))
                .duration(161)
                .build();
        filmController.createFilm(film2);
    }

    void initSetLikes() {
        for (int i = 0; i < 10; i++) {
            film2.addLike(i);
            if (i < 5) {
                film1.addLike(i);
            }
            if (i < 3) {
                film0.addLike(i);
            }
        }
    }

    void initUsers() {
        user0 = User.builder()
                .email("petrov@yandex.ru")
                .login("Petr")
                .name("Stanislav Petrov")
                .birthday(LocalDate.parse("1961-05-21"))
                .build();
    }

    @Test
    @DisplayName("Стандартный тест создания и валидации фильма")
    void createFilmAndValidationStandardTest() {
        initFilms();
        assertAll(
                () -> assertTrue(filmController.findAllFilms().contains(film0),
                        "Фильм " + film0.getName() + " не сохранен"),
                () -> assertTrue(filmController.findAllFilms().contains(film1),
                        "Фильм " + film1.getName() + " не сохранен"),
                () -> assertTrue(filmController.findAllFilms().contains(film2),
                        "Фильм " + film2.getName() + " не сохранен"),
                () -> assertEquals(film0, filmController.findFilmsForId(film0.getId()),
                        "Фильмы " + film0.getName() + " и " +
                                filmController.findFilmsForId(film0.getId()).getName() + " не одинаковые"),
                () -> assertEquals(film1, filmController.findFilmsForId(film1.getId()),
                        "Фильмы " + film1.getName() + " и " +
                                filmController.findFilmsForId(film1.getId()).getName() + " не одинаковые"),
                () -> assertEquals(film2, filmController.findFilmsForId(film2.getId()),
                        "Фильмы " + film2.getName() + " и " +
                                filmController.findFilmsForId(film2.getId()).getName() + " не одинаковые")
        );
    }

    @Test
    @DisplayName("Тест создания и валидации фильма без названия")
    void createFilmAndValidationNameIsBlankTest() {
        Film film = Film.builder()
                .name("")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(154)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);}, "Тест создания и валидации фильма без названия провален");
    }

    @Test
    @DisplayName("Тест создания и валидации фильма максимальная длина описания более 200 символов")
    void createFilmAndValidationDescriptionLengthOver200Test() {
        Film film = Film.builder()
                .name("Futurama")
                .description("Futurama is an American adult sci-fi satirical animated television series created " +
                        "at 20th Century Fox by Matt Groening and David Cohen, creators of The Simpsons. " +
                        "On February 9, it was announced that streaming service Hulu was resurrecting the " +
                        "series and had already ordered 20 new episodes.")
                .releaseDate(LocalDate.parse("1999-03-28"))
                .duration(300)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
        }, "Тест создания и валидации фильма максимальная длина описания более 200 символов провален");
    }

    @Test
    @DisplayName("Тест создания и валидации фильма, дата релиза — не раньше 28 декабря 1895 года")
    void createFilmAndValidationDateOfReleaseTest() {
        Film film = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("1800-03-28"))
                .duration(300)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
        }, "Тест создания и валидации фильма, дата релиза — не раньше 28 декабря 1895 года провален");
    }

    @Test
    @DisplayName("Тест создания и валидации фильма продолжительность фильма должна быть положительной")
    void createFilmAndValidationDurationTest() {
        Film film = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(-1)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
        }, "Тест создания и валидации фильма продолжительность фильма должна быть положительной - првален");
    }

    @Test
    @DisplayName("Тест создания и валидации несуществующего фильма")
    void createFilmAndValidationFilmNullTest() {
        assertThrows(RuntimeException.class, () -> {
            filmController.createFilm(null);
        }, "Тест обновления фильма провален провален");
    }

    @Test
    @DisplayName("Тест обновления фильмов")
    void updateFilmStandardTest() {
        final int idFilm0 = 1;
        final int idFilm1 = 2;
        initFilms();
        final Film film0Control = Film.builder()
                .id(idFilm0)
                .name("Fu-tu-ra-ma")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("1999-03-28"))
                .duration(310)
                .build();
        filmController.updateFilm(film0Control);
        final Film film1Control = Film.builder()
                .id(idFilm1)
                .name("Harry-Potter")
                .description("Harry enters Hogwarts School.")
                .releaseDate(LocalDate.parse("2000-10-12"))
                .duration(252)
                .build();
        filmController.updateFilm(film1Control);
        assertAll(
                () -> assertTrue(filmController.findAllFilms().contains(film0Control),
                        "Фильм " + film0Control.getName() + " не сохранен"),
                () -> assertEquals(film0Control, filmController.findFilmsForId(film0Control.getId()),
                        "Фильмы " + film0Control.getName() + " и " +
                                filmController.findFilmsForId(film0Control.getId()).getName() + " не одинаковые"),
                () -> assertEquals(film0, filmController.findFilmsForId(film0Control.getId()),
                        "Фильмы " + film0.getName() + " и " +
                                filmController.findFilmsForId(film0Control.getId()).getName() + " не одинаковые")
        );

        assertAll(
                () -> assertTrue(filmController.findAllFilms().contains(film1Control),
                        "Фильм " + film1Control.getName() + " не сохранен"),
                () -> assertEquals(film1, filmController.findFilmsForId(film1Control.getId()),
                        "Фильмы " + film1.getName() + " и " +
                                filmController.findFilmsForId(film1.getId()).getName() + " не одинаковые"),
                () -> assertEquals(film1, filmController.findFilmsForId(film1Control.getId()),
                        "Фильмы " + film1.getName() + " и " +
                                filmController.findFilmsForId(film1Control.getId()).getName() + " не одинаковые")
        );
    }

    @Test
    @DisplayName("Тест обновления и валидации фильма без названия")
    void updateFilmAndValidationNameIsBlankTest() {
        initFilms();
        final int idFilm = 1;
        Film film = Film.builder()
                .id(idFilm)
                .name("")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(154)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        }, "Тест обновления и валидации фильма без названия провален");
    }

    @Test
    @DisplayName("Тест обновления и валидации фильма максимальная длина описания более 200 символов")
    void updateFilmAndValidationDescriptionLengthOver200Test() {
        final int idFilm = 1;
        initFilms();
        Film film = Film.builder()
                .id(idFilm)
                .name("Futurama")
                .description("Futurama is an American adult sci-fi satirical animated television series created " +
                        "at 20th Century Fox by Matt Groening and David Cohen, creators of The Simpsons. " +
                        "On February 9, it was announced that streaming service Hulu was resurrecting the " +
                        "series and had already ordered 20 new episodes.")
                .releaseDate(LocalDate.parse("1999-03-28"))
                .duration(300)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        }, "Тест обновления и валидации фильма максимальная длина описания более 200 символов - провален");
    }

    @Test
    @DisplayName("Тест обновления и валидации фильма, дата релиза — не раньше 28 декабря 1895 года")
    void updateFilmAndValidationDateOfReleaseTest() {
        final int idFilm = 1;
        initFilms();
        Film film = Film.builder()
                .id(idFilm)
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("1800-03-28"))
                .duration(300)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        }, "Тест обновления и валидации фильма, дата релиза — не раньше 28 декабря 1895 года - провален");
    }

    @Test
    @DisplayName("Тест обновления и валидации фильма продолжительность фильма должна быть положительной")
    void updateFilmAndValidationDurationTest() {
        final int idFilm = 1;
        initFilms();
        Film film = Film.builder()
                .id(idFilm)
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(-1)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
        }, "Тест обновления и валидации фильма продолжительность фильма должна быть положительной провален");
    }

    @Test
    @DisplayName("Тест обновления и валидации несуществующего фильма")
    void updateFilmAndValidationFilmNullTest() {
        assertThrows(RuntimeException.class, () -> {
            filmController.updateFilm(null);
        }, "Тест обновления и валидации несуществующего фильма провален");
    }

    @Test
    @DisplayName("Получение списка всех фильмов")
    void findAllFilmsTest() {
        final int size = 3;
        initFilms();
        List<Film> listOfAllFilm = new ArrayList<>(filmController.findAllFilms());
        assertAll(
                () -> assertEquals(size, listOfAllFilm.size(), "Размер списка всех фильмов больше, " +
                        "тест провален"),
                () -> assertTrue(listOfAllFilm.contains(film0), "Фильм с id = 0 не найден"),
                () -> assertTrue(listOfAllFilm.contains(film1), "Фильм с id = 1 не найден"),
                () -> assertTrue(listOfAllFilm.contains(film2), "Фильм с id = 2 не найден")
        );
    }

    @Test
    @DisplayName("Получение фильма по ID")
    void findFilmsForIdTest() {
        initFilms();
        final int id = 1;
        Film filmSearch = filmController.findFilmsForId(id);
        assertEquals(filmSearch, film0, "Фильмы не совпали");
    }

    @Test
    @DisplayName("Получение фильма по неверному ID")
    void findFilmsForBadIdTest() {
        initFilms();
        final int id = -1;
        assertThrows(ResourceNotFoundException.class, () -> {
            filmController.findFilmsForId(id);
        }, "Получение фильма c неверным id провален");
    }

    @Test
    @DisplayName("Получение списка всех фильмов с максимальными лайками")
    void findTopTenMostLikesFilmsTest() {
        final int ratingFilm0 = 2;
        final int ratingFilm1 = 1;
        final int ratingFilm2 = 0;
        final int count = 5;
        initFilms();
        initSetLikes();
        List<Film> films = new ArrayList<>(filmController.findTopTenMostLikesFilms(count));
        assertAll(
                () -> assertEquals(films.get(ratingFilm2), film2, "Ошибка рейтинга фильмов"),
                () -> assertEquals(films.get(ratingFilm1), film1, "Ошибка рейтинга фильмов"),
                () -> assertEquals(films.get(ratingFilm0), film0, "Ошибка рейтинга фильмов")

        );
    }

    @Test
    @DisplayName("Получение списка одного фильма с максимальными лайками")
    void findTopTenMostLikesFilmsCountEqualsOneTest() {
        final int ratingFilm2 = 0;
        final int count = 1;
        initFilms();
        initSetLikes();
        List<Film> films = new ArrayList<>(filmController.findTopTenMostLikesFilms(count));
        assertAll(
                () -> assertEquals(films.get(ratingFilm2), film2, "Ошибка рейтинга фильмов"),
                () -> assertFalse(films.contains(film1), "Фильтрация фильмов не работает"),
                () -> assertFalse(films.contains(film0), "Фильтрация фильмов не работает")

        );
    }

    @Test
    @DisplayName("Получение списка всех фильмов с максимальными лайками и отриц. count")
    void findTopTenMostLikesFilmsCountEqualsMinusOneTest() {
        final int ratingFilm0 = 2;
        final int ratingFilm1 = 1;
        final int ratingFilm2 = 0;
        final int count = -1;
        initFilms();
        initSetLikes();
        List<Film> films = new ArrayList<>(filmController.findTopTenMostLikesFilms(count));
        assertAll(
                () -> assertEquals(films.get(ratingFilm2), film2, "Ошибка рейтинга фильмов"),
                () -> assertEquals(films.get(ratingFilm1), film1, "Ошибка рейтинга фильмов"),
                () -> assertEquals(films.get(ratingFilm0), film0, "Ошибка рейтинга фильмов")

        );
    }

    @Test
    @DisplayName("Получение списка всех фильмов с максимальными лайками count = null")
    void findTopTenMostLikesFilmsCountEqualsNullTest() {
        final int ratingFilm0 = 2;
        final int ratingFilm1 = 1;
        final int ratingFilm2 = 0;
        initFilms();
        initSetLikes();
        List<Film> films = new ArrayList<>(filmController.findTopTenMostLikesFilms(null));
        assertAll(
                () -> assertEquals(films.get(ratingFilm2), film2, "Ошибка рейтинга фильмов"),
                () -> assertEquals(films.get(ratingFilm1), film1, "Ошибка рейтинга фильмов"),
                () -> assertEquals(films.get(ratingFilm0), film0, "Ошибка рейтинга фильмов")

        );
    }

    @Test
    @DisplayName("Удаление лайка у фильма")
    void deleteLikeFilmTest() {
        initFilms();
        initUsers();
        userStorage.addUser(user0);
        initSetLikes();
        final int id = film0.getId();
        final int userId = 1;
        final int countLikeSize = 3;
        final int countLikeSizeAfterDelete = 2;
        assertEquals(filmController.findFilmsForId(id).getLikeCount(), countLikeSize);
        filmController.deleteLikeFilm(id, userId);
        assertEquals(filmController.findFilmsForId(id).getLikeCount(), countLikeSizeAfterDelete, "Лайк не удален");
        assertFalse(filmController.findFilmsForId(id).getWhoLikedUserIds().contains(userId), "Лайк не удален");
    }

    @Test
    @DisplayName("Удаление лайка у фильма с неверным id")
    void deleteLikeFilmBadIdFilmTest() {
        initFilms();
        initUsers();
        userStorage.addUser(user0);
        initSetLikes();
        final int id = -1;
        final int userId = 1;
        assertThrows(ResourceNotFoundException.class, () -> {
            filmController.deleteLikeFilm(id, userId);
        }, "Получение фильма c неверным id провален");
    }

    @Test
    @DisplayName("Удаление лайка у фильма с неверным id пользователя")
    void deleteLikeFilmBadIdUserTest() {
        initFilms();
        initUsers();
        userStorage.addUser(user0);
        initSetLikes();
        final int id = 1;
        final int userId = -1;
        assertThrows(ResourceNotFoundException.class, () -> {
            filmController.deleteLikeFilm(id, userId);
        }, "Получение фильма c неверным id провален");
    }*/
}