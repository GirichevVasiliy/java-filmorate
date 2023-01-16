package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikesStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("likesDbStorage") LikesStorage likesStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
    }

    public Film createFilm(Film film) {
        if (!Objects.isNull(film)) {
            if (filmValidation(film)) {
                Film createdFilm = filmStorage.addFilm(film);
                addGenreToFilm(film.getGenres(), createdFilm.getId());
                log.info("Добавлен новый фильм" + film.getName());
                return addLikesAndGenreToStorage(createdFilm);
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
        if (filmValidation(film)) {
            deleteGenreToFilm(genreStorage.getByFilmId(film.getId()), film.getId());
            addGenreToFilm(film.getGenres(), film.getId());
            filmForStorage = addLikesAndGenreToStorage(filmStorage.updateFilm(film));
            log.info("Фильм " + film.getName() + " успешно обновлен");
        } else {
            log.warn("Обновление фильма " + film.getName() + "не выполнено");
            throw new ResourceNotFoundException("Фильм " + film.getName() + " не обновлен");
        }

        return filmForStorage;
    }

    public Collection<Film> findAllFilms() {
        log.info("Запущен метод получения всех фильмов");
        Collection<Film> allFilms = filmStorage.getAllFilms();
        Map<Integer, Film> l = new HashMap();
        for (Film film : allFilms) {
            int id = film.getId();
            if (!l.containsKey(id)) {
                l.put(id, film);
            } else {
                l.get(film.getId()).getWhoLikedUserIds().addAll(film.getWhoLikedUserIds());
                l.get(film.getId()).getGenres().addAll(film.getGenres());
            }
        }
        Collection<Film> allFilmsFull = new ArrayList<>(l.values());
        return allFilmsFull;
    }

    public Film getFilmById(int id) {
        log.info("Поиск фильма по ID = " + id);
        return addLikesAndGenreToStorage(filmStorage.getFilmById(id));
    }

    public void addLikeFilm(Integer id, Integer userId) {
        log.info("Запрос добавления лайка");
        likesStorage.addLike(userId, id);

    }

    public void deleteLikeFilm(Integer id, Integer userId) {
        log.info("Получен запрос на удаление лайка");
        likesStorage.deleteLike(userId, id);

    }

    public Collection<Film> findTopMostLikedFilms(Integer count) {
        log.info("Получен запрос на список популярных фильмов");
        Collection<Film> films = filmStorage.getAllFilms();
        films.forEach(this::addLikesAndGenreToStorage);
        return films.stream()
                .sorted(Comparator.comparing(Film::getLikeCount).reversed())
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

    private Film addLikesAndGenreToStorage(Film film) {
        film.setGenres(genreStorage.getByFilmId(film.getId()));
        film.setWhoLikedUserIds(likesStorage.getFilmLikeId(film.getId()));
        return film;
    }

    private void addGenreToFilm(Collection<Genre> genres, int filmId) {
            genres.forEach(g -> genreStorage.assignGenre(filmId, g.getId()));
    }

    private void deleteGenreToFilm(Collection<Genre> genres, int filmId) {
        genres.forEach(g -> genreStorage.delete(filmId));
    }

}
