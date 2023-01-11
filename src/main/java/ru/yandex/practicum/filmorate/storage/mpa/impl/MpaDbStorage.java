package ru.yandex.practicum.filmorate.storage.mpa.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPA> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public MPA getById(Integer id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING WHERE ID_MPA_RATING=?", id);
        if (mpaRows.next()) {
            return new MPA(mpaRows.getInt("id_mpa_rating"), mpaRows.getString("rating_name"));
        } else {
            throw new ResourceNotFoundException("Такого рейтинга не существует");
        }
    }

    private MPA makeMPA(ResultSet rs) throws SQLException {
        return new MPA(rs.getInt("id_mpa_rating"), rs.getString("rating_name"));
    }
}
