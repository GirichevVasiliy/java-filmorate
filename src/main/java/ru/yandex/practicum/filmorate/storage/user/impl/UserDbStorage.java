package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        int id = 0;
        try {
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String query = "INSERT INTO MODEL_USER (email, login, name, birthday) VALUES (?, ?, ?, ?);";
            try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getLogin());
                pstmt.setString(3, user.getEmail());
                pstmt.setDate(4, Date.valueOf(user.getBirthday()));
                pstmt.executeUpdate();
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE MODEL_USER SET email=?, login=?, name=?, birthday=? WHERE user_id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        User updateUser = jdbcTemplate.queryForObject("SELECT * FROM MODEL_USER WHERE user_id=?",
                new BeanPropertyRowMapper<>(User.class), user.getId());
        return updateUser;
    }

    @Override
    public Collection<User> getAllUser() {
        return jdbcTemplate.query("SELECT * FROM MODEL_USER", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User getUserById(int id) {
        return jdbcTemplate.query("SELECT * FROM MODEL_USER WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    // Данные методы тут не используются, но они есть в интерфейсе для работы хранилища
    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }

    @Override
    public Set<String> getEmails() {
        return null;
    }
}
