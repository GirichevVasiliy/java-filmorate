package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<String, LocalDate> databaseOfFilmsForVerification = new TreeMap<>();
    private int id;

    @Override
    public Film addFilm(Film film) {
        generateIdFilms(film);
        films.put(film.getId(), film);
        databaseOfFilmsForVerification.put(film.getName(), film.getReleaseDate());
        log.info("Добавление нового фильма " + film.getName() + " в хранилище - выполнено");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
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
        log.info("Обновление фильма " + film.getName() + " в хранилище - выполнено");
        return film;
    }

    @Override
    public Collection<Film> getAllFilm() {
        return films.values();
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Map<String, LocalDate> getDatabaseOfFilmsForVerification() {
        return databaseOfFilmsForVerification;
    }

    private void generateIdFilms(Film film) {
        if (film.getId() == 0) {
            film.setId(++id);
        }
    }

}
