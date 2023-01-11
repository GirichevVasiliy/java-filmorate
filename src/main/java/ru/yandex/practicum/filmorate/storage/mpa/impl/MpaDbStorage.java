package ru.yandex.practicum.filmorate.storage.mpa.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPA> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING;", new BeanPropertyRowMapper<>(MPA.class));
    }

    @Override
    public MPA getById(Integer id) {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING WHERE ID_MPA_RATING=?;", new Object[]{id},
                        new BeanPropertyRowMapper<>(MPA.class))
                .stream().findAny().orElse(null);
    }
}
