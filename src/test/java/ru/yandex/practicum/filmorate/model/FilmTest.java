package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    @Test
    @DisplayName("Проверка создание объекта")
    void createFilm() {
        final int idFilm = 0;
        final int filmDuration = 300;
        Film film = Film.builder()
                .name("Futurama")
                .description("American science fiction satirical adult animated television series")
                .releaseDate(LocalDate.parse("1999-03-28"))
                .duration(300)
                .build();
        assertAll(
                () -> assertEquals(idFilm, film.getId(), "id не совпал"),
                () -> assertEquals(film.getName(), "Futurama", "Название фильма не совпало"),
                () -> assertEquals(film.getDescription(),
                        "American science fiction satirical adult animated television series",
                        "Описание фильма не совпало"),
                () -> assertEquals(film.getReleaseDate(), LocalDate.parse("1999-03-28"),
                        "Дата релиза фильма не совпала"),
                () -> assertEquals(filmDuration, film.getDuration(), "Продолжительность фильма не совпала")
        );
    }

    @Test
    @DisplayName("Проверка создание объекта без названия")
    void createFilmNameNull() {
        try {
            Film film = Film.builder()
                    .name(null)
                    .description("American science fiction satirical adult animated television series")
                    .releaseDate(LocalDate.parse("1999-03-28"))
                    .duration(300)
                    .build();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "name is marked non-null but is null");
        }
    }

    @Test
    @DisplayName("Проверка создание объекта без описания")
    void createFilmDescriptionNull() {
        try {
            Film film = Film.builder()
                    .name("Futurama")
                    .description(null)
                    .releaseDate(LocalDate.parse("1999-03-28"))
                    .duration(300)
                    .build();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "description is marked non-null but is null");
        }
    }

    @Test
    @DisplayName("Проверка создание объекта без даты релиза")
    void createFilmReleaseDateNull() {
        try {
            Film film = Film.builder()
                    .name("Futurama")
                    .description("American science fiction satirical adult animated television series")
                    .releaseDate(null)
                    .duration(300)
                    .build();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "releaseDate is marked non-null but is null");
        }
    }
}