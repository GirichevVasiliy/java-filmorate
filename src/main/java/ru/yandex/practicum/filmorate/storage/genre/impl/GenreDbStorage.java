package ru.yandex.practicum.filmorate.storage.genre.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM GENRE_DIRECTORY", new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public Genre getById(Integer id) {
        return jdbcTemplate.query("SELECT * FROM GENRE_DIRECTORY WHERE ID_GENRE=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Genre.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public Collection<Genre> getByFilmId(Integer filmId) {
        jdbcTemplate.query("SELECT fg.GENRE_ID as ID, gd.GENRE_NAME FROM FILMS_GENRE AS fg INNER JOIN GENRE_DIRECTORY " +
                "AS gd ON fg.GENRE_ID = gd.ID_GENRE WHERE FILM_ID;", new BeanPropertyRowMapper<>(Genre.class), filmId);
        return null;
    }

    @Override
    public void assignGenre(Integer filmId, Integer genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS_GENRE WHERE FILM_ID = ? AND GENRE_ID = ?;", filmId, genreId);
        if (!genreRows.next()) {
            jdbcTemplate.update("INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);", filmId, genreId);
        }

    }

    @Override
    public void delete(Integer filmId) {

    }
}
