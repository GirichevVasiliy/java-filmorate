package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
        filmController = new FilmController();
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
                () -> assertTrue(filmController.getFilms().containsValue(film0), "Фильм не сохранен"),
                () -> assertTrue(filmController.getFilms().containsValue(film1), "Фильм не сохранен"),
                () -> assertTrue(filmController.getFilms().containsValue(film2), "Фильм не сохранен"),
                () -> assertEquals(film0, filmController.getFilms().get(film0.getId()),
                        "Фильмы не одинаковые"),
                () -> assertEquals(film1, filmController.getFilms().get(film1.getId()),
                        "Фильмы не одинаковые"),
                () -> assertEquals(film2, filmController.getFilms().get(film2.getId()),
                        "Фильмы не одинаковые")
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
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Название фильма не может быть пустым.",
                            "Тест без имени фильма провален"),
                    () -> assertFalse(filmController.getFilms().containsValue(film), "Фильм сохранен")
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
                    () -> assertFalse(filmController.getFilms().containsValue(film), "Фильм сохранен")
            );
        }
    }

    @Test
    @DisplayName("Тест создания и валидации фильма дата релиза — не раньше 28 декабря 1895 года")
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
                    () -> assertFalse(filmController.getFilms().containsValue(film), "Фильм сохранен")
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
                    () -> assertFalse(filmController.getFilms().containsValue(film3), "Фильм сохранен")
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
                    () -> assertFalse(filmController.getFilms().containsValue(film), "Фильм сохранен")
            );
        }
    }


    @Test
    void updateFilm() {

    }

    @Test
    void findAllFilms() {
        final int size = 3;
        initFilms();
        List<Film> listOfAllFilm = new ArrayList<>(filmController.findAllFilms());
        assertAll(
                () -> assertTrue(listOfAllFilm.size() == size, "Размер списка всех фильмов больше, " +
                        "тест провален"),
                () -> assertTrue(listOfAllFilm.contains(film0), "Фильм с id = 0 не найден"),
                () -> assertTrue(listOfAllFilm.contains(film1),"Фильм с id = 1 не найден"),
                () -> assertTrue(listOfAllFilm.contains(film2), "Фильм с id = 2 не найден")
        );
    }
}