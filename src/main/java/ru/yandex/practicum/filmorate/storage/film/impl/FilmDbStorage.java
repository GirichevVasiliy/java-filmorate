package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query("SELECT * FROM MODEL_FILM AS mf INNER JOIN MPA_RATING AS mpa " +
                "ON mf.MPARATING_RATING = mpa.ID_MPA_RATING", new FilmMapper());
    }

    @Override
    public Film addFilm(Film film) {

        try {
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String query = "INSERT INTO MODEL_FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPARATING_RATING) VALUES (?, ?, ?, ?, ? );";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, Date.valueOf(film.getReleaseDate()));
                statement.setInt(4, film.getDuration());
                statement.setInt(5, film.getMpa().getId());
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    int id = keys.getInt(1);
                    film.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int amountLines = jdbcTemplate.update("UPDATE MODEL_FILM SET NAME=?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASEDATE = ?, " +
                        "DURATION= ?, " +
                        "MPARATING_RATING =? " +
                        "WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (amountLines == 0) {
            throw new ResourceNotFoundException("Фильм для изменения не найден");
        }
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        Film film = new Film();
        try {
            film = jdbcTemplate.queryForObject("SELECT * FROM MODEL_FILM AS mf INNER JOIN MPA_RATING AS mpa " +
                    "ON mf.MPARATING_RATING = mpa.ID_MPA_RATING WHERE FILM_ID=?", new FilmMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Фильм с ID " + id + " не найден или отсуствует");
        }
        return film;
    }
}
