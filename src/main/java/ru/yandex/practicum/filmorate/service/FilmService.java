package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        if (!Objects.isNull(film)) {
            if (filmVerification(film) && filmValidation(film)) {
                return filmStorage.addFilm(film);
            } else {
                log.warn("Добавление нового фильма " + film.getName() + "в хранилище - не выполнен");
                throw new ValidationException("Фильм " + film.getName() + " не сохранен, он был зарегистрирован ранее");
            }
        } else {
            throw new RuntimeException("Ошибка, фильм не задан");
        }
    }

    public Film updateFilm(Film film) {
        Film filmForStorage = null;
        if (!Objects.isNull(film)) {
            if (filmStorage.getFilms().containsKey(film.getId())) {
                if (filmValidation(film)) {
                    filmForStorage = filmStorage.updateFilm(film);
                }
            } else {
                throw new ResourceNotFoundException("Фильм " + film.getName() + " не обновлен");
            }
        } else {
            throw new RuntimeException("Ошибка, фильм не задан");
        }
        return filmForStorage;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.getAllFilm();
    }

    public Film getFilmById(int id) {
        if (filmStorage.getFilms().containsKey(id)) {
            return filmStorage.getFilmById(id);
        } else {
            throw new ResourceNotFoundException("Фильм c ID: " + id + " не найден");
        }
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
        if (!filmStorage.getAllFilm().isEmpty() && !filmStorage.getDatabaseOfFilmsForVerification().isEmpty()) {
            if (filmStorage.getDatabaseOfFilmsForVerification().containsKey(film.getName()) &&
                    filmStorage.getDatabaseOfFilmsForVerification().containsValue(film.getReleaseDate())) {
                log.warn("Фильм: " + film.getName() + " зарегистрирован ранее");
                isFilmVerification = false;
            }
        }
        return isFilmVerification;
    }
}
