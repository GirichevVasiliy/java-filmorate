package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorageForFilm;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorageForFilm) {
        this.filmStorage = filmStorage;
        this.userStorageForFilm = userStorageForFilm;
    }

    public Film createFilm(Film film) {
        if (!Objects.isNull(film)) {
            if (filmVerification(film) && filmValidation(film)) {
                log.info("Добавлен новый фильм" + film.getName());
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
                    log.info("Фильм " + film.getName() + " успешно обновлен");
                }
            } else {
                log.warn("Обновление фильма " + film.getName() + "не выполнено");
                throw new ResourceNotFoundException("Фильм " + film.getName() + " не обновлен");
            }
        } else {
            throw new RuntimeException("Ошибка, фильм не задан");
        }
        return filmForStorage;
    }

    public Collection<Film> findAllFilms() {
        log.info("Запущен метод получения всех фильмов");
        return filmStorage.getAllFilm();
    }

    public Film getFilmById(int id) {
        if (filmStorage.getFilms().containsKey(id)) {
            log.info("Поиск фильма по ID = " + id);
            return filmStorage.getFilmById(id);
        } else {
            log.warn("Фильм c ID: " + id + " не найден");
            throw new ResourceNotFoundException("Фильм c ID: " + id + " не найден");
        }
    }

    public void addLikeFilm(Integer id, Integer userId) {
        if (id >= 0 && filmStorage.getFilms().containsKey(id)) {
            if (userId >= 0 && userStorageForFilm.getUsers().containsKey(userId)) {
                filmStorage.getFilms().get(id).setLikes(userId);
                log.info("Добавлен лайк фильму ID = " + id + " пользователем с ID = " + userId);
            } else {
                log.warn("Пользователь c ID: " + userId + " не найден");
                throw new ResourceNotFoundException("Пользователь c ID: " + userId + " не найден");
            }
        } else {
            log.warn("Фильм c ID: " + id + " не найден");
            throw new ResourceNotFoundException("Фильм c ID: " + id + " не найден");
        }
    }

    public void deleteLikeFilm(Integer id, Integer userId) {
        if (id >= 0 && filmStorage.getFilms().containsKey(id)) {
            if (userId >= 0 && userStorageForFilm.getUsers().containsKey(userId)) {
                filmStorage.getFilms().get(id).getLikes().remove(userId);
                log.info("Удален лайк у фильма ID = " + id + " пользователем с ID = " + userId);
            } else {
                log.warn("Пользователь c ID: " + userId + " не найден");
                throw new ResourceNotFoundException("Пользователь c ID: " + userId + " не найден");
            }
        } else {
            log.warn("Фильм c ID: " + id + " не найден");
            throw new ResourceNotFoundException("Фильм c ID: " + id + " не найден");
        }
    }

    public Collection<Film> findTopTenMostLikesFilms(Integer count) {
        log.info("Получен запрос на список популярных фильмов");
        Collection<Film> films = filmStorage.getFilms().values();
        return films.stream()
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
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
