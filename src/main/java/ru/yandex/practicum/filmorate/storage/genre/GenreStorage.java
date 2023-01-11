package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Collection<Genre> getAll();

    Genre getById(Integer id);

    Collection<Genre> getByFilmId(Integer filmId);

    void assignGenre(Integer filmId, Integer genreId);

    void delete(Integer filmId);
}
