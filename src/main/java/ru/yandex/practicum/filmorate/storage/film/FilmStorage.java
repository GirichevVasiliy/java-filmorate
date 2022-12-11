package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> getAllFilm();

    Map<Integer, Film> getFilms();

    Map<String, LocalDate> getDatabaseOfFilmsForVerification();

    Film addFilm(Film film);


    Film updateFilm(Film film);

    Film getFilmById(int id);

    void deleteFilm(int id);
}

