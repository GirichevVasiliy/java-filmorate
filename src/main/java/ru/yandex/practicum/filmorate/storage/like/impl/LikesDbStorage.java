package ru.yandex.practicum.filmorate.storage.like.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.storage.like.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int userId, int filmId) {
        try {
            jdbcTemplate.update("MERGE INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?);", filmId, userId);
        } catch (ErrorServer e) {
            throw new ResourceNotFoundException("Лайкнуть не удалось");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        int like = jdbcTemplate.update("DELETE FROM FILM_LIKES WHERE FILM_ID=? AND USER_ID=?;", filmId, userId);
        if (like == 0) {
            throw new ResourceNotFoundException("Пользователь с ID = " + userId + " " + "ранее непоставил лайк фильму " +
                    "с ID = " + filmId);
        }
    }

    @Override
    public Collection<Integer> getFilmLikeId(int filmId) {
        String sqlQuery = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
        try {
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createLikeId(rs), filmId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private int createLikeId(ResultSet rs) throws SQLException {
        return rs.getInt("USER_ID");
    }
}
