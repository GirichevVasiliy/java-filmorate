package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (!Objects.isNull(film)) {
            if (filmVerification(film) && filmValidation(film)) {
                generateIdFilms(film);
                films.put(film.getId(), film);
                log.info("Получен запрос к эндпоинту: Добавление нового фильма " + film.getName() + " - выполнено");
                return film;
            } else {
                log.warn("Получен запрос к эндпоинту: Добавление нового фильма " + film.getName() + " - не выполнен");
                throw new ValidationException("Фильм " + film.getName() + " не сохранен, он был зарегистрирован ранее");
            }
        } else {
            throw new RuntimeException("Ошибка, фильм не задан");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!Objects.isNull(film)) {
            if (films.containsKey(film.getId())) {
                if (filmValidation(film)) {
                    final Film savedFilm = films.get(film.getId());
                    if (!(savedFilm.getName().equals(film.getName()))) {
                        final String name = film.getName();
                        savedFilm.setName(name);
                    }
                    if (!(savedFilm.getDescription().equals(film.getDescription()))) {
                        final String description = film.getDescription();
                        savedFilm.setDescription(description);
                    }
                    if (!(savedFilm.getReleaseDate().equals(film.getReleaseDate()))) {
                        final LocalDate releaseDate = film.getReleaseDate();
                        savedFilm.setReleaseDate(releaseDate);
                    }
                    if (!(savedFilm.getDuration() == (film.getDuration()))) {
                        final int duration = film.getDuration();
                        savedFilm.setDuration(duration);
                    }
                }
                return film;
            } else {
                throw new ValidationException("Фильм не обновлен");
            }
        } else {
            throw new RuntimeException("Ошибка, фильм не задан");
        }
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    private boolean filmValidation(Film film) {
        final int maxDescriptionLength = 200;
        final int minDurationFilm = 0;
        boolean isValidation = false;
        if (film != null) {
            if (film.getName().isBlank()) {
                log.warn("Валидация названия фильма " + film.getName() + " завершена ошибкой");
                throw new ValidationException("Название фильма не может быть пустым.");
            } else if (film.getDescription().length() > maxDescriptionLength) {
                log.warn("Валидация описания фильма " + film.getName() + " завершена ошибкой");
                throw new ValidationException("Максимальная длина описания более 200 символов.");
            } else if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                log.warn("Валидация даты релиза фильма " + film.getName() + " завершена ошибкой");
                throw new ValidationException("Дата релиза раньше 28 декабря 1895 года");
            } else if (film.getDuration() <= minDurationFilm) {
                log.warn("Валидация  продолжительности фильма " + film.getName() + " завершена ошибкой");
                throw new ValidationException("Продолжительность фильма должна быть положительной.");
            } else {
                log.info("Валидация фильма " + film.getName() + " выполнена успешно");
                isValidation = true;
            }
        }
        return isValidation;
    }

    private boolean filmVerification(Film film) {
        boolean isFilmVerification = true;
        if (!films.isEmpty()) {
            if (films.containsValue(film)) {
                log.warn("Фильм: " + film.getName() + " зарегистрирован ранее");
                isFilmVerification = false;
            }
        }
        return isFilmVerification;
    }

    private void generateIdFilms(Film film) {
        if (film.getId() == 0) {
            film.setId(++id);
        } else if ((film.getId() > id) || (film.getId() < id)) {
            film.setId(++id);
        }
    }
}
