package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
        return jdbcTemplate.query("SELECT * FROM MODEL_FILM;", new BeanPropertyRowMapper<>(Film.class));
    }

    @Override
    public Film addFilm(Film film) {
        int id = 0;
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
                    id = keys.getInt(1);
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
        jdbcTemplate.update("UPDATE MODEL_FILM SET NAME=?, DESCRIPTION=?, RELEASEDATE=?, DURATION=?, MPARATING_RATING=?" +
                        " WHERE FILM_ID=?;", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa(), film.getId());
        Film updateFilm = jdbcTemplate.queryForObject("SELECT * FROM MODEL_FILM WHERE FILM_ID=?",
                new BeanPropertyRowMapper<>(Film.class), film.getId());
        return updateFilm;
    }

    @Override
    public Film getFilmById(int id) {
        return jdbcTemplate.query("SELECT * FROM MODEL_FILM WHERE FILM_ID=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Film.class))
                .stream().findAny().orElse(null);
    }
}
