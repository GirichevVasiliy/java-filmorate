package ru.yandex.practicum.filmorate.storage.genre.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM GENRE_DIRECTORY;", new GenreMapper());
    }

    @Override
    public Genre getById(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE_DIRECTORY WHERE ID=?", id);
        if (genreRows.next()) {
            return new Genre(genreRows.getInt("id"), genreRows.getString("genre_name"));
        } else {
            throw new ResourceNotFoundException("Ошибочный запрос, жанр отсутствует");
        }
    }

    @Override
    public Collection<Genre> getByFilmId(Integer filmId) {
        String sql1 = "SELECT fg.GENRE_ID as ID, g.GENRE_NAME FROM FILMS_GENRE AS fg JOIN GENRE_DIRECTORY AS g ON fg.genre_id = g.id WHERE FILM_ID = ?;";
        return jdbcTemplate.query(sql1, new GenreMapper(), filmId);
    }

    @Override
    public void assignGenre(Integer filmId, Integer genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS_GENRE WHERE FILM_ID = ? AND GENRE_ID = ?",
                filmId, genreId);
        if (!genreRows.next()) {
            jdbcTemplate.update("INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);", filmId, genreId);
        }
    }

    @Override
    public void delete(Integer filmId) {
        jdbcTemplate.update("DELETE FROM FILMS_GENRE WHERE FILM_ID=?;", filmId);
    }
}
