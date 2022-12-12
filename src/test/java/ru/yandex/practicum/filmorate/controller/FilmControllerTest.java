package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film film0;
    private Film film1;
    private Film film2;

    @BeforeEach
    void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
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
    @DisplayName("Тест создания и валидации повторяющегося пользователя")
    void createFilmAndValidationRepeatingTest() {
        initFilms();
        assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film0);
        }, "Тест создания и валидации повторяющегося фильма провален");
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
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Название фильма не может быть пустым.",
                            "Тест без имени фильма провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
    }


    @Test
    @DisplayName("Тест создания и валидации фильма максимальная длина описания более 200 символов")
    void createFilmAndValidationDescriptionLengthOver200Test() {
        final int maxLengthDescription = 200;
        Film film = Film.builder()
                .name("Futurama")
                .description("Futurama is an American adult sci-fi satirical animated television series created " +
                        "at 20th Century Fox by Matt Groening and David Cohen, creators of The Simpsons. " +
                        "On February 9, it was announced that streaming service Hulu was resurrecting the " +
                        "series and had already ordered 20 new episodes.")
                .releaseDate(LocalDate.parse("1999-03-28"))
                .duration(300)
                .build();
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertTrue(film.getDescription().length() > maxLengthDescription,
                            " В тесте не хватает символов"),
                    () -> assertEquals(e.getMessage(), "Максимальная длина описания более 200 символов.",
                            "Тест длины описания провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
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
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Дата релиза раньше 28 декабря 1895 года",
                            "Тест даты релиза провален провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
        Film film3 = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(300)
                .build();
        try {
            filmController.createFilm(film3);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertFalse(filmController.findAllFilms().contains(film3),
                            "Фильм " + film3.getName() + " сохранен")
            );
        }
    }

    @Test
    @DisplayName("Тест создания и валидации фильма продолжительность фильма должна быть положительной")
    void createFilmAndValidationDurationTest() {
        final int durationZero = 0;
        Film film = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(-1)
                .build();
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertTrue(film.getDuration() < durationZero, "Продолжительность положительная"),
                    () -> assertEquals(e.getMessage(), "Продолжительность фильма должна быть положительной.",
                            "Тест продолжительности фильма провален провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
    }

    @Test
    @DisplayName("Тест создания и валидации несуществующего фильма")
    void createFilmAndValidationFilmNullTest() {
        Film film = null;
        try {
            filmController.createFilm(film);
        } catch (RuntimeException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Ошибка, фильм не задан",
                            "Тест обновления фильма провален провален")
            );
        }
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
        try {
            filmController.updateFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Название фильма не может быть пустым.",
                            "Тест без имени фильма провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
    }

    @Test
    @DisplayName("Тест обновления и валидации фильма максимальная длина описания более 200 символов")
    void updateFilmAndValidationDescriptionLengthOver200Test() {
        final int maxLengthDescription = 200;
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
        try {
            filmController.updateFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertTrue(film.getDescription().length() > maxLengthDescription,
                            " В тесте не хватает символов"),
                    () -> assertEquals(e.getMessage(), "Максимальная длина описания более 200 символов.",
                            "Тест длины описания провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
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
        try {
            filmController.updateFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Дата релиза раньше 28 декабря 1895 года",
                            "Тест даты релиза провален провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
        Film film3 = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(300)
                .build();
        try {
            filmController.createFilm(film3);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertFalse(filmController.findAllFilms().contains(film3),
                            "Фильм " + film3.getName() + " сохранен")
            );
        }
    }

    @Test
    @DisplayName("Тест обновления и валидации фильма продолжительность фильма должна быть положительной")
    void updateFilmAndValidationDurationTest() {
        final int durationZero = 0;
        final int idFilm = 1;
        initFilms();
        Film film = Film.builder()
                .id(idFilm)
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("2002-03-28"))
                .duration(-1)
                .build();
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertTrue(film.getDuration() < durationZero, "Продолжительность положительная"),
                    () -> assertEquals(e.getMessage(), "Продолжительность фильма должна быть положительной.",
                            "Тест продолжительности фильма провален провален"),
                    () -> assertFalse(filmController.findAllFilms().contains(film),
                            "Фильм " + film.getName() + " сохранен")
            );
        }
    }

    @Test
    @DisplayName("Тест обновления и валидации несуществующего фильма")
    void updateFilmAndValidationFilmNullTest() {
        Film film = null;
        try {
            filmController.updateFilm(film);
        } catch (RuntimeException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Ошибка, фильм не задан",
                            "Тест обновления фильма провален провален")
            );
        }
    }

    @Test
    @DisplayName("Получение списка всех фильмов")
    void findAllFilms() {
        final int size = 3;
        initFilms();
        List<Film> listOfAllFilm = new ArrayList<>(filmController.findAllFilms());
        assertAll(
                () -> assertTrue(listOfAllFilm.size() == size, "Размер списка всех фильмов больше, " +
                        "тест провален"),
                () -> assertTrue(listOfAllFilm.contains(film0), "Фильм с id = 0 не найден"),
                () -> assertTrue(listOfAllFilm.contains(film1), "Фильм с id = 1 не найден"),
                () -> assertTrue(listOfAllFilm.contains(film2), "Фильм с id = 2 не найден")
        );
    }
}