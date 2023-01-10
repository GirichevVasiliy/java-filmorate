package ru.yandex.practicum.filmorate.storage.like.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.storage.like.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int userId, int filmId) {
        try {
            int like = jdbcTemplate.update("INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?, ?)", filmId, userId);
            if (like == 0) {
                throw new ErrorServer("Пользователь с ID = " + userId + " " + "ранее поставил лайк фильму с ID = " + filmId);
            }
        } catch (ErrorServer e) {
            throw new ErrorServer("Лайкнуть не удалось");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        try {
            int like = jdbcTemplate.update("DELETE FROM FILM_LIKES WHERE film_id=? AND user_id=?", filmId, userId);
            if (like == 0) {
                throw new ErrorServer("Пользователь с ID = " + userId + " " + "ранее непоставил лайк фильму " +
                        "с ID = " + filmId);
            }
        } catch (ErrorServer e) {
            throw new ErrorServer("Не удалось удалить лайк, ошибка запроса");
        }
    }

    @Override
    public Collection<Integer> getFilmLikeId(int filmId) {
        try {
            return jdbcTemplate.query("SELECT user_id FROM FILM_LIKES WHERE film_id=?", new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt("user_id");
                }
            }, filmId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
