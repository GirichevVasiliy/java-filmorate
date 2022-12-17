package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Map<Integer, Film> getFilms();

    Film addFilm(Film film);


    Film updateFilm(Film film);

    Film getFilmById(int id);

}

