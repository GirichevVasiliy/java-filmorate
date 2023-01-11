package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @Override
    public Film addFilm(Film film) {
        if (!Objects.isNull(film)) {
            generateIdFilms(film);
            films.put(film.getId(), film);
            log.info("Добавление нового фильма " + film.getName() + " в хранилище - выполнено");
        } else {
            throw new ErrorServer("Фильм не сохранен, ошибка сервера");
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!Objects.isNull(film)) {
            final Film savedFilm = films.get(film.getId());
            savedFilm.setName(film.getName());
            savedFilm.setDescription(film.getDescription());
            savedFilm.setReleaseDate(film.getReleaseDate());
            savedFilm.setDuration(film.getDuration());
            log.info("Обновление фильма " + film.getName() + " в хранилище - выполнено");
        } else {
            throw new ErrorServer("Фильм не обновлен, ошибка сервера");
        }
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

      public Map<Integer, Film> getFilms() {
        return films;
    }

    private void generateIdFilms(Film film) {
        if (film.getId() == 0) {
            film.setId(++id);
        }
    }

}
